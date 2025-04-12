package automata;

import java.util.*;

/**
 * This class represents a Non-deterministic Finite Automaton (NFA).
 *
 * An NFA consists of:
 * - a set of states
 * - an alphabet (input symbols)
 * - a start state
 * - a set of final (accepting) states
 * - a transition function (which can include ε-transitions)
 */
public class NFA {
    private Set<String> states;                                           // Set of states (Q)
    private Set<String> alphabet;                                        // Input symbols (Σ)
    private String startState;                                           // Start state (q0)
    private Set<String> finalStates;                                     // Accepting states (F)
    private Map<String, Map<String, Set<String>>> transitions;           // Transition function δ: Q × Σ → P(Q)

    /**
     * Default constructor for an empty NFA.
     * Initializes all internal sets and maps.
     */
    public NFA() {
        states = new LinkedHashSet<>();
        alphabet = new LinkedHashSet<>();
        finalStates = new LinkedHashSet<>();
        transitions = new HashMap<>();
    }

    /**
     * Adds a state to the NFA.
     *
     * @param state The name of the state (e.g., "S", "A")
     */
    public void addState(String state) {
        states.add(state);
    }

    /**
     * Sets the start state of the NFA.
     * Automatically adds it to the set of states if not already present.
     *
     * @param startState The starting state
     */
    public void setStartState(String startState) {
        this.startState = startState;
        addState(startState);
    }

    /**
     * Adds a final (accepting) state to the NFA.
     * Also ensures the state exists in the set of states.
     *
     * @param state The accepting state
     */
    public void addFinalState(String state) {
        finalStates.add(state);
        addState(state);
    }

    /**
     * Adds a symbol to the NFA's input alphabet.
     *
     * @param symbol A character or string symbol (e.g., "a", "b")
     */
    public void addAlphabetSymbol(String symbol) {
        alphabet.add(symbol);
    }

    /**
     * Adds a transition from one state to another on a given symbol.
     * Supports non-determinism: multiple transitions for the same input.
     *
     * @param from   Source state
     * @param symbol Input symbol (can be "ε" for epsilon transitions)
     * @param to     Destination state
     */
    public void addTransition(String from, String symbol, String to) {
        addState(from);
        addState(to);

        transitions
                .computeIfAbsent(from, k -> new HashMap<>())                // Ensure source state map exists
                .computeIfAbsent(symbol, k -> new LinkedHashSet<>())       // Ensure symbol map exists
                .add(to);                                                  // Add destination state
    }

    /**
     * Gets all states of the NFA.
     *
     * @return A set of state names
     */
    public Set<String> getStates() {
        return states;
    }

    /**
     * Gets the alphabet (set of input symbols).
     *
     * @return A set of strings representing the input alphabet
     */
    public Set<String> getAlphabet() {
        return alphabet;
    }

    /**
     * Returns the start state of the NFA.
     *
     * @return The name of the start state
     */
    public String getStartState() {
        return startState;
    }

    /**
     * Returns the set of final (accepting) states.
     *
     * @return A set of final state names
     */
    public Set<String> getFinalStates() {
        return finalStates;
    }

    /**
     * Returns the NFA's transition table.
     * The structure is: Map<fromState, Map<inputSymbol, Set<toStates>>>
     *
     * @return Transition table of the NFA
     */
    public Map<String, Map<String, Set<String>>> getTransitions() {
        return transitions;
    }

    /**
     * Returns a string representation of the NFA (for debugging purposes).
     *
     * @return A readable summary of the NFA structure
     */
    @Override
    public String toString() {
        return "NFA{" +
                "states=" + states +
                ", alphabet=" + alphabet +
                ", startState='" + startState + '\'' +
                ", finalStates=" + finalStates +
                ", transitions=" + transitions +
                '}';
    }
}
