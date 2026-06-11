package calculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/**
 * Calculator.java
 *
 * A fully-featured GUI Calculator built with Java Swing.
 *
 * Supported Operations:
 *   +  Addition
 *   -  Subtraction
 *   *  Multiplication
 *   /  Division
 *   \  Integer Division
 *   ^  Exponentiation
 *   %  Modulus
 *   C  Clear
 *  CE  Clear Entry
 *  +/- Negate
 *  sqrt Square Root
 *
 * @author  Java Calculator Project
 * @version 1.0
 */
public class Calculator extends JFrame implements ActionListener {

    // ─── Display ──────────────────────────────────────────────────────────────
    private JTextField expressionField;   // shows the running expression
    private JTextField displayField;      // shows the current number / result

    // ─── State ────────────────────────────────────────────────────────────────
    private double  operand1      = 0;
    private double  operand2      = 0;
    private String  currentOp     = "";
    private boolean newEntry      = true;   // start of a new number input
    private boolean justEvaluated = false;  // result was just displayed

    private static final DecimalFormat DF = new DecimalFormat("#.##########");

    // ─── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(18,  18,  28);
    private static final Color SURFACE     = new Color(28,  28,  42);
    private static final Color DISPLAY_BG  = new Color(10,  10,  18);
    private static final Color ACCENT      = new Color(99, 102, 241);   // indigo
    private static final Color ACCENT_DARK = new Color(67,  56, 202);
    private static final Color OP_CLR      = new Color(245, 158,  11);  // amber
    private static final Color OP_DARK     = new Color(180, 110,   5);
    private static final Color DIGIT_CLR   = new Color(55,  65,  81);
    private static final Color DIGIT_DARK  = new Color(35,  45,  60);
    private static final Color EQUAL_CLR   = new Color(16, 185, 129);   // emerald
    private static final Color EQUAL_DARK  = new Color(5,  150, 105);
    private static final Color CLEAR_CLR   = new Color(239, 68,  68);   // red
    private static final Color CLEAR_DARK  = new Color(185,  28,  28);
    private static final Color TEXT_BRIGHT = new Color(241, 245, 249);
    private static final Color TEXT_DIM    = new Color(148, 163, 184);

    // ─── Constructor ──────────────────────────────────────────────────────────
    public Calculator() {
        super("Java Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        buildDisplay();
        buildButtonPanel();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ─── Display panel ────────────────────────────────────────────────────────
    private void buildDisplay() {
        JPanel dp = new JPanel();
        dp.setLayout(new BoxLayout(dp, BoxLayout.Y_AXIS));
        dp.setBackground(DISPLAY_BG);
        dp.setBorder(new EmptyBorder(18, 20, 12, 20));

        expressionField = new JTextField("", 18);
        styleTextField(expressionField, 13f, TEXT_DIM, SwingConstants.RIGHT);

        displayField = new JTextField("0", 18);
        styleTextField(displayField, 36f, TEXT_BRIGHT, SwingConstants.RIGHT);

        dp.add(expressionField);
        dp.add(Box.createVerticalStrut(4));
        dp.add(displayField);
        dp.add(Box.createVerticalStrut(6));

        add(dp, BorderLayout.NORTH);
    }

    private void styleTextField(JTextField tf, float size, Color fg, int align) {
        tf.setEditable(false);
        tf.setHorizontalAlignment(align);
        tf.setBackground(DISPLAY_BG);
        tf.setForeground(fg);
        tf.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        tf.setFont(new Font("SansSerif", Font.BOLD, (int) size));
        tf.setOpaque(true);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, tf.getPreferredSize().height + 6));
    }

    // ─── Button grid ──────────────────────────────────────────────────────────
    private void buildButtonPanel() {
        JPanel grid = new JPanel(new GridLayout(6, 4, 8, 8));
        grid.setBackground(SURFACE);
        grid.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Row 1 — clear / utility
        addBtn(grid, "C",    CLEAR_CLR,  CLEAR_DARK,  TEXT_BRIGHT, true);
        addBtn(grid, "CE",   CLEAR_CLR,  CLEAR_DARK,  TEXT_BRIGHT, true);
        addBtn(grid, "+/-",  DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "√",    OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);

        // Row 2 — exponent / mod / int-div / div
        addBtn(grid, "^",    OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);
        addBtn(grid, "%",    OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);
        addBtn(grid, "\\",   OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);
        addBtn(grid, "/",    OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);

        // Row 3 — 7 8 9 *
        addBtn(grid, "7",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "8",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "9",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "*",    OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);

        // Row 4 — 4 5 6 -
        addBtn(grid, "4",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "5",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "6",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "-",    OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);

        // Row 5 — 1 2 3 +
        addBtn(grid, "1",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "2",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "3",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "+",    OP_CLR,     OP_DARK,     TEXT_BRIGHT, false);

        // Row 6 — 0 . = (span)
        addBtn(grid, "0",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, ".",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "⌫",    DIGIT_CLR,  DIGIT_DARK,  TEXT_BRIGHT, false);
        addBtn(grid, "=",    EQUAL_CLR,  EQUAL_DARK,  TEXT_BRIGHT, true);

        add(grid, BorderLayout.CENTER);

        // Keyboard binding
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED) handleKey(e);
                return false;
            });
    }

    private void addBtn(JPanel p, String label,
                        Color bg, Color hover, Color fg, boolean bold) {
        JButton b = new JButton(label) {
            private boolean hovered = false;
            { // init
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(fg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(70, 58));
        b.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, 18));
        b.setForeground(fg);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(this);
        p.add(b);
    }

    // ─── Event handling ───────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        process(e.getActionCommand());
    }

    private void handleKey(KeyEvent e) {
        String k = KeyEvent.getKeyText(e.getKeyCode());
        char c = e.getKeyChar();
        if (Character.isDigit(c))        process(String.valueOf(c));
        else if (c == '.')               process(".");
        else if (c == '+')               process("+");
        else if (c == '-')               process("-");
        else if (c == '*')               process("*");
        else if (c == '/')               process("/");
        else if (c == '%')               process("%");
        else if (c == '^')               process("^");
        else if (c == '\n' || c == '=')  process("=");
        else if (c == '\b')              process("⌫");
        else if (k.equals("Escape"))     process("C");
        else if (k.equals("Delete"))     process("CE");
    }

    private void process(String cmd) {
        switch (cmd) {
            // ── Digits ──
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                appendDigit(cmd);
                break;
            case ".":
                appendDot();
                break;

            // ── Operators ──
            case "+": case "-": case "*": case "/":
            case "\\": case "^": case "%":
                setOperator(cmd);
                break;

            // ── Evaluate ──
            case "=":
                evaluate();
                break;

            // ── Unary ──
            case "+/-":
                negate();
                break;
            case "√":
                squareRoot();
                break;

            // ── Editing ──
            case "⌫":
                backspace();
                break;
            case "C":
                clearAll();
                break;
            case "CE":
                clearEntry();
                break;
        }
    }

    // ─── Input helpers ────────────────────────────────────────────────────────
    private void appendDigit(String d) {
        if (newEntry || justEvaluated) {
            displayField.setText(d.equals("0") ? "0" : d);
            newEntry      = false;
            justEvaluated = false;
        } else {
            String cur = displayField.getText();
            if (cur.equals("0")) displayField.setText(d);
            else                 displayField.setText(cur + d);
        }
    }

    private void appendDot() {
        if (justEvaluated) { displayField.setText("0."); newEntry = false; justEvaluated = false; return; }
        if (newEntry)       { displayField.setText("0."); newEntry = false; return; }
        if (!displayField.getText().contains("."))
            displayField.setText(displayField.getText() + ".");
    }

    private void setOperator(String op) {
        double current = parseDisplay();

        if (!newEntry && !currentOp.isEmpty()) {
            // chain calculation
            operand2 = current;
            current  = compute(operand1, operand2, currentOp);
            if (Double.isNaN(current) || Double.isInfinite(current)) { handleError("Math Error"); return; }
            displayField.setText(DF.format(current));
        }

        operand1  = current;
        currentOp = op;
        newEntry  = true;
        justEvaluated = false;

        String displayOp = op.equals("\\") ? "÷" + "(int)" : op.equals("*") ? "×" : op;
        expressionField.setText(DF.format(operand1) + "  " + displayOp);
    }

    private void evaluate() {
        if (currentOp.isEmpty()) return;
        operand2 = parseDisplay();
        double result = compute(operand1, operand2, currentOp);
        if (Double.isNaN(result) || Double.isInfinite(result)) { handleError("Math Error"); return; }

        String displayOp = currentOp.equals("\\") ? "÷(int)" : currentOp.equals("*") ? "×" : currentOp;
        expressionField.setText(DF.format(operand1) + "  " + displayOp
                + "  " + DF.format(operand2) + "  =");
        displayField.setText(DF.format(result));
        operand1      = result;
        currentOp     = "";
        newEntry      = true;
        justEvaluated = true;
    }

    private double compute(double a, double b, String op) {
        switch (op) {
            case "+":  return a + b;
            case "-":  return a - b;
            case "*":  return a * b;
            case "/":  return (b == 0) ? Double.NaN : a / b;
            case "\\":
                if (b == 0) return Double.NaN;
                long ia = (long) a, ib = (long) b;
                return ia / ib;
            case "^":  return Math.pow(a, b);
            case "%":  return (b == 0) ? Double.NaN : a % b;
            default:   return Double.NaN;
        }
    }

    // ─── Unary operations ─────────────────────────────────────────────────────
    private void negate() {
        double v = parseDisplay();
        v = -v;
        displayField.setText(DF.format(v));
        if (justEvaluated) operand1 = v;
    }

    private void squareRoot() {
        double v = parseDisplay();
        if (v < 0) { handleError("Domain Error"); return; }
        double result = Math.sqrt(v);
        expressionField.setText("√(" + DF.format(v) + ")  =");
        displayField.setText(DF.format(result));
        operand1      = result;
        currentOp     = "";
        newEntry      = true;
        justEvaluated = true;
    }

    // ─── Clear / backspace ────────────────────────────────────────────────────
    private void clearAll() {
        operand1 = operand2 = 0;
        currentOp = "";
        newEntry  = true;
        justEvaluated = false;
        displayField.setText("0");
        expressionField.setText("");
    }

    private void clearEntry() {
        displayField.setText("0");
        newEntry = true;
    }

    private void backspace() {
        if (newEntry || justEvaluated) return;
        String t = displayField.getText();
        if (t.length() <= 1 || (t.length() == 2 && t.startsWith("-")))
            displayField.setText("0");
        else
            displayField.setText(t.substring(0, t.length() - 1));
    }

    // ─── Utility ──────────────────────────────────────────────────────────────
    private double parseDisplay() {
        try {
            return Double.parseDouble(displayField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void handleError(String msg) {
        displayField.setText(msg);
        expressionField.setText("");
        operand1 = operand2 = 0;
        currentOp = "";
        newEntry  = true;
        justEvaluated = false;
    }

    // ─── Entry point ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new Calculator();
        });
    }
}
