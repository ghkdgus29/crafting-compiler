package parser.node.expression;

import static parser.Printer.indent;

public class NullLiteral implements Expression {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("null");
    }
}
