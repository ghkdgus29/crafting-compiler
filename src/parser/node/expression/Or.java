package parser.node.expression;

import static interpreter.Datatype.isTrue;
import static parser.Printer.indent;

public class Or implements Expression {

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
        System.out.println("OR:");

        indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);

        indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }

    @Override
    public Object interpret() {
        return isTrue(lhs.interpret()) ? true : rhs.interpret();
    }
}
