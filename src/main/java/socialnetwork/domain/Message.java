package socialnetwork.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Message extends socialnetwork.domain.Entity<UUID> implements Comparable<Message> {
    private UUID sentBy;
    private UUID receivedBy;
    private String messageText;
    private LocalDateTime timeSent;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd YYYY hh:mm:ss");

    public Message(UUID sentBy, UUID receivedBy, String messageText){
        this.setId(UUID.randomUUID());

        this.sentBy = sentBy;
        this.receivedBy = receivedBy;

        this.messageText = messageText;
        this.timeSent = LocalDateTime.now();
        dtf.format(this.timeSent);
    }

    public Message(UUID id, UUID sentBy, UUID receivedBy, String messageText, String timeSent){
        this.setId(id);

        this.sentBy = sentBy;
        this.receivedBy = receivedBy;

        this.messageText = messageText;
        this.timeSent = LocalDateTime.parse(timeSent);
        dtf.format(this.timeSent);

    }

    public UUID getSentBy() {
        return sentBy;
    }

    public UUID getReceivedBy() {
        return receivedBy;
    }

    public String getMessageText() {
        return messageText;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sentBy, message.sentBy) && Objects.equals(receivedBy, message.receivedBy) && Objects.equals(messageText, message.messageText) && Objects.equals(timeSent, message.timeSent) && Objects.equals(dtf, message.dtf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentBy, receivedBy, messageText, timeSent, dtf);
    }

    @Override
    public String toString() {
        return "Sent by" + sentBy.toString() +
                ", Received by=" + receivedBy.toString();
    }

    @Override
    public int compareTo(Message u) {
        if (getTimeSent() == null || u.getTimeSent() == null) {
            return 0;
        }
        return getTimeSent().compareTo(u.getTimeSent());
    }

}
