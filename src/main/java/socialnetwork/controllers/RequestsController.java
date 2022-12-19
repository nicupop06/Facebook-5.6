package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.service.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class RequestsController implements Initializable {

    private Service service;
    private User connectedUser;


    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TableView<Friendship> requestsTable;
    @FXML
    private TableColumn<Friendship, String> from;
    @FXML
    private TableColumn<Friendship, String> email;
    @FXML
    private TableColumn<Friendship, LocalDateTime> date;
    @FXML
    private Button backFromRequestsBtn;
    @FXML
    private ImageView backImg;

    public void setService(Service srv){
        this.service =  srv;
    }

    public void setUser(User u){
        this.connectedUser = u;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        from.setCellValueFactory(new PropertyValueFactory<Friendship, String>("id1Name"));
        email.setCellValueFactory(new PropertyValueFactory<Friendship, String>("id1Email"));
        date.setCellValueFactory(new PropertyValueFactory<Friendship, LocalDateTime>("friendsFrom"));
        backImg = new ImageView(getClass().getResource("/images/back.jpg").toExternalForm());
        backImg.setFitHeight(20);
        backImg.setFitWidth(30);
        backFromRequestsBtn.setGraphic(backImg);
        requestsTable.setPlaceholder(new Label("No requests yet."));
    }

    public void fillRequestsTable() {
        List<Friendship> requestsList = new ArrayList<>();
        for (Friendship f : service.getFriendships()) {
            if (f.getStatus().equals("requested") && f.getId2().equals(connectedUser.getId())) {
                requestsList.add(f);
            }
        }

        ObservableList<Friendship> list = FXCollections.observableList(requestsList);
        requestsTable.setItems(list);

    }

    @FXML
    void handleBackFromRequests(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/connectedUser.fxml"));
        root = loader.load();
        ConnectedUserController connectedUserController = loader.getController();
        connectedUserController.setUser(connectedUser);
        connectedUserController.setService(service);
        connectedUserController.displayConnectedUser(connectedUser.getFullName());
        connectedUserController.fillTable();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleAcceptFriendRequest(ActionEvent event) {
        Friendship selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
        if(selectedRequest != null){
            selectedRequest.setFriendsFrom(LocalDateTime.now());
            service.updateFriendship(selectedRequest);
            requestsTable.getItems().remove(requestsTable.getSelectionModel().getSelectedItem());
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friend request accepted!");
        }
        else
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Please select a request!");
    }

    @FXML
    void handleRefuseFriendRequest(ActionEvent event) {
        Friendship selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
        if(selectedRequest != null){
            service.removeFriendship(selectedRequest);
            requestsTable.getItems().remove(requestsTable.getSelectionModel().getSelectedItem());
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friend request was rejected!");
        }
        else
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Please select a request!");
    }





}