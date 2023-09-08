package parser.node.statement;

import interpreter.exception.ContinueException;

import static parser.Printer.indent;

public class Continue implements Statement {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("CONTINUE");
    }

    @Override
    public void interpret() {
        throw new ContinueException();
    }
}
