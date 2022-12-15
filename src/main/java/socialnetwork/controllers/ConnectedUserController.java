package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.service.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectedUserController implements Initializable {

    private Service service;
    private User connectedUser;

    private Stage stage;
    private Scene scene;
    private Parent root;



    public void setService(Service srv){
        this.service =  srv;
    }

    public void setUser(User u){
        this.connectedUser = u;
    }
    @FXML
    private TableView<User> friendsTable;

    @FXML
    private TableColumn<User, String> name;

    @FXML
    private TableColumn<User, String> email;

    @FXML
    private Label connectedUserLabel;





    public void displayConnectedUser(String fullName){
        connectedUserLabel.setText("Connected as " + fullName);
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        friendsTable.setPlaceholder(new Label("No friends yet :( Check suggestions or requests :)"));

    }

    public void fillTable(){
        connectedUser.getFriends().removeAll(connectedUser.getFriends());
        service.loadFriends(connectedUser);
        ObservableList<User> list = FXCollections.observableList(connectedUser.getFriends());
        friendsTable.setItems(list);

    }

    @FXML
    void handleRemoveFriend(ActionEvent event) {
        User selectedUser = friendsTable.getSelectionModel().getSelectedItem();
        if(selectedUser != null){

            service.removeExistingFriendshipBetween(connectedUser.getId(), selectedUser.getId());
            friendsTable.getItems().remove(friendsTable.getSelectionModel().getSelectedItem());
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friendship was removed succesfully!");
        }
        else
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Please select a friendship!");
    }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setService(service);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void handleRequests(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/friendRequests.fxml"));
        root = loader.load();
        RequestsController requestsController = loader.getController();
        requestsController.setService(service);
        requestsController.setUser(connectedUser);
        requestsController.fillRequestsTable();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleShowSuggestions(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/friendSuggestions.fxml"));
        root = loader.load();
        SuggestionsController suggestionsController = loader.getController();
        suggestionsController.setService(service);
        suggestionsController.setUser(connectedUser);
        suggestionsController.fillSuggestionsTable();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




}

