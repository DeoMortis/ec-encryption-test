import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.IESParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Decryptor {

    private final String keyPath;

    private PrivateKey privateKey;

    public Decryptor(String keyPath, String keyPassword) {
        this.keyPath = keyPath;
        try {
            privateKey = loadKey(keyPassword);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not load key", e);
        }
    }

    public String decrypt(Payload encrypted) {
        Security.addProvider(new BouncyCastleProvider());
        try {

            byte[] nonce = encrypted.getIv();
            IESParameterSpec params = new IESParameterSpec(null, null, 128, 128, nonce, true);
            Cipher cipher = Cipher.getInstance("ECIESwithAES-CBC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey, params);

            byte[] encryptedBytes = cipher.doFinal(encrypted.getData());

            return new String(encryptedBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public PrivateKey loadKey(String password) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncryptedPrivateKeyInfo pkInfo = new EncryptedPrivateKeyInfo(KeyLoader.loadKeyBytesFromPEM(keyPath));
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory pbeKeyFactory = SecretKeyFactory.getInstance(pkInfo.getAlgName());
        PKCS8EncodedKeySpec encodedKeySpec = pkInfo.getKeySpec(pbeKeyFactory.generateSecret(keySpec));

        return kf.generatePrivate(encodedKeySpec);
    }
}
