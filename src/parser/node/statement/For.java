package parser.node.statement;

import parser.node.expression.Expression;

import java.util.List;

import static parser.Printer.indent;

public class For implements Statement {

    private Variable variable;
    private Expression condition;
    private Expression expression;
    private List<Statement> block;

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setBlock(List<Statement> block) {
        this.block = block;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("FOR:");

        indent(depth + 1);
        System.out.println("VARIABLE:");
        variable.print(depth + 2);

        indent(depth + 1);
        System.out.println("CONDITION:");
        condition.print(depth + 2);

        indent(depth + 1);
        System.out.println("EXPRESSION:");
        expression.print(depth + 2);

        indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement node : block) {
            node.print(depth + 2);
        }
    }
}
