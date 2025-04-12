package automata;

import model.Grammar;
import model.Rule;

/**
 * Converts a regular grammar into an equivalent Non-deterministic Finite Automaton (NFA).
 *
 * Each production rule of the grammar is interpreted as a transition in the NFA.
 */
public class GrammarToNFAConverter {

    /**
     * Converts the given Grammar object to an NFA object.
     *
     * The rules are interpreted as follows:
     * - A → aB     → transition from A to B on input 'a'
     * - A → a      → transition from A to final state on 'a'
     * - A → ε      → epsilon transition from A to final state
     *
     * @param grammar The regular grammar to be converted
     * @return The equivalent NFA representation
     */
    public static NFA convert(Grammar grammar) {
        NFA nfa = new NFA();

        // Add all symbols in the grammar's alphabet to the NFA
        for (String symbol : grammar.getAlphabet()) {
            nfa.addAlphabetSymbol(symbol);
        }

        // Set the start state of the NFA (same as the grammar's start variable)
        nfa.setStartState(grammar.getStartSymbol());

        // Create a special final state for accepting strings (e.g., F_G1, F_G2)
        String finalState = "F_" + grammar.getName();
        nfa.addFinalState(finalState);

        // Convert each grammar rule into an NFA transition
        for (Rule rule : grammar.getRules()) {
            String from = rule.getLeft();          // Rule's left-hand side (e.g., A)
            String right = rule.getRight();        // Rule's right-hand side (e.g., aB, ε, a)

            if (right.equals("ε")) {
                // ε-transition to final state
                nfa.addTransition(from, "ε", finalState);
            } else if (right.length() == 1) {
                // Terminal only (e.g., A → a)
                String symbol = right;
                nfa.addTransition(from, symbol, finalState);
            } else if (right.length() == 2) {
                // Terminal followed by variable (e.g., A → aB)
                String symbol = right.substring(0, 1);   // first character (terminal)
                String to = right.substring(1);          // second character (non-terminal)
                nfa.addTransition(from, symbol, to);
            }
        }

        return nfa;
    }
}
