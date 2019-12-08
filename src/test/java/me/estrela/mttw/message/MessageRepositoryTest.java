package me.estrela.mttw.message;

import me.estrela.mttw.TimeMachine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TimeMachine timeMachine;

    @Before
    public void cleanUp() {
        messageRepository.deleteAll();
    }

    @Test
    public void shouldFindByEventId() {

        Message message1 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("123")
                .build();
        Message message2 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("456")
                .build();
        Message message3 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("789").build();

        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);

        assertEquals(messageRepository.count(), 3);

        Optional<Message> message = messageRepository.findByEventId("456");

        assertTrue(message.isPresent());

        assertEquals("456", message.get().getEventId());

        assertFalse(messageRepository.findByEventId("999").isPresent());

    }

    @Test
    public void shouldFindLastMessage() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowMinus15m = now.minusMinutes(15);
        LocalDateTime nowPlus5s = now.plusSeconds(5);
        LocalDateTime nowPlus10m = now.plusMinutes(10);
        LocalDateTime nowPlus2h = now.plusHours(2);

        Message message1 = new Message.Builder()
                .withAuthor("author").withText("text")
                .withEventId("123")
                .withPresentedDate(nowMinus15m)
                .build();
        Message message2 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("456")
                .withPresentedDate(nowPlus5s)
                .build();
        Message message3 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("789")
                .withPresentedDate(nowPlus10m)
                .build();
        Message message4 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("101")
                .withPresentedDate(nowPlus2h)
                .build();

        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);
        messageRepository.save(message4);

        assertEquals(messageRepository.count(), 4);

        Optional<Message> message = messageRepository.findLastMessage();

        assertTrue(message.isPresent());

        assertEquals("101", message.get().getEventId());

    }

    @Test
    public void shouldFindCurrentMessage() {

        TimeMachine.Zoned zoned = timeMachine.utc();

        LocalDateTime nowMinus10s = zoned.now().minusSeconds(10);
        LocalDateTime nowMinus5s = zoned.now().minusSeconds(5);
        LocalDateTime nowPlus5s = zoned.now().plusSeconds(5);
        LocalDateTime nowPlus10s = zoned.now().plusSeconds(10);

        Message message1 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("123")
                .withPresentedDate(nowMinus10s)
                .build();
        Message message2 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("456")
                .withPresentedDate(nowMinus5s)
                .build();
        Message message3 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("789").withPresentedDate(zoned.now())
                .build();
        Message message4 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("101").withPresentedDate(nowPlus5s)
                .build();
        Message message5 = new Message.Builder()
                .withAuthor("author")
                .withText("text")
                .withEventId("123").withPresentedDate(nowPlus10s)
                .build();

        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);
        messageRepository.save(message4);
        messageRepository.save(message5);

        assertEquals(messageRepository.count(), 5);

        Optional<Message> message = messageRepository.findCurrentMessage(zoned);

        assertTrue(message.isPresent());

        assertEquals("789", message.get().getEventId());

    }

}
