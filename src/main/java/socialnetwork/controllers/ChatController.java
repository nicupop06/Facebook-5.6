package socialnetwork.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repo.db.MessageDB;
import socialnetwork.repo.db.UserDB;
import socialnetwork.service.MessageService;
import socialnetwork.service.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private Parent root;
    private Scene scene;
    private Stage stage;
    private MessageService messageService;
    private Service service;
    private User connectedUser;
    private User chatUser;
    @FXML
    private Button sendBtn;

    @FXML
    private TextField messageField;

    @FXML
    private VBox vbox_messages;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private Label chatUserLabel;

    @FXML
    private Button backFromChatBtn;

    @FXML
    private ImageView backImg;



    public void setService(Service service) {
        this.service = service;
    }

    public void setMessageService(MessageService messageService){
        this.messageService = messageService;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public void setChatUser(User chatUser) {
        this.chatUser = chatUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        backImg = new ImageView(getClass().getResource("/images/back.jpg").toExternalForm());
        backImg.setFitHeight(20);
        backImg.setFitWidth(30);
        backFromChatBtn.setGraphic(backImg);

    }

    @FXML
    public void handleSendMessage(ActionEvent event) throws Exception {
        String messageToSend = messageField.getText();
        if(!messageToSend.isEmpty()){
            messageService.addMessage(connectedUser.getId(), chatUser.getId(), messageToSend);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            Label messageLabel = new Label();
            if(messageToSend.length() > 80)
                 messageLabel.setText(messageToSend.substring(0, 60) + "\n" + (messageToSend.substring(60)));
            else
                messageLabel.setText(messageToSend);
            messageLabel.setStyle("-fx-background-color: #30D5C8");

            hBox.getChildren().add(messageLabel);
            vbox_messages.getChildren().add(hBox);
            messageField.clear();
        }
    }

    @FXML
    public void handleBackFromChat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/connectedUser.fxml"));
        root = loader.load();
        ConnectedUserController connectedUserController = loader.getController();
        connectedUserController.setUser(connectedUser);
        connectedUserController.setMessageService(messageService);
        connectedUserController.setService(service);
        connectedUserController.displayConnectedUser(connectedUser.getFullName());
        connectedUserController.fillTable();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void fillChat(){
        List<Message> existingMessages = messageService.getMessagesBetweenUsers(connectedUser.getId(), chatUser.getId());
        Collections.sort(existingMessages);
        for(Message m : existingMessages){
            if(m.getSentBy().equals(connectedUser.getId())){
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                Label messageLabel = new Label();
                if(m.getMessageText().length() > 80)
                    messageLabel.setText(m.getMessageText().substring(0, 60) + "\n" + (m.getMessageText().substring(60)));
                else
                    messageLabel.setText(m.getMessageText());
                messageLabel.setStyle("-fx-background-color: #30D5C8");
                hBox.getChildren().add(messageLabel);
                vbox_messages.getChildren().add(hBox);
                messageField.clear();
            }
            else{
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                Label messageLabel = new Label();
                if(m.getMessageText().length() > 80)
                    messageLabel.setText(m.getMessageText().substring(0, 60) + "\n" + (m.getMessageText().substring(60)));
                else
                    messageLabel.setText(m.getMessageText());
                messageLabel.setStyle("-fx-background-color: #3bffe5");
                hBox.getChildren().add(messageLabel);
                vbox_messages.getChildren().add(hBox);
                messageField.clear();
            }
        }
    }


    public void displayChatUser(String fullName) {
        chatUserLabel.setText("Chat with " + fullName);
        chatUserLabel.setTextAlignment(TextAlignment.CENTER);
    }
}
