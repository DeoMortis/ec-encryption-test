import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.stream.Collectors;

public class KeyLoader {
    public static byte[] loadKeyBytesFromPEM(String keyPath) throws IOException {
        // read private key PEM format
        String encodedKey = Files.readAllLines(Path.of(keyPath))
                .stream()
                .filter(line -> !line.startsWith("--"))
                .collect(Collectors.joining());
        System.out.println("Key: " + encodedKey);
        return Base64.getDecoder().decode(encodedKey);
    }
}
