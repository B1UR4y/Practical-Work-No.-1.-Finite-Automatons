public class FiniteAutomaton {
    static final int ALPHABET_SIZE = 2;

    // ------------------------- DFA -------------------------
    static final int DFA_START = 0;

    // DFA_DELTA[state][0] = переход по 'a'
    // DFA_DELTA[state][1] = переход по 'b'
    static final int[][] DFA_DELTA = {
            {1, 0}, // q0: a -> q1, b -> q0
            {2, 1}, // q1: a -> q2, b -> q1
            {0, 2}  // q2: a -> q0, b -> q2
    };

    static final boolean[] DFA_ACCEPT = {
            false, // q0
            false, // q1
            true   // q2
    };

    // ------------------------- NFA -------------------------
    static final int NFA_STATES = 5;
    static final int NFA_START = 0;

    // NFA_DELTA[state][symbol][nextState] == true, если есть переход
    static final boolean[][][] NFA_DELTA =
            new boolean[NFA_STATES][ALPHABET_SIZE][NFA_STATES];

    static final boolean[] NFA_ACCEPT = {
            false, // q0
            false, // q1
            true,  // q2
            true,  // q3
            true   // q4
    };

    static {
        // q0 --a--> q1
        NFA_DELTA[0][0][1] = true;

        // q1 --b--> q2
        NFA_DELTA[1][1][2] = true;

        // q2 --a--> q3
        NFA_DELTA[2][0][3] = true;

        // q2 --a--> q4
        NFA_DELTA[2][0][4] = true;

        // q3 --a--> q3
        NFA_DELTA[3][0][3] = true;

        // q4 --b--> q4
        NFA_DELTA[4][1][4] = true;
    }

    // Преобразование символа в индекс алфавита
    static int symbolIndex(char c) {
        if (c == 'a') return 0;
        if (c == 'b') return 1;
        return -1;
    }

    // ------------------------- DFA run -------------------------
    static boolean runDfa(char[] input) {
        int state = DFA_START;

        for (int i = 0; i < input.length; i++) {
            int sym = symbolIndex(input[i]);
            if (sym < 0) return false;

            state = DFA_DELTA[state][sym];
        }

        return DFA_ACCEPT[state];
    }

    // ------------------------- NFA run -------------------------
    static boolean runNfa(char[] input) {
        boolean[] current = new boolean[NFA_STATES];
        current[NFA_START] = true;

        for (int i = 0; i < input.length; i++) {
            int sym = symbolIndex(input[i]);
            if (sym < 0) return false;

            boolean[] next = new boolean[NFA_STATES];

            for (int s = 0; s < NFA_STATES; s++) {
                if (!current[s]) continue;

                for (int t = 0; t < NFA_STATES; t++) {
                    if (NFA_DELTA[s][sym][t]) {
                        next[t] = true;
                    }
                }
            }

            current = next;
        }

        for (int s = 0; s < NFA_STATES; s++) {
            if (current[s] && NFA_ACCEPT[s]) {
                return true;
            }
        }

        return false;
    }

    // Печать цепочки
    static void printChain(char[] input) {
        if (input.length == 0) {
            System.out.print("ε");
            return;
        }

        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i]);
        }
    }

    public static void main(String[] args) {
        // Тестовые цепочки для ДКА
        char[][] dfaTests = {
                new char[0],
                new char[]{'a'},
                new char[]{'a', 'a'},
                new char[]{'a', 'a', 'b'},
                new char[]{'a', 'b', 'a', 'b'},
                new char[]{'b', 'b', 'b'},
                new char[]{'a', 'a', 'a', 'a', 'a'}
        };

        // Тестовые цепочки для НКА
        char[][] nfaTests = {
                new char[0],
                new char[]{'a'},
                new char[]{'a', 'b'},
                new char[]{'a', 'b', 'a'},
                new char[]{'a', 'b', 'a', 'a'},
                new char[]{'a', 'b', 'a', 'b'},
                new char[]{'a', 'b', 'a', 'b', 'b'},
                new char[]{'a', 'b', 'a', 'b', 'a'},
                new char[]{'a', 'b', 'b'}
        };

        System.out.println("DFA: #a mod 3 = 2");
        for (int i = 0; i < dfaTests.length; i++) {
            printChain(dfaTests[i]);
            System.out.print(" -> ");
            System.out.println(runDfa(dfaTests[i]) ? "Accept" : "Reject");
        }

        System.out.println();
        System.out.println("NFA: {abab^n | n >= 0} U {aba^n | n >= 0}");
        for (int i = 0; i < nfaTests.length; i++) {
            printChain(nfaTests[i]);
            System.out.print(" -> ");
            System.out.println(runNfa(nfaTests[i]) ? "Accept" : "Reject");
        }
    }
}