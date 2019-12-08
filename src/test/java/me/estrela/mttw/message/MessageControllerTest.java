package me.estrela.mttw.message;

import me.estrela.mttw.TimeMachine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    @Mock
    private TimeMachine.Zoned zoned;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @Test
    public void shouldPublishMessage() {

        LocalDateTime nowMinus10m = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(10);

        when(zoned.now()).thenReturn(nowMinus10m);

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(zoned);

        when(messageService.publish(toPublish)).thenReturn(Optional.of(toPublish));

        ResponseEntity<MessageEvent> response = messageController.publishMessage(toPublish);

        verify(messageService).publish(toPublish);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(toPublish, response.getBody());

    }

    @Test
    public void shouldFailPublishMessage() {

        MessageEvent toPublish = new MessageEvent.Builder().withAuthor("author").withText("text").build(zoned);

        when(messageService.publish(toPublish)).thenReturn(Optional.empty());

        ResponseEntity<MessageEvent> response = messageController.publishMessage(toPublish);

        verify(messageService).publish(toPublish);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

    }

    @Test
    public void shouldGetCurrentMessage() {

        Message message = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("123")
                .build();

        when(messageService.currentMessage()).thenReturn(Optional.of(message));

        ResponseEntity<Message> response = messageController.currentMessage();

        verify(messageService).currentMessage();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());

    }

    @Test
    public void shouldNotGetCurrentMessage() {

        when(messageService.currentMessage()).thenReturn(Optional.empty());

        ResponseEntity<Message> response = messageController.currentMessage();

        verify(messageService).currentMessage();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

    }

}
