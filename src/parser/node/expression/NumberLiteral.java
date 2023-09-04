package parser.node.expression;

import static parser.Printer.indent;

public class NumberLiteral implements Expression {

    private double value = 0.0;

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println(value);
    }
}
