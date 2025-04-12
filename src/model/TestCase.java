package model;

import java.util.*;

/**
 * This class represents a single test case read from the input file.
 * A test case includes:
 * - a unique ID
 * - a list of one or more regular grammars
 * - the type of operation to perform (union, intersection, complement)
 */
public class TestCase {
    private int id;                          // Unique ID for this test case
    private List<Grammar> grammars;         // List of grammars included in the test case
    private String operation;               // Operation to perform: union, intersection, complement

    /**
     * Constructor to create a new test case with a given ID.
     * @param id The ID number of the test case (e.g., 1, 2, 3).
     */
    public TestCase(int id) {
        this.id = id;
        this.grammars = new ArrayList<>();
    }

    /**
     * Gets the test case ID.
     * @return The numeric ID of this test case.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the list of grammars for this test case.
     * @return A list of Grammar objects.
     */
    public List<Grammar> getGrammars() {
        return grammars;
    }

    /**
     * Adds a grammar to the list of grammars in this test case.
     * @param grammar The Grammar object to add.
     */
    public void addGrammar(Grammar grammar) {
        grammars.add(grammar);
    }

    /**
     * Gets the operation that should be applied on the grammars.
     * @return A string such as "Union", "Intersection", or "Complement".
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the operation to be performed for this test case.
     * @param operation The name of the operation.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Returns a string representation of the test case (for debugging).
     * @return A string summary including ID, operation, and grammars.
     */
    @Override
    public String toString() {
        return "TestCase #" + id + " Operation=" + operation + ", Grammars=" + grammars;
    }
}
