package parser.node.expression;

import scanner.Kind;

import static parser.Printer.indent;

public class Relational implements Expression {

    private Kind kind;
    private Expression lhs;
    private Expression rhs;

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public void setLhs(Expression lhs) {
        this.lhs = lhs;
    }

    public void setRhs(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println(kind.getString() + ":");

        indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);

        indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }
}
// 관계 연산자
// !=, == 등
