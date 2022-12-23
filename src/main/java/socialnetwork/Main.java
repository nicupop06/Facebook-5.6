package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import socialnetwork.controllers.LoginController;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repo.db.FriendshipDB;
import socialnetwork.repo.db.MessageDB;
import socialnetwork.repo.db.UserDB;
import socialnetwork.service.MessageService;
import socialnetwork.service.Service;
import socialnetwork.ui.UI;

import java.io.IOException;
import java.util.UUID;


public class Main extends Application  {
//    public static void main(String[] args) {
//        UI ui = UI.getInstance();
//        ui.run();



    private Service service = new Service(new UserDB("jdbc:postgresql://localhost:5432/facebook", "postgres", "postgres", new UserValidator()),
            new FriendshipDB("jdbc:postgresql://localhost:5432/facebook", "postgres", "postgres", new FriendshipValidator())
    );

    private MessageService messageService = new MessageService(new MessageDB("jdbc:postgresql://localhost:5432/facebook", "postgres", "postgres"));

    private Scene scene;
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws IOException {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
                root = loader.load();
                LoginController loginController = loader.getController();
                loginController.setService(service);
                loginController.setMessageService(messageService);
                scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();

//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chat.fxml"));
//                root = loader.load();
//                scene = new Scene(root);
//                primaryStage.setScene(scene);
//                primaryStage.setResizable(false);
//                primaryStage.show();
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }

    public static void main(String[] args) {
        launch(args);
    }





}
