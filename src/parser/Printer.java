package parser;

import parser.node.statement.Function;

public class Printer {

    public void printSyntaxTree(Program program) {
        for (Function node : program.getFunctions()) {
            node.print(0);
        }
    }

    public static void indent(int depth) {
        System.out.print(" ".repeat(depth * 2));
    }
}
