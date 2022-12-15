package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class User extends socialnetwork.domain.Entity<UUID> {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<User> friends;

    public User(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.setId(UUID.randomUUID());
        friends = new ArrayList<>();
    }

    public User(UUID id,String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.setId(id);
        friends = new ArrayList<>();
    }



    public String getFullName() {
        return lastName + " " + firstName;
    }



    public List<User> getFriends() {
        return friends;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String psw){
        this.password = psw;
    }

    public void encryptPassword(){
        String encryptedPassword = "";
        char[] chars = password.toCharArray();
        for(char c : chars){
            c+=5;
            encryptedPassword+=c;
        }
        password=encryptedPassword;
    }

    public void decryptPassword(){
        String decryptedPassword = "";
        char[] chars = password.toCharArray();
        for(char c : chars){
            c-=5;
            decryptedPassword+=c;
        }
        password=decryptedPassword;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, friends);
    }

    @Override
    public String toString() {
        return
                firstName + " " +
                        lastName +
                        ", email= " + email;


    }
}
