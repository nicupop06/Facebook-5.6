package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.validators.exceptions.FriendshipException;
import socialnetwork.domain.validators.exceptions.ValidationException;


public class FriendshipValidator implements socialnetwork.domain.validators.Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getId1() == null || entity.getId2() == null)
            throw new FriendshipException("User ID cannot be null!\n");
        if(entity.getId1().equals(entity.getId2()))
            throw new FriendshipException("User cannot be friend with himself!\n");
    }
}
