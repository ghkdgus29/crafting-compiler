package parser.node.statement;

import static parser.Printer.indent;

public class Continue implements Statement {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("CONTINUE");
    }
}
