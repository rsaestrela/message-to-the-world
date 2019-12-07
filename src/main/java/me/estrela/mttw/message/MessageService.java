package me.estrela.mttw.message;

import me.estrela.mttw.TimeMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Optional;

@Component
public class MessageService {

    private static final long MESSAGE_DURATION = 5;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TimeMachine timeMachine;

    public Optional<MessageEvent> publish(MessageEvent messageEvent) {
        try {
            jmsTemplate.convertAndSend("message_queue", messageEvent);
            return Optional.of(messageEvent);
        } catch (JmsException ignore) {

        }
        return Optional.empty();
    }

    @JmsListener(destination = "message_queue", containerFactory = "jmsListenerContainerFactory")
    public void handleMessageEvent(MessageEvent messageEvent) {

        MessageDTO messageDTO = new MessageDTO.Builder()
                .withEventId(messageEvent.getId())
                .withAuthor(messageEvent.getAuthor())
                .withText(messageEvent.getText())
                .withPublishedDate(messageEvent.getCreatedDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .withComputedPresentedDate(messageRepository.findLastMessage(), timeMachine.now(), MESSAGE_DURATION)
                .build();

        messageRepository.save(messageDTO);

    }

    public Optional<MessageDTO> currentMessage() {
        return messageRepository.findCurrentMessage(timeMachine.now());
    }

}
