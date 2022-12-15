package socialnetwork.ui;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.exceptions.FriendshipException;
import socialnetwork.domain.validators.exceptions.ValidationException;
import socialnetwork.repo.db.FriendshipDB;
import socialnetwork.repo.db.UserDB;
import socialnetwork.repo.file.FriendshipFile;
import socialnetwork.repo.file.UserFile;
import socialnetwork.service.Service;
import java.time.format.DateTimeFormatter;

import java.util.*;

public class UI {

    private static final UI instance = new UI();
    private final Service srv;
    private final Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd YYYY hh:mm:ss");

    private UI(){
        srv = new Service
                (new UserDB("jdbc:postgresql://localhost:5432/facebook", "postgres", "postgres", new UserValidator()),
                        new FriendshipDB("jdbc:postgresql://localhost:5432/facebook", "postgres", "postgres", new FriendshipValidator())
                );

//            srv = new Service
//                    (new UserFile("src/main/java/socialnetwork/Users.txt", new UserValidator()),
//                            new FriendshipFile("src/main/java/socialnetwork/Friendships.txt", new FriendshipValidator())
//
//            );
    }


    public static UI getInstance(){
        return instance;
    }
    public void run(){
        boolean ok = true;
        Integer command;
        String line;
        while(ok){
            try{
                showMenu();
                System.out.print("Command: ");
                line = scanner.nextLine();
                command = Integer.parseInt(line);
                switch (command) {
                    case 0 -> ok = false;
                    case 1 -> addUser();
                    case 2 -> updateUser();
                    case 3 -> removeUser();
                    case 4 -> addFriend();
                    case 5 -> removeFriend();
                    case 6 -> printUserList(srv.getUsers());
                    case 7 -> printFriendshipList(srv.getFriendships());
                    case 8 -> friendList();
                    default -> System.out.println("Invalid command!\n");
                }

            }
            catch (NumberFormatException e){
                System.out.println("Invalid command!\n");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }




    private void addUser() {
        String firstName;
        String lastName;
        String email;
        String password;
        System.out.println("----ADD USER----");

        System.out.print("Last name: ");
        lastName = scanner.nextLine();

        System.out.print("First name: ");
        firstName = scanner.nextLine();

        System.out.print("Email: ");
        email = scanner.nextLine();

        System.out.print("Password: ");
        password = scanner.nextLine();




        try{
            srv.addUser(lastName, firstName, email, password);
            System.out.println("User added successfully\n");
        } catch (ValidationException ve){
            System.out.println(ve.getMessage());
        }

    }

    private void updateUser(){
        String firstName;
        String lastName;
        String email;
        String password;
        System.out.println("----UPDATE USER----");

        ArrayList<User> users = srv.getUsers();

        printUserList(users);

        System.out.println("Index of the user that you want to update: ");
        String indexStr = scanner.nextLine();
        int index = Integer.parseInt(indexStr);

        if(index >= 0 && index < users.size()){

            System.out.print("New last name: ");
            lastName = scanner.nextLine();

            System.out.print("New first name: ");
            firstName = scanner.nextLine();

            System.out.print("New email: ");
            email = scanner.nextLine();

            System.out.print("New password: ");
            password = scanner.nextLine();


            User newUser = new User(firstName, lastName, email, password);
            newUser.setId(users.get(index).getId());

            try{
                srv.updateUser(newUser);
                System.out.println("User updated successfully! \n");
            }catch(ValidationException e){
                System.out.println(e.getMessage());
            }
        }
        else
            System.out.println("Index invalid! \n");



    }


    private void removeUser(){
        System.out.println("----REMOVE----");
        System.out.println("Last Name: ");
        String substring = scanner.nextLine();
        ArrayList<User> matchingUsers = srv.getUsersWithName(substring);

        printUserList(matchingUsers);
        if(!matchingUsers.isEmpty()){
            System.out.println("Index of the user that you want to remove: ");
            String indexStr = scanner.nextLine();
            int index = Integer.parseInt(indexStr);
            try{
                srv.removeUser(matchingUsers.get(index));
                System.out.println("User removed successfully!\n");
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }


        }
    }

    private void addFriend() {
        System.out.println("----ADD FRIEND----");
        printUserList(srv.getUsers());
        System.out.println("Give out 2 indexes for creating friendship: ");

        String Index1 = scanner.nextLine();
        int index1 = Integer.parseInt(Index1);

        String Index2 = scanner.nextLine();
        int index2 = Integer.parseInt(Index2);


        try{
            srv.addFriend(srv.getUsers().get(index1).getId(), srv.getUsers().get(index2).getId());

            System.out.println("Friendship was saved successfully!\n");
        } catch (FriendshipException e){
            System.out.println(e.getMessage());
        }

    }

    private void removeFriend() {
        System.out.println("----REMOVE FRIEND----");
        printUserList(srv.getUsers());
        System.out.println("Who's friend you want to delete?(index) ");

        String Index1 = scanner.nextLine();
        int index1 = Integer.parseInt(Index1);


        ArrayList<Friendship> userFriends = srv.friendList(srv.getUsers().get(index1));

        printFriendshipList(userFriends);
        System.out.println("Choose a friendship that you want to remove: (index)");

        String Index2 = scanner.nextLine();
        int index2 = Integer.parseInt(Index2);


        try {
            srv.removeFriendship(userFriends.get(index2));
            System.out.println("Friendship removed successfully!");
        } catch (FriendshipException fe){
            System.out.println(fe.getMessage());
        }


    }

    private void showMenu(){
        System.out.println("1 -> Add User");
        System.out.println("2 -> Update User");
        System.out.println("3 -> Remove User");
        System.out.println("4 -> Add Friend");
        System.out.println("5 -> Remove Friendship");
        System.out.println("6 -> Users list");
        System.out.println("7 -> Friends list");
        System.out.println("8 -> Friend list for user");
        System.out.println("0 -> Exit");

    }


    private void printUserList(List<User> list){

        if(list.isEmpty()){
            System.out.println();
            System.out.println("Empty list");
        }
        else{
            System.out.println();
            int i=0;
            for(User u : list){
                System.out.println(i + ". " + u);
                i++;
            }
            System.out.println();
        }
    }

    private void printFriendshipList(List<Friendship> list){

        if(list.isEmpty()){
            System.out.println();
            System.out.println("Empty list");
        }
        else{

            System.out.println();
            int i=0;
            for(Friendship f : list){
                UUID id1 = f.getId1();
                UUID id2 = f.getId2();
                String user1 = srv.getFullName(id1);
                String user2 = srv.getFullName(id2);
                System.out.println(i + ". " + user1 + " with " + user2 + " status " + f.getStatus() + " from " + dtf.format(f.getTime()));
                i++;
            }
            System.out.println();
        }
    }

    private void friendList() {
        printUserList(srv.getUsers());
        System.out.println("Who's friendlist you want to show?(index)");


        String Index1 = scanner.nextLine();
        int index1 = Integer.parseInt(Index1);
        User user = srv.getUsers().get(index1);
        if(user.getFriends().size() == 0)
            srv.loadFriends(user);

        System.out.println("User's friendlist: ");
        int i=1;
        var friends = user.getFriends();
        for(User u : friends) {
            System.out.println(i + ") " + u.getFullName());
            i++;
        }
        System.out.println();


    }




}
