package kz.itbc.docviewhub.security;

import org.mindrot.jbcrypt.BCrypt;


public class Security {

    public static String crypt(String password){
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(password, salt);
    }

    public static boolean decrypt(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }
}
