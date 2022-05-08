import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.IESParameterSpec;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Encryptor {

    private final String keyPath;
    private final SecureRandom secureRandom;

    private PublicKey publicKey;

    public Encryptor(String keyPath) {
        this.keyPath = keyPath;
        secureRandom = new SecureRandom();
    }

    public Payload encrypt(String data) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            if (publicKey == null) {
                publicKey = loadKey();
            }
            byte[] nonce = new byte[16];
            secureRandom.nextBytes(nonce);
            IESParameterSpec params = new IESParameterSpec(null, null, 128, 128, nonce, true);
            Cipher cipher = Cipher.getInstance("ECIESwithAES-CBC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, params);

            return new Payload(nonce, cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public PublicKey loadKey() throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance("EC");
        // decode private key
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(KeyLoader.loadKeyBytesFromPEM(keyPath));
        return kf.generatePublic(pubSpec);
    }
}
