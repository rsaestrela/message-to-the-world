package me.estrela.mttw.message;

import me.estrela.mttw.GenericRepository;
import me.estrela.mttw.generated.tables.records.MessageRecord;

import java.time.LocalDateTime;
import java.util.Optional;


public interface MessageRepository extends GenericRepository<MessageDTO, MessageRecord> {

    Optional<MessageDTO> findLastMessage();

    Optional<MessageDTO> findCurrentMessage(LocalDateTime dateTime);

    Optional<MessageDTO> findByEventId(String eventId);

}
