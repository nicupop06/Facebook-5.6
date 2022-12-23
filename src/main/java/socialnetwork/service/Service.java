package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.exceptions.FriendshipException;
import socialnetwork.domain.validators.exceptions.ValidationException;
import socialnetwork.repo.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Service {
    private Repository<UUID, User> userRepo;

    private Repository<UUID, Friendship> friendRepo;

    public Service(Repository<UUID, User> userRepo, Repository<UUID, Friendship> friendRepo) {
        this.userRepo = userRepo;
        this.friendRepo = friendRepo;
    }

    /**
     *
     * @return list of users from repo
     */
    public ArrayList<User> getUsers(){


        return new ArrayList<>( (Collection<User>) userRepo.findAll());

    }

    /**
     *
     * @return list of friendships from repo
     */
    public ArrayList<Friendship> getFriendships(){
        Collection<Friendship> friendships = (Collection<Friendship>) friendRepo.findAll();
        for(Friendship f : friendships){
            f.setId1Name(userRepo.findOne(f.getId1()).getFirstName());
            f.setId1Email(userRepo.findOne(f.getId1()).getEmail());
        }
        return new ArrayList<>( (Collection<Friendship>) friendships);
    }

    /**
     *
     * @param string string must not be null
     * @return new list made of users that have last name equal to string
     */
    public ArrayList<User> getUsersWithName(String string) {

        Collection<User> users = (Collection<User>) userRepo.findAll();
        return new ArrayList<>(users.stream()
                .filter(
                        (User x) -> {
                            return x.getLastName().equals(string);
                        }
                )
                .toList()
        );
    }

    /**
     *
     * @param lastName must not be null
     * @param firstName must not be null
     *                  saves user in repo
     */
    public void addUser(String lastName, String firstName, String email, String password){
        User user = new User(firstName, lastName, email, password);
        int ok=0;
        for(User u : getUsers()){
            if(u.getEmail().equals(email)){
                ok = 1;
            }
        }
        if(ok==0){
            userRepo.save(user);
        }
        else
            throw new ValidationException("Emailul este deja folosit");

    }

    public void updateUser(User newUser) {
        userRepo.update(newUser);
    }

    public void updateFriendship(Friendship f){
        friendRepo.update(f);
    }

    public void loadFriends(User user){
        Collection<Friendship> friendships = (Collection<Friendship>) friendList(user);
        for(Friendship f : friendships){
            if(f.getId1().equals(user.getId()) && f.getStatus().equals("accepted"))
                user.getFriends().add(getUserFromId(f.getId2()));
            else if(f.getId2().equals(user.getId()) && f.getStatus().equals("accepted"))
                user.getFriends().add(getUserFromId(f.getId1()));
        }
    }

    public void loadFriendsWithRequests(User user){
        Collection<Friendship> friendships = (Collection<Friendship>) friendList(user);
        for(Friendship f : friendships){
            if(f.getId1().equals(user.getId()))
                user.getFriends().add(getUserFromId(f.getId2()));
            else if(f.getId2().equals(user.getId()))
                user.getFriends().add(getUserFromId(f.getId1()));
        }
    }

    /**
     *
     * @param user user must not be null
     *             removes user from repo
     */
    public void removeUser(User user){

        UUID id = user.getId();
        removeFriendsForUser(user);
        removeFriendships(id);
        userRepo.delete(id);


    }

    /**
     *
     * @param id id must not be null
     * @return entity user from his id
     */
    public User getUserFromId(UUID id){

        return userRepo.findOne(id);
    }

    /**
     *
     * @param user user must not be null
     * removes all friends for user
     */
    private void removeFriendsForUser(User user) {

        for(User u : getUsers()){
            u.getFriends().remove(user);
        }

    }

    /**
     *
     * @param user user must not be null
     * @param user2 must not be null
     * removes user2 from user friend list
     */
    public void removeFriendForUser(User user, User user2) {

        if(user!=null && user2 !=null)
            user.getFriends().remove(user2);

    }

    /**
     *
     * @param id id must not be null
     *  removes friendships from repo
     */
    public void removeFriendships(UUID id) {

        for(Friendship u : getFriendships()){
            if(u.getId1().equals(id) || u.getId2().equals(id))
                friendRepo.delete(u.getId());
        }

    }

    public void removeExistingFriendshipBetween(UUID id1, UUID id2){
        for(Friendship u : getFriendships()){
            if((u.getId1().equals(id1) && u.getId2().equals(id2)) || (u.getId1().equals(id2) && u.getId2().equals(id1))) {
                removeFriendForUser(userRepo.findOne(id1), userRepo.findOne(id2));
                friendRepo.delete(u.getId());
            }
        }
    }

    /**
     *
     * @param f f must not be null
     * deletes the friendship from repo
     */
    public void removeFriendship(Friendship f){

        UUID id = f.getId();
        removeFriendForUser(getUserFromId(f.getId1()), getUserFromId(f.getId2()));
        removeFriendForUser(getUserFromId(f.getId2()), getUserFromId(f.getId1()));
        friendRepo.delete(id);
    }

    /**
     *
     * @param id1 id1 must not be null
     * @param id2 id2 must not be null
     *  saves the friendship in repo
     */
    public void addFriend(UUID id1, UUID id2){

        Friendship newFriendship = new Friendship(id1, id2);
        Collection<Friendship> friendships = (Collection<Friendship>) friendRepo.findAll();
        for (Friendship s : friendships) {
            if((s.getId1().equals(newFriendship.getId1())  && s.getId2().equals(newFriendship.getId2()))
            || (s.getId1().equals(newFriendship.getId2()) && s.getId2().equals(newFriendship.getId1())))
                throw new FriendshipException("These users are already friends!\n");
        }
        friendRepo.save(newFriendship);
    }

    /**
     *
     * @param id id must not be null
     * @return full name of user with id
     *
     */
    public String getFullName(UUID id){

        Collection<User> users = (Collection<User>) userRepo.findAll();
        for(User u : users){
            if(u.getId().equals(id))
                return u.getLastName() + " " + u.getFirstName();
        }
        return null;
    }

    /**
     *
     * @param user user must not be null
     * @return friend list of a given user
     */
    public ArrayList<Friendship> friendList(User user) {

        Collection<Friendship> friendships = (Collection<Friendship>) friendRepo.findAll();
        return new ArrayList<>(friendships.stream()
                .filter(
                        (Friendship x) -> {
                            return x.getId1().equals(user.getId()) || x.getId2().equals(user.getId());
                        }
                )
                .toList()

        );


    }





}

