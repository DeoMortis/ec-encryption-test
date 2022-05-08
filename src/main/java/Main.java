public class Main {

    /**
     *  $ openssl ecparam -name prime256v1 -genkey -noout -out private.ec.key
     *  password protect & use java supported cipher PBE-SHA1-RC4-128
     *  $ openssl pkcs8 -topk8 -in private.ec.key -v1 PBE-SHA1-RC4-128 -out private.pem
     *  without selecting supported cipher you might get `1.2.840.113549.1.5.13 SecretKeyFactory not available`
     *
     *  $ openssl ec -in private.pem -pubout -out public.pem
     */

    private static final String PUBLIC_KEY_PATH = "src/main/resources/public.pem";
    private static final String PRIVATE_KEY_PATH = "src/main/resources/private.pem";
    private static final String PASSWORD = "supersafe";

    public static void main(String[] args) {

        String data = DataGenerator.generateData(40960);
        System.out.println("Data is: " + data);
        Encryptor e = new Encryptor(PUBLIC_KEY_PATH);

        Payload encrypted = e.encrypt(data);

        System.out.println(encrypted);

        Decryptor d = new Decryptor(PRIVATE_KEY_PATH, PASSWORD);

        String decrypted = d.decrypt(encrypted);

        System.out.println("Data is: " + decrypted);
        System.out.println("Equal: " + data.equals(decrypted));
    }
}
