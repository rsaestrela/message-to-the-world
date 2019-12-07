package me.estrela.mttw.message;

import me.estrela.mttw.JooqRepositoryImpl;
import me.estrela.mttw.TimeMachine;
import me.estrela.mttw.generated.tables.records.MessageRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class MessageRepositoryImpl extends JooqRepositoryImpl<Message, MessageRecord> implements MessageRepository {

    private static final me.estrela.mttw.generated.tables.Message table = me.estrela.mttw.generated.tables.Message.MESSAGE;

    @Autowired
    private DSLContext dsl;

    public MessageRepositoryImpl() {
        super(table);
    }

    @Transactional
    @Override
    public Optional<Message> findLastMessage() {
        return Message.fromRecord(
                dsl.selectFrom(table)
                        .orderBy(table.PRESENTED_DATE.desc())
                        .limit(1)
                        .fetchOne());
    }

    @Transactional
    @Override
    public Optional<Message> findCurrentMessage(TimeMachine.Zoned zoned) {
        return Message.fromRecord(
                dsl.selectFrom(table)
                        .where(table.PRESENTED_DATE.greaterThan(zoned.now()))
                        .orderBy(table.PRESENTED_DATE.asc())
                        .limit(1)
                        .fetchOne());
    }

    @Transactional
    @Override
    public Optional<Message> findByEventId(String eventId) {
        return Message.fromRecord(dsl.fetchOne(table, table.EVENT_ID.eq(eventId)));
    }

}
