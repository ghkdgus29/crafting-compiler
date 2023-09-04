package parser.node.statement;

import parser.node.expression.Expression;

import static parser.Printer.indent;

public class Return implements Statement {

    private Expression expression;

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("RETURN:");
        expression.print(depth + 1);
    }
}
