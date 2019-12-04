package me.estrela.mttw.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Optional;

@Component
public class MessageService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageRepository messageRepository;

    public void publish(MessageEvent messageEvent) {
        jmsTemplate.convertAndSend("message_queue", messageEvent);
    }

    @JmsListener(destination = "message_queue", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(MessageEvent messageEvent) {
        Message message = new Message();
        message.setEventId(messageEvent.getId());
        message.setAuthor(messageEvent.getAuthor());
        message.setText(messageEvent.getText());
        message.setPublishedDate(messageEvent.getCreatedDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        messageRepository.save(message);
    }

    public Optional<Message> currentMessage() {
        throw new UnsupportedOperationException();
    }

}
