package socialnetwork.repo.db;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repo.Repository;

import java.sql.*;
import java.util.*;

public class FriendshipDB implements Repository<UUID, Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;

    public FriendshipDB(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Friendship findOne(UUID id) {
        String sql = String.format("SELECT * FROM \"Friendships\" WHERE id = '" + id.toString() + "'");
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()){
            rs.next();
            UUID fid = (UUID) rs.getObject("id");
            UUID id1 = (UUID) rs.getObject("id1");
            UUID id2 = (UUID) rs.getObject("id2");
            String status = rs.getString("status");
            String date = rs.getString("friends_from");
            Friendship f = new Friendship(fid, id1, id2, status, date);
            return f;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> ret = new HashSet<Friendship>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"Friendships\"");
             ResultSet rs = statement.executeQuery()){

            while(rs.next()){
                UUID id = (UUID) rs.getObject("id");
                UUID id1 = (UUID) rs.getObject("id1");
                UUID id2 = (UUID) rs.getObject("id2");
                String status = rs.getString("status");
                String date = rs.getString("friends_from");
                Friendship f = new Friendship(id, id1, id2, status, date);

                ret.add(f);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return ret;
    }

    @Override
    public Friendship save(Friendship f) {
        validator.validate(f);
        String sql = "INSERT INTO \"Friendships\" (id, id1, id2, status, friends_from) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, f.getId());
            statement.setObject(2, f.getId1());
            statement.setObject(3, f.getId2());
            statement.setString(4, f.getStatus());
            statement.setString(5, f.getTime().toString());
            statement.executeUpdate();
            return f;
        } catch (SQLException throwable) {
            return null;
        }
    }


    @Override
    public Friendship delete(UUID id) {
        Friendship f = findOne(id);
        if(f == null)
            return null;
        String sql = String.format("DELETE from \"Friendships\" WHERE id = '" + id.toString() + "'");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            return f;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    public Friendship update(Friendship friendship) {
        validator.validate(friendship);
        if(friendship == null)
            return null;
        String sql = String.format("UPDATE \"Friendships\" SET id = '" +
                friendship.getId() +
                "', id1 = '" +
                friendship.getId1() +
                "', id2 = '" +
                friendship.getId2() +
                "', friends_from = '" +
                friendship.getFriendsFrom().toString() +
                "', status = '" +
                "accepted" +
                "' WHERE id = '" +
                friendship.getId().toString() +"'");
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            return friendship;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }


}


