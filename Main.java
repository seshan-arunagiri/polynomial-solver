import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    // decode "encrypted" value (base conversion)
    static BigInteger decode(String val, int base) {
        return new BigInteger(val, base);
    }

    // Lagrange interpolation to get f(0)
    static BigInteger findConstant(
            List<BigInteger> xs,
            List<BigInteger> ys,
            int k) {

        BigInteger ans = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    num = num.multiply(xs.get(j).negate());
                    den = den.multiply(
                            xs.get(i).subtract(xs.get(j)));
                }
            }

            ans = ans.add(
                    ys.get(i)
                            .multiply(num)
                            .divide(den)
            );
        }
        return ans;
    }

    public static void main(String[] args) throws Exception {

        String text =
                new String(Files.readAllBytes(
                        Paths.get("input.json")));

        int k =
                Integer.parseInt(
                        text.split("\"k\"")[1]
                                .split(":")[1]
                                .split("}")[0]
                                .trim());

        List<BigInteger> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        String[] blocks =
                text.split("\"\\d+\"\\s*:");

        for (int i = 1; i <= k; i++) {

            String block = blocks[i];

            int base =
                    Integer.parseInt(
                            block.split("\"base\"")[1]
                                    .split("\"")[1]);

            String value =
                    block.split("\"value\"")[1]
                            .split("\"")[1];

            xs.add(BigInteger.valueOf(i));
            ys.add(decode(value, base));
        }

        BigInteger secret =
                findConstant(xs, ys, k);

        System.out.println("Secret = " + secret);
    }
}