package socialnetwork.repo.db;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repo.Repository;

import java.sql.*;
import java.util.*;

public class UserDB implements Repository<UUID, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;


    public UserDB(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public User findOne(UUID id) {
        String sql = String.format("SELECT * FROM \"Users\" WHERE id = '" + id.toString() + "'");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()){
             rs.next();
                UUID uuid = (UUID) rs.getObject("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String userPassword = rs.getString("password");

                User user = new User(uuid, firstName, lastName, email, userPassword);
                user.decryptPassword();
                return user;
        } catch (SQLException throwable) {
            throwable.printStackTrace();

        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Users\" ");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                User utilizator = new User(id, firstName, lastName, email, userPassword);
                utilizator.decryptPassword();
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User user) {
        validator.validate(user);
        String sql = "INSERT INTO \"Users\" (id, first_name, last_name , email, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            user.encryptPassword();
            ps.setString(5,user.getPassword());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public User delete(UUID id) {
        User user = findOne(id);
        if(user == null)
            return null;
        String sql = String.format("DELETE FROM \"Users\" WHERE id = '" + id.toString() + "'");
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            return user;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    public User update(User user) {
        validator.validate(user);
        if(user == null)
            return null;
        user.encryptPassword();
        String sql = String.format("UPDATE \"Users\" SET first_name = '" +
                user.getFirstName() +
                "', last_name = '" +
                user.getLastName() +
                "', email = '" +
                user.getEmail() +
                "', password = '" +
                user.getPassword() +
                "' WHERE id = '" +
                user.getId().toString() +"'");
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            return user;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }


}

