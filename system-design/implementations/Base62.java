/**
 * Base62 encoding for short-link codes, plus a reversible scramble so that
 * consecutive IDs do not produce guessable consecutive codes.
 *
 * The scramble is multiplication by an odd constant modulo 2^41, which is a
 * bijection on [0, 2^41): every ID maps to a distinct value and back, so
 * uniqueness is preserved and no collision check is needed.
 */
public class Base62 {
    private static final String ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final long MASK = (1L << 41) - 1;
    private static final long MULT = 0x2545F4914F6CDD1DL | 1L;          // odd, so invertible mod 2^41
    private static final long MULT_INV = modInverse(MULT & MASK);

    public static String encode(long n) {
        if (n == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            sb.append(ALPHABET.charAt((int) (n % 62)));
            n /= 62;
        }
        return sb.reverse().toString();
    }

    public static long decode(String s) {
        long n = 0;
        for (char c : s.toCharArray()) {
            int d = ALPHABET.indexOf(c);
            if (d < 0) throw new IllegalArgumentException("not a Base62 character: " + c);
            n = n * 62 + d;
        }
        return n;
    }

    public static long scramble(long id) {
        return (id * MULT) & MASK;
    }

    public static long unscramble(long scrambled) {
        return (scrambled * MULT_INV) & MASK;
    }

    // Newton's iteration for the inverse of an odd number modulo 2^64, then masked.
    private static long modInverse(long a) {
        long x = a;
        for (int i = 0; i < 6; i++) x *= 2 - a * x;
        return x & MASK;
    }

    public static void main(String[] args) {
        for (long id : new long[]{1, 2, 3, 1_000_000, 2_199_023_255_551L}) {
            String code = encode(scramble(id));
            long back = unscramble(decode(code));
            System.out.printf("id=%-15d code=%-8s decodes back to %d  %s%n",
                    id, code, back, back == id ? "ok" : "MISMATCH");
        }
    }
}
