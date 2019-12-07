package me.estrela.mttw.message;

import me.estrela.mttw.JooqRepositoryImpl;
import me.estrela.mttw.generated.tables.Message;
import me.estrela.mttw.generated.tables.records.MessageRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class MessageRepositoryImpl extends JooqRepositoryImpl<MessageDTO, MessageRecord> implements MessageRepository {

    private static final Message message = Message.MESSAGE;

    @Autowired
    private DSLContext dsl;

    public MessageRepositoryImpl() {
        super(message);
    }

    @Transactional
    @Override
    public Optional<MessageDTO> findLastMessage() {
        return MessageDTO.fromRecord(
                dsl.selectFrom(message)
                        .orderBy(message.PRESENTED_DATE.desc())
                        .limit(1)
                        .fetchOne());
    }

    @Transactional
    @Override
    public Optional<MessageDTO> findCurrentMessage(LocalDateTime dateTime) {
        return MessageDTO.fromRecord(
                dsl.selectFrom(message)
                        .where(message.PRESENTED_DATE.greaterThan(dateTime))
                        .orderBy(message.PRESENTED_DATE.asc())
                        .limit(1)
                        .fetchOne());
    }

    @Transactional
    @Override
    public Optional<MessageDTO> findByEventId(String eventId) {
        return MessageDTO.fromRecord(dsl.fetchOne(message, message.EVENT_ID.eq(eventId)));
    }

}
