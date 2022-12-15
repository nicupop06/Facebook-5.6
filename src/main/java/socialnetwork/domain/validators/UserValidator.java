package socialnetwork.domain.validators;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.exceptions.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements socialnetwork.domain.validators.Validator<User> {
    /**
     *s
     * @param namVar must not be null
     * @return true if it respects the username regex, false if not
     */
    static boolean checkLastName(String namVar)
    {

        String namRegExpVar = "[A-Z][a-z]+";

        Pattern pVar = Pattern.compile(namRegExpVar);
        Matcher mVar = pVar.matcher(namVar);
        return mVar.matches() && namVar.length() > 1 && namVar.length() < 30;

    }

    /**
     *
     * @param namVar must not be null
     * @return true if the name respects the username regex, false if not
     */
    static boolean checkFirstName(String namVar)
    {

        String namRegExpVar = "[A-Z][a-z]+(([ -][A-Z])?[a-z]+)*";

        Pattern pVar = Pattern.compile(namRegExpVar);
        Matcher mVar = pVar.matcher(namVar);
        return mVar.matches() && namVar.length() > 1 && namVar.length() < 30;
    }

    /**
     *
     * @param namVar must not be null
     * @return true if the email respects the email regex, false if not
     */
    static boolean checkEmail(String namVar)
    {

        String namRegExpVar = "[a-z0-9._]+@[a-z.]+";

        Pattern pVar = Pattern.compile(namRegExpVar);
        Matcher mVar = pVar.matcher(namVar);
        return mVar.matches() && namVar.length() > 1 && namVar.length() < 30;
    }
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMsg = "";
        if(!checkFirstName(entity.getFirstName()))
            errorMsg += "Invalid First Name \n";
        if(!checkLastName(entity.getLastName()))
            errorMsg += "Invalid Last Name \n";
        if(!checkEmail(entity.getEmail()))
            errorMsg += "Invalid Email Format \n";
        if(!errorMsg.equals(""))
            throw new ValidationException(errorMsg);

    }
}
