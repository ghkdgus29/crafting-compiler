package parser.node.expression;

import static parser.Printer.indent;

public class StringLiteral implements Expression {

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("\"" + value + "\"");
    }

    @Override
    public Object interpret() {
        return value;
    }
}
