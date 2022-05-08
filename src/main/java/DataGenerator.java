public class DataGenerator {

    private static final String BASE = "AB]CD/EFabc^def;9900882299310;";

    public static String generateData(int length) {
        StringBuilder sb = new StringBuilder(BASE);
        while (sb.length() < length) {
            sb.append("//").append(BASE);
        }

        if (sb.length() > length) {
            return sb.substring(0, length);
        }
        return sb.toString();
    }

}
