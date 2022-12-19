package socialnetwork.repo.file;
import socialnetwork.domain.Friendship;

import socialnetwork.domain.validators.Validator;


import java.util.List;
import java.util.UUID;

public class FriendshipFile extends socialnetwork.repo.file.AbstractFileRepository<UUID, Friendship> {
    public FriendshipFile(String fileName, Validator<Friendship> validator){
        super(fileName, validator);
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return
                entity.getId() + ";" +
                        entity.getId1() + ";" +
                        entity.getId2() + ";" +
                        entity.getStatus() + ";" +
                        entity.getTime();

    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship ret = new Friendship(UUID.fromString(attributes.get(0)), UUID.fromString(attributes.get(1)), UUID.fromString(attributes.get(2)), attributes.get(3), attributes.get(4));
        return ret;
    }


}
