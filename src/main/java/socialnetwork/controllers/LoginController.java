package socialnetwork.controllers;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.service.Service;
import socialnetwork.domain.validators.exceptions.ValidationException;

import java.io.IOException;


public class LoginController {
    private Service service;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField ;

    @FXML
    private TextField newUserLastName;

    @FXML
    private TextField newUserFirstName;

    @FXML
    private TextField newUserEmail;

    @FXML
    private TextField newUserPassword;

    @FXML
    private TextField newUserConfirmPassword;
    public void setService(Service srv){
        this.service = srv;
    }
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
            String email = emailField.getText();
            String password = passwordField.getText();
            int k = 0;
            for (User u : service.getUsers()) {
                if (u.getEmail().equals(email)) {
                    k = 1;
                    if (u.getPassword().equals(password)) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/connectedUser.fxml"));



                        root = loader.load();

                        ConnectedUserController connectedUserController = loader.getController();
                        connectedUserController.setUser(u);
                        connectedUserController.setService(service);
                        connectedUserController.displayConnectedUser(u.getFullName());
                        connectedUserController.fillTable();



                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;
                    }
                    else{
                        PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Parola incorecta!");
                        break;
                    }
                }
            }
            if (k == 0)
                PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Nu exista acest cont!");

        }

        @FXML
        public void handleSignUp(ActionEvent event) throws IOException{
            String newLastName = newUserLastName.getText();
            String newFirstName = newUserFirstName.getText();
            String newEmail = newUserEmail.getText();
            String newPassword = newUserPassword.getText();
            String newConfirmPassword = newUserConfirmPassword.getText();

            if(!newPassword.equals(newConfirmPassword)){
                PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Passwords do not match!");

            }
            try{
                service.addUser(newLastName, newFirstName, newEmail, newPassword);
                PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Account created successfully!");
                newUserLastName.clear();
                newUserFirstName.clear();
                newUserEmail.clear();
                newUserPassword.clear();
                newUserConfirmPassword.clear();

            }
            catch (ValidationException e){
                PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Invalid data!");
                newUserLastName.clear();
                newUserFirstName.clear();
                newUserEmail.clear();
                newUserPassword.clear();
                newUserConfirmPassword.clear();

            }


        }
}





