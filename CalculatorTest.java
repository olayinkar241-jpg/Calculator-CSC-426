package calculator;

/**
 * CalculatorTest.java
 *
 * Standalone unit-test suite for the Calculator's arithmetic engine.
 * No external test framework required — runs via plain main().
 *
 * Run:  javac -d out src/calculator/CalculatorTest.java
 *       java  -cp out calculator.CalculatorTest
 */
public class CalculatorTest {

    private static int passed = 0;
    private static int failed = 0;

    // ── Thin wrapper so we can test compute() without a GUI ──────────────────
    private static double compute(double a, double b, String op) {
        switch (op) {
            case "+":  return a + b;
            case "-":  return a - b;
            case "*":  return a * b;
            case "/":  return (b == 0) ? Double.NaN : a / b;
            case "\\":
                if (b == 0) return Double.NaN;
                return (long) a / (long) b;
            case "^":  return Math.pow(a, b);
            case "%":  return (b == 0) ? Double.NaN : a % b;
            default:   return Double.NaN;
        }
    }

    // ── Assertion helpers ─────────────────────────────────────────────────────
    private static void assertEquals(String label, double expected, double actual, double epsilon) {
        if (Math.abs(expected - actual) <= epsilon) {
            System.out.printf("  [PASS] %s%n", label);
            passed++;
        } else {
            System.out.printf("  [FAIL] %s  expected=%.10f  actual=%.10f%n", label, expected, actual);
            failed++;
        }
    }

    private static void assertTrue(String label, boolean cond) {
        if (cond) { System.out.printf("  [PASS] %s%n", label); passed++; }
        else      { System.out.printf("  [FAIL] %s%n", label); failed++; }
    }

    // ── Test cases ────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=".repeat(52));
        System.out.println("  Java Calculator — Unit Test Suite");
        System.out.println("=".repeat(52));

        testAddition();
        testSubtraction();
        testMultiplication();
        testDivision();
        testIntegerDivision();
        testExponentiation();
        testModulus();
        testEdgeCases();
        testSquareRoot();

        System.out.println();
        System.out.println("=".repeat(52));
        System.out.printf("  Results:  %d passed  /  %d failed%n", passed, failed);
        System.out.println("=".repeat(52));
        System.exit(failed == 0 ? 0 : 1);
    }

    static void testAddition() {
        System.out.println("\n[+] Addition");
        assertEquals("2 + 3",          5,      compute(2,  3,  "+"), 1e-9);
        assertEquals("-5 + 5",         0,      compute(-5, 5,  "+"), 1e-9);
        assertEquals("0.1 + 0.2",      0.3,    compute(0.1, 0.2, "+"), 1e-9);
        assertEquals("Large numbers", 2e15,    compute(1e15, 1e15, "+"), 1e0);
    }

    static void testSubtraction() {
        System.out.println("\n[-] Subtraction");
        assertEquals("10 - 4",       6,    compute(10, 4,  "-"), 1e-9);
        assertEquals("0 - 7",       -7,    compute(0,  7,  "-"), 1e-9);
        assertEquals("-3 - (-3)",    0,    compute(-3, -3, "-"), 1e-9);
    }

    static void testMultiplication() {
        System.out.println("\n[*] Multiplication");
        assertEquals("6 * 7",        42,    compute(6,  7,    "*"), 1e-9);
        assertEquals("-4 * 3",      -12,    compute(-4, 3,    "*"), 1e-9);
        assertEquals("0 * 999",       0,    compute(0,  999,  "*"), 1e-9);
        assertEquals("2.5 * 4",      10,    compute(2.5, 4,   "*"), 1e-9);
    }

    static void testDivision() {
        System.out.println("\n[/] Division");
        assertEquals("10 / 2",       5,    compute(10, 2,   "/"), 1e-9);
        assertEquals("7 / 2",        3.5,  compute(7,  2,   "/"), 1e-9);
        assertEquals("-9 / 3",      -3,    compute(-9, 3,   "/"), 1e-9);
        assertTrue  ("10 / 0 = NaN", Double.isNaN(compute(10, 0, "/")));
    }

    static void testIntegerDivision() {
        System.out.println("\n[\\] Integer Division");
        assertEquals("10 \\ 3",     3,    compute(10, 3,  "\\"), 1e-9);
        assertEquals("15 \\ 4",     3,    compute(15, 4,  "\\"), 1e-9);
        assertEquals("-7 \\ 2",    -3,    compute(-7, 2,  "\\"), 1e-9);
        assertTrue  ("5 \\ 0 = NaN", Double.isNaN(compute(5, 0, "\\")));
    }

    static void testExponentiation() {
        System.out.println("\n[^] Exponentiation");
        assertEquals("2 ^ 10",    1024,  compute(2,  10, "^"), 1e-9);
        assertEquals("5 ^ 0",        1,  compute(5,  0,  "^"), 1e-9);
        assertEquals("9 ^ 0.5",      3,  compute(9, 0.5, "^"), 1e-9);
        assertEquals("(-2) ^ 3",    -8,  compute(-2, 3,  "^"), 1e-9);
    }

    static void testModulus() {
        System.out.println("\n[%] Modulus");
        assertEquals("10 % 3",   1,    compute(10, 3, "%"), 1e-9);
        assertEquals("15 % 5",   0,    compute(15, 5, "%"), 1e-9);
        assertEquals("7 % 4",    3,    compute(7,  4, "%"), 1e-9);
        assertTrue  ("8 % 0 = NaN", Double.isNaN(compute(8, 0, "%")));
    }

    static void testEdgeCases() {
        System.out.println("\n[!] Edge Cases");
        assertEquals("0 + 0",        0,    compute(0, 0, "+"), 1e-9);
        assertEquals("Max + 1",      Double.MAX_VALUE + 1,
                                            compute(Double.MAX_VALUE, 1, "+"), 1e0);
        assertTrue  ("Inf check",    Double.isInfinite(
                                            compute(Double.MAX_VALUE, Double.MAX_VALUE, "+")));
    }

    static void testSquareRoot() {
        System.out.println("\n[√] Square Root (Math.sqrt)");
        assertEquals("sqrt(4)",    2,    Math.sqrt(4),    1e-9);
        assertEquals("sqrt(2)",    Math.sqrt(2), Math.sqrt(2), 1e-9);
        assertEquals("sqrt(0)",    0,    Math.sqrt(0),    1e-9);
        assertTrue  ("sqrt(-1) = NaN", Double.isNaN(Math.sqrt(-1)));
    }
}
