package gamestore.entities.user;

import gamestore.exeptions.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;

    /**
     *Validate data for register a user.
     *
     * Email must be....
     * Password must be....
     *
     * commandParts[0] is skipped because it contains the command name
     * which is not needed in user object
     *
     * @param commandParts all data read from the console
     */
    public RegisterDTO(String[] commandParts){
        this.email = commandParts[1];
        this.password = commandParts[2];
        this.confirmPassword= commandParts[3];
        this.fullName = commandParts[4];

        this.validate();
    }

    private void validate() {
        int indexOfAt = email.indexOf("@");
        int indexOfDot = email.lastIndexOf(".");
        if (indexOfAt < 0 || indexOfDot < 0 || indexOfAt > indexOfDot) {
            throw new ValidationException("Email must contain @ and .");
        }
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(password);

        boolean isMatch = matcher.find();

        if (!isMatch){
            throw  new ValidationException("Password length must be at least 6 symbols and must contain at least 1 uppercase, 1 lowercase letter and 1 digit.");
        }

        if(!password.equals(confirmPassword)){
            throw new ValidationException("Password and confirm password must match!");
        }

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }
}
