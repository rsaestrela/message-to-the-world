package me.estrela.mttw.message;

import java.util.Date;
import java.util.UUID;

public class MessageEvent {

    private final String id = UUID.randomUUID().toString();

    private final Date createdDate = new Date();

    private String text;

    private String author;

    public String getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return String.format(
                "MessageEvent (id=%s, createdDate=%s, text=%s, author=%s)", this.id, this.createdDate, this.text, this.author);
    }

}
