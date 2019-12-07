package me.estrela.mttw;

import me.estrela.mttw.message.MessageDTO;
import me.estrela.mttw.message.MessageEvent;
import me.estrela.mttw.message.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void shouldBeAbleToPublishAndStoreMessages() {
        messageRepository.deleteAll();

        for (int i = 0; i < 10; i++) {

            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setAuthor("test");
            messageEvent.setText(UUID.randomUUID().toString());

            ResponseEntity response = this.restTemplate.postForEntity(getUrl("/message", port), messageEvent, MessageEvent.class);

            assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            MessageDTO message = getMessage(messageEvent.getId());

            assertEquals(messageEvent.getId(), message.getEventId());
            assertEquals(messageEvent.getAuthor(), message.getAuthor());
            assertEquals(messageEvent.getText(), message.getText());
            assertEquals(0, message.getUpVotes());
            assertEquals(0, message.getDownVotes());

        }

    }

    private MessageDTO getMessage(String id) {
        for (int i = 0; i < 3; i++) {
            Optional<MessageDTO> message = messageRepository.findByEventId(id);
            if (message.isPresent()) {
                return message.get();
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException ignore) {
                fail();
            }
        }
        throw new AssertionError("failed to load message");
    }

    public static String getUrl(String URI, int port) {
        return "http://localhost:" + port + URI;
    }

}
