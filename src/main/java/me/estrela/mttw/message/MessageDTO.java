package me.estrela.mttw.message;

import me.estrela.mttw.DataTransferObject;
import me.estrela.mttw.generated.tables.Message;
import me.estrela.mttw.generated.tables.records.MessageRecord;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public final class MessageDTO implements DataTransferObject<MessageRecord> {

    private final String id;
    private final String eventId;
    private final String text;
    private final String author;
    private final int upVotes;
    private final int downVotes;
    private final LocalDateTime publishedDate;
    private final LocalDateTime presentedDate;

    private MessageDTO(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.eventId = builder.eventId;
        this.text = builder.text;
        this.author = builder.author;
        this.upVotes = builder.upVotes;
        this.downVotes = builder.downVotes;
        this.publishedDate = builder.publishedDate;
        this.presentedDate = builder.presentedDate;
    }

    public String getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public LocalDateTime getPresentedDate() {
        return presentedDate;
    }

    public static class Builder {

        private String id;
        private String eventId;
        private String text;
        private String author;
        private int upVotes;
        private int downVotes;
        private LocalDateTime publishedDate;
        private LocalDateTime presentedDate;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withUpVotes(int upVotes) {
            this.upVotes = upVotes;
            return this;
        }

        public Builder withDownVotes(int downVotes) {
            this.downVotes = downVotes;
            return this;
        }

        public Builder withPublishedDate(LocalDateTime publishedDate) {
            this.publishedDate = publishedDate;
            return this;
        }

        public Builder withPresentedDate(LocalDateTime presentedDate) {
            this.presentedDate = presentedDate;
            return this;
        }

        public Builder withComputedPresentedDate(Optional<MessageDTO> lastMessage, LocalDateTime timeReference, long messageDuration) {
            this.presentedDate = getPresentedDate(lastMessage, timeReference, messageDuration);
            return this;
        }

        private LocalDateTime getPresentedDate(Optional<MessageDTO> lastMessage, LocalDateTime timeReference, long messageDuration) {
            return lastMessage.map(lm -> {
                LocalDateTime lastMessagePresented = lm.getPresentedDate();
                if (lastMessagePresented.isBefore(timeReference.minusSeconds(messageDuration))) {
                    return timeReference;
                } else {
                    return lastMessagePresented.plusSeconds(messageDuration);
                }
            }).orElse(timeReference);
        }

        public MessageDTO build() {
            return new MessageDTO(this);
        }

    }

    public static Optional<MessageDTO> fromRecord(MessageRecord messageRecord) {
        return Optional.ofNullable(messageRecord).map(mr -> new Builder()
                .withId(mr.getId())
                .withEventId(mr.getEventId())
                .withAuthor(mr.getAuthor())
                .withText(mr.getText())
                .withDownVotes(mr.getDownVotes())
                .withUpVotes(mr.getUpVotes())
                .withPresentedDate(mr.getPresentedDate())
                .withPublishedDate(mr.getPublishedDate())
                .build());
    }

    @Override
    public MessageRecord toRecord(DSLContext dsl) {
        MessageRecord newMessage = dsl.newRecord(Message.MESSAGE);
        newMessage.setId(this.id);
        newMessage.setEventId(this.eventId);
        newMessage.setAuthor(this.author);
        newMessage.setText(this.text);
        newMessage.setUpVotes(this.upVotes);
        newMessage.setDownVotes(this.downVotes);
        newMessage.setPublishedDate(this.publishedDate);
        newMessage.setPresentedDate(this.presentedDate);
        return newMessage;
    }

    @Override
    public String toString() {
        return String.format("MessageDTO (id=%s, eventId=%s, text=%s, author=%s, upVotes=%s, downVotes=%s, " +
                        "publishedDate=%s, presentedDate=%s)", this.id, this.eventId, this.text, this.author,
                this.upVotes, this.downVotes, this.publishedDate, this.presentedDate);
    }

}