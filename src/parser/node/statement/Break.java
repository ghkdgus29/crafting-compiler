package parser.node.statement;

import interpreter.exception.BreakException;

import static parser.Printer.indent;

public class Break implements Statement {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("BREAK");
    }

    @Override
    public void interpret() {
        throw new BreakException();
    }
}
