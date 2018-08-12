import java.security.MessageDigest;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class HashUtil {

    public static final String HASH_ALGORITHM = "SHA-256";

    public static String generateSalt() {
        byte[] salt = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return new String(Base64.encodeBase64(salt));
    }

    public static String hash(String str, String salt) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            if (StringUtils.isNotEmpty(salt)) {
                md.update(Base64.decodeBase64(salt.getBytes()));
            }
            md.update(str.getBytes("UTF-8"));
            hash = new String(Base64.encodeBase64(md.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
}
