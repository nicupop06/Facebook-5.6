package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.repo.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MessageService {

    private Repository<UUID, Message> messageRepo;

    public MessageService(Repository<UUID, Message> messageRepo) {
        this.messageRepo = messageRepo;
    }

    public ArrayList<Message> getMessages(){
        return new ArrayList<>( (Collection<Message>) messageRepo.findAll());
    }

    public void addMessage(UUID sender, UUID receiver, String text) throws Exception {
        Message message = new Message(sender, receiver, text);

        if(text != null && !text.equals("")){
            messageRepo.save(message);
        }
        else
            throw new Exception("Mesaj invalid!");

    }



    public void removeMessagesBetweenUsers(UUID user1, UUID user2){
        for(Message m: messageRepo.findAll()){
            if((m.getSentBy().equals(user1) && m.getReceivedBy().equals(user2)) || (m.getSentBy().equals(user2) && m.getReceivedBy().equals(user1)))
                messageRepo.delete(m.getId());
        }
    }

    public ArrayList<Message> getMessagesBetweenUsers(UUID user1, UUID user2){
        Collection<Message> userMessages = (Collection<Message>) messageRepo.findAll();
        return new ArrayList<>(userMessages.stream()
                .filter(
                        (Message x) -> {
                            return (x.getSentBy().equals(user1) && x.getReceivedBy().equals(user2)) || (x.getSentBy().equals(user2) && x.getReceivedBy().equals(user1));
                        }
                )
                .toList()

        );
    }


}