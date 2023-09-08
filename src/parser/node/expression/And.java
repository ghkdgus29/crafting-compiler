package parser.node.expression;

import static interpreter.Datatype.isFalse;
import static parser.Printer.indent;

public class And implements Expression {

    private Expression lhs;
    private Expression rhs;

    public void setLhs(Expression lhs) {
        this.lhs = lhs;
    }

    public void setRhs(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("AND:");

        indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);

        indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }

    @Override
    public Object interpret() {
        return isFalse(lhs.interpret()) ? false : rhs.interpret();
    }
}
