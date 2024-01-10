package mailclient.com.credentials;

public class UserCredentials {
    private static String userMail;
    private static String userPassword;
    private static String userFullName;
    private static String userUsername;

    public static void initialize(String mail, String password, String fullname, String username) {
        userMail = mail;
        userPassword = password;
        userFullName = fullname;
        userUsername = username;
    }

    public static String getUserFullName() {
        return userFullName;
    }

    public static String getUserMail() {
        return userMail;
    }

    public static String getPassword() {
        return userPassword;
    }

    public static String getUsername() {
        return userUsername;
    }

}
