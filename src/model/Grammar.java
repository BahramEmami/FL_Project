package model;

import java.util.*;

/**
 * This class represents a Regular Grammar (RG).
 *
 * A grammar consists of:
 * - a name (e.g., G1, G2)
 * - a set of terminal symbols (alphabet)
 * - a set of non-terminal symbols (variables)
 * - a start symbol
 * - a list of production rules
 */
public class Grammar {
    private String name;                     // Grammar name (e.g., G1)
    private Set<String> alphabet;           // Set of terminal symbols (Σ)
    private Set<String> variables;          // Set of non-terminal variables (V)
    private String startSymbol;             // Start symbol (usually 'S')
    private List<Rule> rules;               // List of production rules

    /**
     * Constructs a grammar object with the specified name.
     *
     * @param name Name of the grammar (e.g., "G1", "G2")
     */
    public Grammar(String name) {
        this.name = name;
        this.alphabet = new LinkedHashSet<>();
        this.variables = new LinkedHashSet<>();
        this.rules = new ArrayList<>();
    }

    /**
     * Gets the name of the grammar.
     *
     * @return The name string (e.g., "G1")
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the set of terminal symbols (alphabet) of the grammar.
     *
     * @return Set of terminals (e.g., 'a', 'b')
     */
    public Set<String> getAlphabet() {
        return alphabet;
    }

    /**
     * Returns the set of variables (non-terminal symbols).
     *
     * @return Set of variables (e.g., 'S', 'A', 'B')
     */
    public Set<String> getVariables() {
        return variables;
    }

    /**
     * Gets the start symbol of the grammar.
     *
     * @return The start symbol as a string
     */
    public String getStartSymbol() {
        return startSymbol;
    }

    /**
     * Gets the list of production rules in the grammar.
     *
     * @return List of Rule objects
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Sets the start symbol of the grammar.
     *
     * @param startSymbol The symbol that starts derivation (usually 'S')
     */
    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    /**
     * Adds a terminal symbol to the grammar's alphabet.
     *
     * @param symbol The terminal symbol to add
     */
    public void addAlphabetSymbol(String symbol) {
        alphabet.add(symbol);
    }

    /**
     * Adds a non-terminal variable to the grammar.
     *
     * @param variable The variable to add
     */
    public void addVariable(String variable) {
        variables.add(variable);
    }

    /**
     * Adds a rule to the list of production rules.
     *
     * @param rule The Rule object to add
     */
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    /**
     * Returns a readable string representation of the grammar,
     * showing the start symbol, alphabet, variables, and rules.
     *
     * @return A formatted string summary of the grammar.
     */
    @Override
    public String toString() {
        return name + ": Start=" + startSymbol + ", Alphabet=" + alphabet + ", Variables=" + variables + ", Rules=" + rules;
    }
}
