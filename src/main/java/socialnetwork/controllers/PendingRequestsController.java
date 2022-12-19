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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class PendingRequestsController implements Initializable {

    private Service service;
    private User connectedUser;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button backFromPendingRequestBtn;
    @FXML
    private ImageView backImg;

    @FXML
    private TableView<User> pendingRequestsTable;

    @FXML
    private TableColumn<User, String> email;

    @FXML
    private TableColumn<User, String> name;

    public void setService(Service service) {
        this.service = service;
    }

    public void setUser(User user) {
        this.connectedUser = user;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        backImg = new ImageView(getClass().getResource("/images/back.jpg").toExternalForm());
        backImg.setFitHeight(20);
        backImg.setFitWidth(30);
        backFromPendingRequestBtn.setGraphic(backImg);
        pendingRequestsTable.setPlaceholder(new Label("No pending requests."));
    }

    public void fillPendingRequestsTable() {
        List<User> requestsList = new ArrayList<>();
        for (Friendship f : service.getFriendships()) {
            if (f.getStatus().equals("requested") && f.getId1().equals(connectedUser.getId())) {
                requestsList.add(service.getUserFromId(f.getId2()));
            }
        }

        ObservableList<User> list = FXCollections.observableList(requestsList);
        pendingRequestsTable.setItems(list);
    }


    @FXML
    void handleBackFromPending(ActionEvent event) throws IOException {
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
    void handleCancelRequest(ActionEvent event) {
        User selectedRequest = pendingRequestsTable.getSelectionModel().getSelectedItem();
        Collection<Friendship> friendships = (Collection<Friendship>) service.friendList(connectedUser);
        Friendship toDelete = new Friendship();
        for(Friendship f : friendships){
            if(f.getId2().equals(selectedRequest.getId()))
                toDelete = f;

        }
        if(selectedRequest != null){
            service.removeFriendship(toDelete);
            pendingRequestsTable.getItems().remove(pendingRequestsTable.getSelectionModel().getSelectedItem());
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Request canceled!");
        }
        else
            PopupMessagesController.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Please select a request!");
    }
}
