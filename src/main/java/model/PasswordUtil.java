package model;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Método para hashear una contraseña
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Método para verificar una contraseña
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
