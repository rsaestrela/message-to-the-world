package me.estrela.mttw.message;

import me.estrela.mttw.GenericRepository;
import me.estrela.mttw.generated.tables.records.MessageRecord;

import java.time.LocalDateTime;
import java.util.Optional;


public interface MessageRepository extends GenericRepository<Message, MessageRecord> {

    Optional<Message> findLastMessage();

    Optional<Message> findCurrentMessage(LocalDateTime dateTime);

    Optional<Message> findByEventId(String eventId);

}
