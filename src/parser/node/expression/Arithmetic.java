package parser.node.expression;

import interpreter.Datatype;
import scanner.Kind;

import static interpreter.Datatype.*;
import static parser.Printer.indent;

public class Arithmetic implements Expression {

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

    @Override
    public Object interpret() {
        Object lValue = lhs.interpret();
        Object rValue = rhs.interpret();

        if (kind == Kind.Add && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) + toNumber(rValue);
        }
        if (kind == Kind.Add && isString(lValue) && isString(rValue)) {
            return Datatype.toString(lValue) + Datatype.toString(rValue);
        }
        if (kind == Kind.Subtract && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) - toNumber(rValue);
        }
        if (kind == Kind.Multiply && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) * toNumber(rValue);
        }
        if (kind == Kind.Divide && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(rValue) == 0 ? "INF" : toNumber(lValue) / toNumber(rValue);
        }
        if (kind == Kind.Modulo && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(rValue) == 0 ? "NaN" : toNumber(lValue) % toNumber(rValue);
        }

        throw new RuntimeException(lValue + " " + kind.getString() + " " + rValue + ": 잘못된 계산 식입니다.");
    }
}

// 산술 연산자
// *, +, -, / 등
