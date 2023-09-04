package parser.node.statement;

import parser.node.expression.Expression;

import static parser.Printer.indent;

public class Variable implements Statement{

    private String name;
    private Expression expression;

    public void setName(String name) {
        this.name = name;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("VAR " + name + ":");
        expression.print(depth + 1);
    }
}
