import java.util.Base64;

public class Payload {

    byte[] iv;
    byte[] data;

    public Payload(byte[] iv, byte[] data) {
        this.iv = iv;
        this.data = data;
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "iv=" + Base64.getEncoder().encodeToString(iv) +
                ", data=" + Base64.getEncoder().encodeToString(data) +
                '}';
    }
}
