package automata;

import java.util.*;

/**
 * This class represents a Deterministic Finite Automaton (DFA).
 *
 * It supports:
 * - Adding states, transitions, final states, and alphabet symbols
 * - Trimming unreachable states
 * - Renaming states to readable format (S, A, B, ...)
 * - Outputting the DFA in a readable formatted way
 */
public class DFA {
    private Set<String> states;
    private Set<String> alphabet;
    private String startState;
    private Set<String> finalStates;
    private Map<String, Map<String, String>> transitions;

    /**
     * Constructs an empty DFA with initialized internal structures.
     */
    public DFA() {
        states = new LinkedHashSet<>();
        alphabet = new LinkedHashSet<>();
        finalStates = new LinkedHashSet<>();
        transitions = new LinkedHashMap<>();
    }

    /** Adds a new state to the DFA. */
    public void addState(String state) {
        states.add(state);
    }

    /** Sets the DFA's start state. Also adds the state if it doesn’t exist. */
    public void setStartState(String startState) {
        this.startState = startState;
        addState(startState);
    }

    /** Adds a final (accepting) state to the DFA. Also adds the state itself. */
    public void addFinalState(String state) {
        finalStates.add(state);
        addState(state);
    }

    /**
     * Adds a transition from one state to another for a given input symbol.
     * @param from   the current state
     * @param symbol the input symbol
     * @param to     the destination state
     */
    public void addTransition(String from, String symbol, String to) {
        addState(from);
        addState(to);
        transitions.computeIfAbsent(from, k -> new LinkedHashMap<>()).put(symbol, to);
    }

    /** @return the set of all states in the DFA */
    public Set<String> getStates() {
        return states;
    }

    /** @return the input alphabet of the DFA */
    public Set<String> getAlphabet() {
        return alphabet;
    }

    /** @return the start state of the DFA */
    public String getStartState() {
        return startState;
    }

    /** @return the set of final states */
    public Set<String> getFinalStates() {
        return finalStates;
    }

    /**
     * @return the DFA's transition function in the format:
     * Map<FromState, Map<Symbol, ToState>>
     */
    public Map<String, Map<String, String>> getTransitions() {
        return transitions;
    }

    /**
     * Trims the DFA by removing unreachable states.
     * Uses BFS from the start state to determine reachability.
     */
    public void trim() {
        Set<String> reachable = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startState);
        reachable.add(startState);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            Map<String, String> trans = transitions.getOrDefault(current, Collections.emptyMap());
            for (String symbol : trans.keySet()) {
                String next = trans.get(symbol);
                if (!reachable.contains(next)) {
                    reachable.add(next);
                    queue.add(next);
                }
            }
        }

        // Keep only reachable states and their transitions
        states.retainAll(reachable);
        finalStates.retainAll(reachable);
        transitions.keySet().retainAll(reachable);
    }

    /**
     * Renames states to simple readable names like S (start), A, B, C, etc.
     * Ensures start state is always renamed to "S".
     */
    public void renameStatesReadable() {
        Map<String, String> nameMap = new LinkedHashMap<>();

        // Start with mapping start state to "S"
        nameMap.put(startState, "S");
        char next = 'A';

        for (String state : sorted(states)) {
            if (!state.equals(startState)) {
                while (nameMap.containsValue(String.valueOf(next)) || next == 'S') {
                    next++;
                }
                nameMap.put(state, String.valueOf(next));
                next++;
            }
        }

        applyRenameMap(nameMap);
    }

    /**
     * Renames states based on a custom mapping provided by the user.
     * Useful for matching specific naming expectations in output.
     *
     * @param customNames Map of old state names → new names
     */
    public void renameStates(Map<String, String> customNames) {
        Map<String, String> nameMap = new LinkedHashMap<>();
        for (String state : sorted(states)) {
            String newName = customNames.getOrDefault(state, state);
            nameMap.put(state, newName);
        }

        applyRenameMap(nameMap);
    }

    // Applies renaming using a mapping to all DFA components
    private void applyRenameMap(Map<String, String> nameMap) {
        Set<String> newStates = new LinkedHashSet<>();
        Set<String> newFinalStates = new LinkedHashSet<>();
        Map<String, Map<String, String>> newTransitions = new LinkedHashMap<>();

        for (String oldState : states) {
            String newState = nameMap.get(oldState);
            newStates.add(newState);
            if (finalStates.contains(oldState)) {
                newFinalStates.add(newState);
            }

            Map<String, String> map = transitions.getOrDefault(oldState, Collections.emptyMap());
            for (String symbol : map.keySet()) {
                String oldDest = map.get(symbol);
                String newDest = nameMap.get(oldDest);
                newTransitions
                        .computeIfAbsent(newState, k -> new LinkedHashMap<>())
                        .put(symbol, newDest);
            }
        }

        this.states = newStates;
        this.finalStates = newFinalStates;
        this.startState = nameMap.get(startState);
        this.transitions = newTransitions;
    }

    /**
     * Prints the DFA in a clean format matching the output.txt format.
     *
     * @param testCaseId The ID of the test case (for labeling)
     */
    public void printFormatted(int testCaseId) {
        System.out.println(testCaseId + ":");
        System.out.println("# States");
        List<String> sortedStates = sorted(states);
        sortedStates.remove("S");
        sortedStates.add(0, "S"); // Start state should come first
        System.out.println(String.join(" ", sortedStates));

        System.out.println("# Alphabet");
        System.out.println(String.join(" ", sorted(alphabet)));

        System.out.println("# Start State");
        System.out.println("S");

        System.out.println("# Final States");
        System.out.println(String.join(" ", sorted(finalStates)));

        System.out.println("# Transitions");
        for (String from : sortedStates) {
            Map<String, String> map = transitions.getOrDefault(from, Collections.emptyMap());
            for (String symbol : sorted(alphabet)) {
                String to = map.get(symbol);
                if (to != null) {
                    System.out.println(from + " " + symbol + " " + to);
                }
            }
        }
        System.out.println();
    }

    /** Utility method for returning a sorted list of any set of strings. */
    private List<String> sorted(Set<String> set) {
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return list;
    }
}
