package me.estrela.mttw.message;

import me.estrela.mttw.GenericRepository;
import me.estrela.mttw.TimeMachine;
import me.estrela.mttw.generated.tables.records.MessageRecord;

import java.util.Optional;


public interface MessageRepository extends GenericRepository<Message, MessageRecord> {

    Optional<Message> findLastMessage();

    Optional<Message> findCurrentMessage(TimeMachine.Zoned zoned);

    Optional<Message> findByEventId(String eventId);

}
