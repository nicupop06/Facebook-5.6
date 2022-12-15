package socialnetwork.repo.file;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.util.List;
import java.util.UUID;

public class UserFile extends socialnetwork.repo.file.AbstractFileRepository<UUID, User> {

    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }


    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getLastName()+";"+entity.getFirstName()+";"+entity.getEmail()+";"+entity.getPassword();
    }
    @Override
    public User extractEntity(List<String> attributes) {

        User user = new User(UUID.fromString(attributes.get(0)), attributes.get(2), attributes.get(1), attributes.get(3), attributes.get(4));
        return user;
    }


}
