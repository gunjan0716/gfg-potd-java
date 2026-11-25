/*
 * GeeksforGeeks POTD (25 Nov 2026) - Geek's Destination with Special Jump  [Hard]
 * https://www.geeksforgeeks.org/problems/geek-and-her-destination--145724/1
 *
 * IDEA
 * ----
 * 1. The jump is never worse than walking: walking the last unit costs 1 s,
 *    a jump of j >= 1 units also costs 1 s. So for every j:
 *        answer(j) = 1 + (first time Geek's position reaches x),  x = d - j.
 *
 * 2. One cycle = forward a (a s) + backward b (b s) -> T = a+b seconds, net gain s = a-b.
 *    The highest point of cycle k is k*s + a. Geek first reaches x during the
 *    forward part of the smallest cycle k with k*s + a >= x:
 *        k(x) = max(0, ceil((x - a) / s))
 *        time = k*T + (x - k*s) = x + 2*b*k(x)
 *    => answer(j) = 1 + x + 2*b*k(x)
 *
 * 3. Sum over x in [d-r, d-l] with closed forms -> O(1) time, O(1) space.
 *    sum of ceil(y/s) for y = 1..n  = n + sum_{z=0}^{n-1} floor(z/s)
 *    sum_{z=0}^{m-1} floor(z/s)     = s*q*(q-1)/2 + q*rem,  q = m/s, rem = m%s
 */
class Solution {
    static final long MOD = 1_000_000_007L;

    // sum_{z=0}^{m-1} floor(z / s)   (mod MOD)
    private static long floorSum(long m, long s) {
        if (m <= 0) return 0;
        long q = m / s, rem = m % s;
        long tri = ((q * (q - 1)) / 2) % MOD;          // q <= ~1e9 -> fits in long
        return (tri * (s % MOD) % MOD + (q % MOD) * (rem % MOD)) % MOD;
    }

    // sum_{y=1}^{n} ceil(y / s)   (mod MOD)
    private static long ceilSum(long n, long s) {
        if (n <= 0) return 0;
        return (n % MOD + floorSum(n, s)) % MOD;
    }

    // sum of x over [lo, hi]   (mod MOD)
    private static long rangeSum(long lo, long hi) {
        long cnt = hi - lo + 1;
        long a = lo + hi, b = cnt;
        if (a % 2 == 0) a /= 2; else b /= 2;
        return (a % MOD) * (b % MOD) % MOD;
    }

    public int minTime(int a, int b, int d, int l, int r) {
        long s = (long) a - b;
        long lo = (long) d - r, hi = (long) d - l;        // range of x = d - j  (x >= 1)

        long count = hi - lo + 1;
        long total = (count % MOD + rangeSum(lo, hi)) % MOD;   // sum of (1 + x)

        // sum of k(x) for x in [lo, hi]; k(x) > 0 only when y = x - a >= 1
        long kSum = (ceilSum(hi - a, s) - ceilSum(lo - 1 - a, s) + MOD) % MOD;
        total = (total + (2L * b % MOD) * kSum) % MOD;

        return (int) total;
    }
}
