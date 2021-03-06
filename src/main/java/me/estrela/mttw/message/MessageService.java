package me.estrela.mttw.message;

import com.google.common.flogger.FluentLogger;
import me.estrela.mttw.TimeMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageService {

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private static final long MESSAGE_DURATION_S = 5;

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
        } catch (JmsException jmsException) {
            LOGGER.atSevere().withCause(jmsException).log("operation=publish msg='publish event failed'");
        }
        return Optional.empty();
    }

    @JmsListener(destination = "message_queue", containerFactory = "jmsListenerContainerFactory")
    public void handleMessageEvent(MessageEvent messageEvent) {

        Message message = new Message.Builder()
                .withEventId(messageEvent.getId())
                .withAuthor(messageEvent.getAuthor())
                .withText(messageEvent.getText())
                .withPublishedDate(messageEvent.getRequestDate())
                .withComputedPresentedDate(messageRepository.findLastMessage(), timeMachine.utc(), MESSAGE_DURATION_S)
                .build();

        messageRepository.save(message);

    }

    public Optional<Message> currentMessage() {
        return messageRepository.findCurrentMessage(timeMachine.utc());
    }

}
