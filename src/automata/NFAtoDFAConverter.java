package automata;

import java.util.*;

/**
 * This class is responsible for converting a Non-deterministic Finite Automaton (NFA)
 * into a Deterministic Finite Automaton (DFA) using the standard subset construction algorithm.
 */
public class NFAtoDFAConverter {

    /**
     * Converts a given NFA into an equivalent DFA.
     *
     * @param nfa The input NFA object
     * @return A new DFA object equivalent to the given NFA
     */
    public static DFA convert(NFA nfa) {
        DFA dfa = new DFA();

        // Mapping from a set of NFA states to the corresponding DFA state name
        Map<Set<String>, String> stateNameMap = new HashMap<>();
        Queue<Set<String>> queue = new LinkedList<>();         // For BFS traversal of subsets
        Set<Set<String>> visited = new HashSet<>();            // To prevent revisiting subsets

        // Step 1: Compute the epsilon-closure of the NFA's start state
        Set<String> startSet = epsilonClosure(nfa, Set.of(nfa.getStartState()));
        String startName = nameForStateSet(startSet);
        dfa.setStartState(startName);      // Set the DFA start state
        dfa.addState(startName);           // Register the state
        stateNameMap.put(startSet, startName);
        queue.add(startSet);

        // If the start state of the DFA includes any final NFA state, mark it as final
        if (containsFinalState(startSet, nfa.getFinalStates())) {
            dfa.addFinalState(startName);
        }

        // Copy the alphabet from NFA to DFA
        dfa.getAlphabet().addAll(nfa.getAlphabet());

        // Step 2: Build DFA transitions using subset construction
        while (!queue.isEmpty()) {
            Set<String> currentSet = queue.poll();                         // Current DFA state (subset of NFA states)
            String currentName = stateNameMap.get(currentSet);             // Name of current DFA state

            // For each symbol in the alphabet, determine reachable set
            for (String symbol : nfa.getAlphabet()) {
                Set<String> nextSet = new HashSet<>();

                // For each NFA state in the current set, find its transitions
                for (String state : currentSet) {
                    Set<String> targets = nfa.getTransitions()
                            .getOrDefault(state, Map.of())
                            .getOrDefault(symbol, Set.of());

                    // Add epsilon-closure of all reachable states to next set
                    nextSet.addAll(epsilonClosure(nfa, targets));
                }

                if (nextSet.isEmpty()) continue;

                // Generate or retrieve the name for this DFA state
                String nextName = stateNameMap.computeIfAbsent(nextSet, s -> nameForStateSet(s));

                // Register this state and its transition
                dfa.addState(nextName);
                dfa.addTransition(currentName, symbol, nextName);

                // If this state hasn't been processed yet, enqueue it
                if (!visited.contains(nextSet)) {
                    queue.add(nextSet);
                    visited.add(nextSet);

                    // If this subset contains a final NFA state, it's final in the DFA too
                    if (containsFinalState(nextSet, nfa.getFinalStates())) {
                        dfa.addFinalState(nextName);
                    }
                }
            }
        }

        return dfa;
    }

    /**
     * Computes the epsilon-closure for a set of NFA states.
     * The epsilon-closure is the set of all states reachable through ε-transitions.
     *
     * @param nfa The NFA
     * @param states Set of states to compute closure for
     * @return Set of reachable states including ε-transitions
     */
    private static Set<String> epsilonClosure(NFA nfa, Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) {
            String state = stack.pop();
            for (String next : nfa.getTransitions()
                    .getOrDefault(state, Map.of())
                    .getOrDefault("ε", Set.of())) {
                if (!closure.contains(next)) {
                    closure.add(next);
                    stack.push(next);
                }
            }
        }

        return closure;
    }

    /**
     * Checks whether a set of states contains any final state from the NFA.
     *
     * @param stateSet A set of NFA states
     * @param finalStates Final states of the NFA
     * @return true if any final state is in the set, false otherwise
     */
    private static boolean containsFinalState(Set<String> stateSet, Set<String> finalStates) {
        for (String state : stateSet) {
            if (finalStates.contains(state)) return true;
        }
        return false;
    }

    /**
     * Generates a name for a DFA state created from a set of NFA states.
     * The set is sorted alphabetically to maintain consistency.
     *
     * @param states The set of NFA states
     * @return A string representation used as a DFA state name
     */
    private static String nameForStateSet(Set<String> states) {
        List<String> sorted = new ArrayList<>(states);
        Collections.sort(sorted);
        return String.join("", sorted);  // Concatenate to make a unique state name
    }
}
