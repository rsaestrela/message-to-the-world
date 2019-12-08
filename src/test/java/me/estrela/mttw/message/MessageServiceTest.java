package me.estrela.mttw.message;

import me.estrela.mttw.TimeMachine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private TimeMachine timeMachine;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void shouldPublishMessage() {

        when(timeMachine.utc()).thenReturn(new TimeMachine().utc());

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(timeMachine.utc());

        Optional<MessageEvent> response = messageService.publish(toPublish);

        verify(jmsTemplate).convertAndSend(eq("message_queue"), eq(toPublish));

        assertTrue(response.isPresent());

        assertEquals(toPublish, response.get());

    }

    @Test
    public void shouldFailPublishingMessage() {

        when(timeMachine.utc()).thenReturn(new TimeMachine().utc());

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(timeMachine.utc());

        when(messageService.publish(toPublish)).thenThrow(new UncategorizedJmsException("oh geez"));

        Optional<MessageEvent> response = messageService.publish(toPublish);

        verify(jmsTemplate).convertAndSend(eq("message_queue"), eq(toPublish));

        assertFalse(response.isPresent());

    }

    @Test
    public void shouldHandleMessageEventNoMessagesAvailable() {

        TimeMachine.Zoned zoned = new TimeMachine().utc();

        when(timeMachine.utc()).thenReturn(zoned);

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(timeMachine.utc());

        when(messageRepository.findLastMessage()).thenReturn(Optional.empty());

        messageService.handleMessageEvent(toPublish);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);

        verify(messageRepository).save(argumentCaptor.capture());

        Message messageSaved = argumentCaptor.getValue();

        assertEquals("author", messageSaved.getAuthor());
        assertEquals("text", messageSaved.getText());
        assertEquals(zoned.now(), messageSaved.getPublishedDate());
        assertEquals(zoned.now(), messageSaved.getPresentedDate());

    }

    @Test
    public void shouldHandleMessageEventOldMessageExists() {

        TimeMachine.Zoned zoned = new TimeMachine().utc();

        when(timeMachine.utc()).thenReturn(zoned);

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(timeMachine.utc());

        Message message = new Message.Builder().withPresentedDate(LocalDateTime.now().minusDays(1)).build();

        when(messageRepository.findLastMessage()).thenReturn(Optional.of(message));

        messageService.handleMessageEvent(toPublish);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);

        verify(messageRepository).save(argumentCaptor.capture());

        Message messageSaved = argumentCaptor.getValue();

        assertEquals("author", messageSaved.getAuthor());
        assertEquals("text", messageSaved.getText());
        assertEquals(zoned.now(), messageSaved.getPresentedDate());

    }

    @Test
    public void shouldHandleMessageEventFutureMessageExists() {

        TimeMachine.Zoned zoned = new TimeMachine().utc();

        when(timeMachine.utc()).thenReturn(zoned);

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(timeMachine.utc());

        Message message = new Message.Builder().withPresentedDate(zoned.now().plusDays(15)).build();

        when(messageRepository.findLastMessage()).thenReturn(Optional.of(message));

        messageService.handleMessageEvent(toPublish);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);

        verify(messageRepository).save(argumentCaptor.capture());

        Message messageSaved = argumentCaptor.getValue();

        assertEquals("author", messageSaved.getAuthor());
        assertEquals("text", messageSaved.getText());

        //new message is presented 5 seconds after the last on the queue
        assertEquals(zoned.now().plusDays(15).plusSeconds(5), messageSaved.getPresentedDate());

    }

    @Test
    public void shouldHandleMessageEventNearFutureMessageExists() {

        TimeMachine.Zoned zoned = new TimeMachine().utc();

        when(timeMachine.utc()).thenReturn(zoned);

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(timeMachine.utc());

        Message message = new Message.Builder().withPresentedDate(zoned.now().plusSeconds(2)).build();

        when(messageRepository.findLastMessage()).thenReturn(Optional.of(message));

        messageService.handleMessageEvent(toPublish);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);

        verify(messageRepository).save(argumentCaptor.capture());

        Message messageSaved = argumentCaptor.getValue();

        assertEquals("author", messageSaved.getAuthor());
        assertEquals("text", messageSaved.getText());

        //new message is presented 5 seconds after the last on the queue
        assertEquals(zoned.now().plusSeconds(2).plusSeconds(5), messageSaved.getPresentedDate());

    }

}
