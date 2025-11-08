package api.example.SimpleNotes.infrastructure.encrypt;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${app.secret.key}")
    private String secretKey;
    private SecretKey key;
    private Cipher cipher;

    @PostConstruct
    public void init() throws Exception {
        byte[] keyBytes = Arrays.copyOf(secretKey.getBytes(StandardCharsets.UTF_8), 16);
        this.key = new SecretKeySpec(keyBytes, "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }

    public String encrypt(String data) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
