package parser.node.statement;

import static parser.Printer.indent;

public class Break implements Statement {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("BREAK");
    }
}
