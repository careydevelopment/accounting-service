package us.careydevelopment.ecosystem.business.harness;

public class RandomStringGenerator {

    public static String generateStringOfLength(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i=0;i<length;i++) {
            sb.append("a");
        }

        return sb.toString();
    }


}
