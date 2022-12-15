package socialnetwork.domain;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;


public class Friendship extends socialnetwork.domain.Entity<UUID> {

    private String id1Name;

    private String id1Email;



    private UUID id1;
    private UUID id2;
    private String status;

    private LocalDateTime friendsFrom;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd YYYY hh:mm:ss");
    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public Friendship(UUID id1, UUID id2) {
        this.setId(UUID.randomUUID());


        this.id1 = id1;
        this.id2 = id2;

        this.status = "requested";
        friendsFrom = LocalDateTime.now();
        dtf.format(friendsFrom);


    }

    public Friendship(UUID id, UUID id1, UUID id2, String status, String date) {
        this.setId(id);

        this.id1 = id1;
        this.id2 = id2;

        this.status = status;
        friendsFrom = LocalDateTime.parse(date);
        dtf.format(friendsFrom);


    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public UUID getId1() {
        return id1;
    }

    public void setId1(UUID id1) {
        this.id1 = id1;
    }

    public UUID getId2() {
        return id2;
    }

    public void setId2(UUID id2) {
        this.id2 = id2;
    }

    public LocalDateTime getTime(){ return friendsFrom;}

    public String getId1Name() {
        return id1Name;
    }

    public void setId1Name(String id1Name) {
        this.id1Name = id1Name;
    }



    public String getId1Email() {
        return id1Email;
    }

    public void setId1Email(String id1Email) {
        this.id1Email = id1Email;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }

    @Override
    public String toString() {
        return "User 1 ID=" + id1 +
                ", User 2 ID=" + id2;
    }
}
