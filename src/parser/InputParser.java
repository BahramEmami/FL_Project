package parser;

import model.Grammar;
import model.Rule;
import model.TestCase;

import java.io.*;
import java.util.*;

/**
 * This class handles parsing of the input.txt file.
 *
 * It reads test cases, each consisting of one or more grammars and a requested operation
 * (e.g., Union, Intersection, Complement), and converts them into structured objects.
 */
public class InputParser {

    /**
     * Parses the given input file and returns a list of structured test cases.
     *
     * @param filename The path to the input file (typically "input.txt")
     * @return A list of TestCase objects extracted from the file
     * @throws IOException If the file can't be read
     */
    public static List<TestCase> parseInputFile(String filename) throws IOException {
        List<TestCase> testCases = new ArrayList<>();  // List to hold all parsed test cases
        BufferedReader reader = new BufferedReader(new FileReader(filename));  // For reading input line-by-line

        String line;
        TestCase currentTestCase = null;
        Grammar currentGrammar = null;
        boolean readingRules = false;  // Flag to indicate whether we're in # Rules section

        // Read the file line by line
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Test case ID line (e.g., "1:")
            if (line.matches("\\d+:")) {
                int id = Integer.parseInt(line.replace(":", ""));
                currentTestCase = new TestCase(id);
                testCases.add(currentTestCase);
            }

            // Grammar section start (e.g., "G1:", "G2:")
            else if (line.endsWith(":") && line.startsWith("G")) {
                currentGrammar = new Grammar(line.replace(":", ""));
                if (currentTestCase != null)
                    currentTestCase.addGrammar(currentGrammar);
            }

            // Start of metadata sections
            else if (line.startsWith("# Alphabet")) {
                readingRules = false;
            } else if (line.startsWith("# Variables")) {
                readingRules = false;
            } else if (line.startsWith("# Start")) {
                readingRules = false;
            } else if (line.startsWith("# Rules")) {
                readingRules = true;  // Start reading rules from next lines
            } else if (line.startsWith("# Operation")) {
                readingRules = false;
            }

            // Delimiter for end of a grammar block
            else if (line.equals("========")) {
                currentGrammar = null;
                readingRules = false;
            }

            // Unknown or other comments (ignored)
            else if (line.startsWith("#")) {
                readingRules = false;
            }

            // Reading grammar rules like: S -> aA
            else if (readingRules && currentGrammar != null) {
                String[] parts = line.split("->");
                if (parts.length == 2) {
                    String left = parts[0].trim();
                    String right = parts[1].trim();
                    currentGrammar.addRule(new Rule(left, right));
                }
            }

            // Reading grammar metadata: alphabet, variables, start symbol
            else if (currentGrammar != null) {
                if (currentGrammar.getAlphabet().isEmpty()) {
                    for (String s : line.split("\\s+")) {
                        currentGrammar.addAlphabetSymbol(s.trim());
                    }
                } else if (currentGrammar.getVariables().isEmpty()) {
                    for (String s : line.split("\\s+")) {
                        currentGrammar.addVariable(s.trim());
                    }
                } else if (currentGrammar.getStartSymbol() == null) {
                    currentGrammar.setStartSymbol(line.trim());
                }
            }

            // Reading the operation for the test case (e.g., "Union", "Intersection")
            else if (currentTestCase != null && currentTestCase.getOperation() == null) {
                currentTestCase.setOperation(line.trim());
            }
        }

        reader.close(); // Always close the file
        return testCases;
    }
}
