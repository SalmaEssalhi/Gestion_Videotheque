package ma.ensa.videotheque.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String motDePasse) {
        return BCrypt.hashpw(motDePasse, BCrypt.gensalt(12));
    }

    public static boolean verify(String motDePasse, String hash) {
        return BCrypt.checkpw(motDePasse, hash);
    }
}