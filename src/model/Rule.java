package model;

/**
 * Represents a single production rule in a regular grammar.
 *
 * Each rule is of the form:
 *     A -> aB   or   A -> a   or   A -> ε
 * Where:
 * - left  is a non-terminal (e.g., A)
 * - right is either a terminal, a terminal+non-terminal, or ε (epsilon)
 */
public class Rule {
    private final String left;   // Left side of the rule (a non-terminal variable)
    private final String right;  // Right side of the rule (a string or ε)

    /**
     * Constructs a new Rule with the specified left and right sides.
     *
     * @param left  The left-hand side of the rule (non-terminal)
     * @param right The right-hand side (production result)
     */
    public Rule(String left, String right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the left-hand side of the rule.
     *
     * @return The non-terminal variable on the left side.
     */
    public String getLeft() {
        return left;
    }

    /**
     * Gets the right-hand side of the rule.
     *
     * @return The production value (could be terminal(s), non-terminal(s), or ε).
     */
    public String getRight() {
        return right;
    }

    /**
     * Returns a string representation of the rule in the form "A -> aB".
     *
     * @return Formatted string of the rule.
     */
    @Override
    public String toString() {
        return left + " -> " + right;
    }
}
