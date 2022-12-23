package socialnetwork.repo.db;

import socialnetwork.domain.Message;
import socialnetwork.repo.Repository;

import java.sql.*;
import java.util.*;

public class MessageDB implements Repository<UUID, Message> {
    private String url;
    private String username;
    private String password;

    public MessageDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    @Override
    public Message findOne(UUID id) {
        String sql = String.format("SELECT * FROM \"Messages\" WHERE id = '" + id.toString() + "'");
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()){
            rs.next();
            UUID messageId = (UUID) rs.getObject("id");
            UUID sentBy = (UUID) rs.getObject("sender");
            UUID receivedBy = (UUID) rs.getObject("receiver");
            String messageText= rs.getString("text");
            String time = rs.getString("timeSent");
            return new Message(messageId, sentBy, receivedBy, messageText, time);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> ret = new HashSet<Message>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"Messages\"");
             ResultSet rs = statement.executeQuery()){

            while(rs.next()){
                UUID id = (UUID) rs.getObject("id");
                UUID sentBy = (UUID) rs.getObject("sender");
                UUID receivedBy = (UUID) rs.getObject("receiver");
                String messageText= rs.getString("text");
                String time = rs.getString("timeSent");
                Message m = new Message(id, sentBy, receivedBy, messageText, time);

                ret.add(m);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return ret;
    }

    @Override
    public Message save(Message m) {
        String sql = "INSERT INTO \"Messages\" (id, sender, receiver, text, timesent) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, m.getId());
            statement.setObject(2, m.getSentBy());
            statement.setObject(3, m.getReceivedBy());
            statement.setString(4, m.getMessageText());
            statement.setString(5, m.getTimeSent().toString());
            statement.executeUpdate();
            return m;
        } catch (SQLException throwable) {
            return null;
        }
    }


    @Override
    public Message delete(UUID id) {
        Message m = findOne(id);
        if(m == null)
            return null;
        String sql = String.format("DELETE FROM \"Messages\" WHERE id = '" + id.toString() + "'");
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            return m;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    public Message update(Message friendship) {
        return null;
    }


}