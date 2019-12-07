package me.estrela.mttw.message;

import me.estrela.mttw.TimeMachine;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageEvent {

    private final String id = UUID.randomUUID().toString();
    private LocalDateTime requestDate;
    private String text;
    private String author;

    // Serialization
    public MessageEvent() {
    }

    public MessageEvent(Builder builder, TimeMachine.Zoned zoned) {
        this.text = builder.text;
        this.author = builder.author;
        this.requestDate = zoned.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public static class Builder {

        private String text;

        private String author;

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public MessageEvent build(TimeMachine.Zoned zoned) {
            return new MessageEvent(this, zoned);
        }

    }

    @Override
    public String toString() {
        return String.format("MessageEvent (id=%s, requestDate=%s, text=%s, author=%s)", this.id, this.requestDate,
                this.text, this.author);
    }
}
