import java.util.Random;

// Stress test: closed-form Solution vs. a second-by-second simulation.
public class Test {
    // Brute force: simulate the walk 1 second at a time; at every whole second try the jump.
    static long brute(int a, int b, int d, int j) {
        long pos = 0, t = 0;
        while (true) {
            for (int i = 0; i < a; i++) {          // forward phase
                if (pos + j >= d) return t + 1;     // jump now
                if (pos >= d) return t;             // reached by walking
                pos++; t++;
            }
            if (pos >= d) return t;
            for (int i = 0; i < b; i++) {          // backward phase
                if (pos + j >= d) return t + 1;
                pos--; t++;
            }
        }
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        check(sol.minTime(2, 1, 5, 3, 4), 5);
        check(sol.minTime(4, 2, 8, 2, 4), 26);

        Random rnd = new Random(42);
        for (int it = 0; it < 20000; it++) {
            int a = 2 + rnd.nextInt(9), b = 1 + rnd.nextInt(a - 1);
            int d = a + 1 + rnd.nextInt(200);
            int l = 1 + rnd.nextInt(d - 1), r = l + rnd.nextInt(d - l);
            long exp = 0;
            for (int j = l; j <= r; j++) exp += brute(a, b, d, j);
            check(sol.minTime(a, b, d, l, r), exp % 1_000_000_007L);
        }
        // big inputs: must run instantly and not overflow
        System.out.println(sol.minTime(1_000_000_000, 1, 1_000_000_000, 1, 999_999_999));
        System.out.println(sol.minTime(2, 1, 1_000_000_000, 1, 999_999_999));
        System.out.println("All tests passed");
    }

    static void check(long got, long exp) {
        if (got != exp) throw new AssertionError("got " + got + " expected " + exp);
    }
}
