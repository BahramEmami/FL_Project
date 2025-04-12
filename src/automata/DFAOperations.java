package automata;

import java.util.*;

/**
 * This class provides static methods to perform standard operations on DFAs:
 * - complement
 * - union
 * - intersection
 *
 * It also includes a method to complete a DFA by adding a dead state.
 */
public class DFAOperations {

    /**
     * Returns the complement of a given DFA.
     * It first completes the DFA to ensure all transitions are defined,
     * then flips the set of final states.
     *
     * @param dfa The input DFA
     * @return A new DFA representing the complement language
     */
    public static DFA complement(DFA dfa) {
        DFA completeDFA = complete(dfa); // Make sure all transitions exist

        DFA result = new DFA();
        result.getAlphabet().addAll(completeDFA.getAlphabet());

        // Copy all states
        for (String state : completeDFA.getStates()) {
            result.addState(state);
        }

        result.setStartState(completeDFA.getStartState());

        // In complement DFA, final states are the ones that were not final before
        for (String state : completeDFA.getStates()) {
            if (!completeDFA.getFinalStates().contains(state)) {
                result.addFinalState(state);
            }
        }

        // Copy all transitions
        for (String from : completeDFA.getStates()) {
            for (String symbol : completeDFA.getAlphabet()) {
                String to = completeDFA.getTransitions().getOrDefault(from, Collections.emptyMap()).get(symbol);
                if (to != null) {
                    result.addTransition(from, symbol, to);
                }
            }
        }

        return result;
    }

    /**
     * Completes a DFA by ensuring that every state has a transition
     * for every symbol in the alphabet. Missing transitions are directed
     * to a special "DEAD" state that loops on itself.
     *
     * @param dfa The input (possibly incomplete) DFA
     * @return A completed DFA
     */
    public static DFA complete(DFA dfa) {
        DFA result = new DFA();
        result.getAlphabet().addAll(dfa.getAlphabet());

        String deadState = "DEAD";

        // Copy original states and add the dead state
        for (String state : dfa.getStates()) {
            result.addState(state);
        }
        result.addState(deadState);

        result.setStartState(dfa.getStartState());

        for (String finalState : dfa.getFinalStates()) {
            result.addFinalState(finalState);
        }

        // Ensure all transitions are defined
        for (String state : result.getStates()) {
            for (String symbol : result.getAlphabet()) {
                String to = dfa.getTransitions()
                        .getOrDefault(state, Collections.emptyMap())
                        .get(symbol);
                if (to == null) {
                    result.addTransition(state, symbol, deadState); // redirect to DEAD state
                } else {
                    result.addTransition(state, symbol, to);
                }
            }
        }

        // DEAD state loops to itself on all inputs
        for (String symbol : result.getAlphabet()) {
            result.addTransition(deadState, symbol, deadState);
        }

        return result;
    }

    /**
     * Performs the union of two DFAs.
     * Uses the Cartesian product construction.
     * A state in the resulting DFA is final if at least one of the corresponding states is final.
     *
     * @param d1 First DFA
     * @param d2 Second DFA
     * @return DFA representing the union of d1 and d2
     */
    public static DFA union(DFA d1, DFA d2) {
        DFA result = new DFA();

        result.getAlphabet().addAll(d1.getAlphabet());

        // Alphabet consistency check
        if (!d1.getAlphabet().equals(d2.getAlphabet())) {
            throw new IllegalArgumentException("DFAs must have the same alphabet for union");
        }

        // Generate combined states and mark final if at least one is final
        for (String q1 : d1.getStates()) {
            for (String q2 : d2.getStates()) {
                String newState = q1 + "_" + q2;
                result.addState(newState);

                if (d1.getFinalStates().contains(q1) || d2.getFinalStates().contains(q2)) {
                    result.addFinalState(newState);
                }
            }
        }

        // Start state is the combination of both start states
        result.setStartState(d1.getStartState() + "_" + d2.getStartState());

        // Define transitions for all combined states
        for (String q1 : d1.getStates()) {
            for (String q2 : d2.getStates()) {
                String from = q1 + "_" + q2;
                for (String symbol : d1.getAlphabet()) {
                    String to1 = d1.getTransitions().get(q1).get(symbol);
                    String to2 = d2.getTransitions().get(q2).get(symbol);
                    String to = to1 + "_" + to2;
                    result.addTransition(from, symbol, to);
                }
            }
        }

        return result;
    }

    /**
     * Performs the intersection of two DFAs using the Cartesian product construction.
     * A state is final only if both corresponding DFA states are final.
     *
     * @param d1 First DFA
     * @param d2 Second DFA
     * @return DFA representing the intersection of d1 and d2
     */
    public static DFA intersection(DFA d1, DFA d2) {
        DFA result = new DFA();

        result.getAlphabet().addAll(d1.getAlphabet());

        // Alphabet consistency check
        if (!d1.getAlphabet().equals(d2.getAlphabet())) {
            throw new IllegalArgumentException("DFAs must have the same alphabet for intersection");
        }

        // Generate combined states and mark final only if both are final
        for (String q1 : d1.getStates()) {
            for (String q2 : d2.getStates()) {
                String newState = q1 + "_" + q2;
                result.addState(newState);

                if (d1.getFinalStates().contains(q1) && d2.getFinalStates().contains(q2)) {
                    result.addFinalState(newState);
                }
            }
        }

        // Start state is the combination of both start states
        result.setStartState(d1.getStartState() + "_" + d2.getStartState());

        // Define transitions for all combined states
        for (String q1 : d1.getStates()) {
            for (String q2 : d2.getStates()) {
                String from = q1 + "_" + q2;
                for (String symbol : d1.getAlphabet()) {
                    String to1 = d1.getTransitions().get(q1).get(symbol);
                    String to2 = d2.getTransitions().get(q2).get(symbol);
                    String to = to1 + "_" + to2;
                    result.addTransition(from, symbol, to);
                }
            }
        }

        return result;
    }
}
