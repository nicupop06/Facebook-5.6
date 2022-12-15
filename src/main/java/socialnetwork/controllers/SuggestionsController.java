package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SuggestionsController implements Initializable {

    private Service service;
    private User connectedUser;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setService(Service service) {
        this.service = service;
    }

    public void setUser(User user) {
        this.connectedUser = user;
    }

    @FXML
    private TableView<User> suggestionsTable;
    @FXML
    private TableColumn<User, String> name;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private Button backFromSuggestionsBtn;
    @FXML
    private ImageView backImg;

    @FXML
    void handleBackFromSuggestions(ActionEvent event) throws IOException {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        backImg = new ImageView(getClass().getResource("/images/back.jpg").toExternalForm());
        backImg.setFitHeight(20);
        backImg.setFitWidth(30);
        backFromSuggestionsBtn.setGraphic(backImg);
        suggestionsTable.setPlaceholder(new Label("No suggestions available."));
    }

    public void fillSuggestionsTable(){
        List<User> suggestions = new ArrayList<>();
        connectedUser.getFriends().removeAll(connectedUser.getFriends());
        service.loadFriendsWithRequests(connectedUser);


        for(User u : service.getUsers()){
            if(!connectedUser.getFriends().contains(u) && !u.getEmail().equals(connectedUser.getEmail())){
                suggestions.add(u);
            }
        }
        List<Friendship> existingRequests = service.friendList(connectedUser);
        for(Friendship f : existingRequests){
            if(suggestions.contains(service.getUserFromId(f.getId1()))){
                suggestions.remove(service.getUserFromId(f.getId1()));
            }
        }
        ObservableList<User> list = FXCollections.observableList(suggestions);

        suggestionsTable.setItems(list);
    }

    @FXML
    void handleSendFriendRequest(ActionEvent event) {
        User selectedSuggestion = suggestionsTable.getSelectionModel().getSelectedItem();
        if(selectedSuggestion != null){
            service.addFriend(connectedUser.getId(), selectedSuggestion.getId());
            suggestionsTable.getItems().remove(suggestionsTable.getSelectionModel().getSelectedItem());
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friend request sent!");
        }
        else
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Please select a suggestion!");
    }
}
