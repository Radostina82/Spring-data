package gamestore.entities.user;

import gamestore.exeptions.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginDTO {
    private String email;
    private String password;

    public LoginDTO(String [] commandParts){
        this.email = commandParts[1];
        this.password=commandParts[2];
        this.validate();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    }
}
