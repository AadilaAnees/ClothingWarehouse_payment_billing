package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash the plain text password before saving to DB
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Compare entered password with stored hashed password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
