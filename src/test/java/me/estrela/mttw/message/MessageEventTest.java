package me.estrela.mttw.message;

import me.estrela.mttw.TimeMachine;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MessageEventTest {

    @Test
    public void shouldCreateObject() {

        TimeMachine.Zoned zoned = new TimeMachine().utc();

        MessageEvent messageEvent = new MessageEvent.Builder().withText("text").withAuthor("author").build(zoned);

        assertNotNull(messageEvent.getId());
        assertEquals(36, messageEvent.getId().length());
        assertEquals("text", messageEvent.getText());
        assertEquals("author", messageEvent.getAuthor());
        assertEquals(zoned.now(), messageEvent.getRequestDate());

    }

}