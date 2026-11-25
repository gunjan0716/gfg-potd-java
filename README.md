# Geek's Destination with Special Jump (GFG POTD · 25 Nov 2026 · Hard)

🔗 Problem: https://www.geeksforgeeks.org/problems/geek-and-her-destination--145724/1
☕ Language: Java · ⏱ Time **O(1)** · 💾 Space **O(1)**

---

## 1. The problem in one line
Geek walks in a repeating pattern of **a steps forward (a s) → b steps back (b s)**. A one-time **special jump** moves him `j` units in **1 s** and ends the trip. For every `j` in `[l, r]`, find the fastest time to reach `d`, and return the **sum** modulo 1e9+7.

---

## 2. Real-life example: the escalator commuter 🚶‍♂️

Imagine you're climbing a **broken escalator** to reach your office on floor `d`:
- You climb **a = 4** steps up (4 s), then the escalator jerks and pushes you **b = 2** steps down (2 s). Over and over.
- So every 6 seconds you gain only **2** steps. Slow!
- But a friend at the top can **pull you up j steps in 1 second** (the special jump), the moment you're within reach.

**Question:** at what time are you first within `j` steps of the top? Add 1 s for the pull, and you're done.

> Office at step d = 8, friend reaches j = 3 steps down.
> You need to stand on step 8 − 3 = **5**.
> - Cycle 0: climb to 4 (t = 4), slide back to 2 (t = 6). Never reached 5.
> - Cycle 1: climb 2 → 5 (t = 9). Reached! Pull → **t = 10** ✅ (matches the sample)

That's the whole problem: **find the first time you touch step `x = d − j`**.

---

## 3. Basic → concept, step by step

### Step 1: The jump is always worth using
Walking the last unit takes 1 s. A jump of `j ≥ 1` units also takes 1 s. So jumping is never worse:
```
answer(j) = 1 + firstTime(x),   where x = d − j
```

### Step 2: Understand one cycle
| Quantity | Value |
|---|---|
| Cycle length | `T = a + b` seconds |
| Net gain per cycle | `s = a − b` units |
| Highest point in cycle k | `k·s + a` (at the end of its forward move) |

### Step 3: Which cycle first touches x?
The smallest `k` with `k·s + a ≥ x`:
```
k(x) = max(0, ceil((x − a) / s))
```
Inside that cycle, Geek starts at `k·s` and walks forward `x − k·s` more seconds:
```
firstTime(x) = k·T + (x − k·s) = x + 2·b·k(x)
```
(Intuition: each wasted cycle costs you the **b steps back plus the b steps to recover them**, i.e. `2b`.)

### Step 4: Brute force (too slow)
Loop `j = l..r` and apply the formula: **O(r − l)**. With values up to 1e9 that's too slow, so we need sums in closed form.

### Step 5: Sum it in O(1)
```
Total = Σ (1 + x)  +  2b · Σ k(x)        for x ∈ [d−r, d−l]
```
- `Σ (1 + x)` is a simple arithmetic series.
- `Σ ceil(y/s)` for `y = 1..n` = `n + Σ_{z=0}^{n−1} floor(z/s)`
- `Σ_{z=0}^{m−1} floor(z/s) = s·q(q−1)/2 + q·rem`, where `q = m / s`, `rem = m % s`

Use prefix differences for the range, and take mod at every multiplication to avoid overflow.

---

## 4. Dry run (a = 4, b = 2, d = 8, l = 2, r = 4)
| j | x = d−j | k(x) | 1 + x + 2b·k |
|---|---|---|---|
| 2 | 6 | 1 | 1 + 6 + 4 = **11** |
| 3 | 5 | 1 | 1 + 5 + 4 = **10** |
| 4 | 4 | 0 | 1 + 4 + 0 = **5** |
| | | | **Sum = 26** ✅ |

---

## 5. Code
See [`Solution.java`](Solution.java).

## 6. Verification
[`Test.java`](Test.java) compares the O(1) formula against a second-by-second simulation on **20,000 random cases**, and runs inputs up to 1e9 instantly.
```
javac Solution.java Test.java && java Test
```

---

### Where else does this pattern show up?
- 📈 **Savings with recurring expenses:** earn `a`, spend `b` each month, a one-time bonus `j` gets you to goal `d`.
- 🔋 **Battery charging with drain:** charge `a`%, lose `b`% to a background task, fast-charge boost at the end.
- 🐌 **The classic "snail climbs a well"** puzzle is exactly this movement pattern.

**Key takeaway:** turn the simulation into a formula per query, then turn the per-query loop into a closed-form sum.
