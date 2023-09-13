package parser.node.expression;

import generator.Instruction;
import interpreter.Datatype;
import scanner.Kind;

import java.util.Map;

import static generator.Generator.writeCode;
import static interpreter.Datatype.*;
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

    @Override
    public Object interpret() {
        Object lValue = lhs.interpret();
        Object rValue = rhs.interpret();

        if (kind == Kind.Equal && isNull(lValue) && isNull(rValue)) {
            return true;
        }
        if (kind == Kind.Equal && isBoolean(lValue) && isBoolean(rValue)) {
            return toBoolean(lValue) == toBoolean(rValue);
        }
        if (kind == Kind.Equal && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue).equals(toNumber(rValue));
        }
        if (kind == Kind.Equal && isString(lValue) && isString(rValue)) {
            return Datatype.toString(lValue).equals(Datatype.toString(rValue));
        }

        if (kind == Kind.NotEqual && (isNull(lValue) || isNull(rValue))) {
            return lValue != rValue;
        }
        if (kind == Kind.NotEqual && isBoolean(lValue) && isBoolean(rValue)) {
            return toBoolean(lValue) != toBoolean(rValue);
        }
        if (kind == Kind.NotEqual && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) != toNumber(rValue);
        }
        if (kind == Kind.NotEqual && isString(lValue) && isString(rValue)) {
            return !Datatype.toString(lValue).equals(Datatype.toString(rValue));
        }

        if (kind == Kind.LessThan && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) < toNumber(rValue);
        }
        if (kind == Kind.GreaterThan && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) > toNumber(rValue);
        }
        if (kind == Kind.LessOrEqual && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) <= toNumber(rValue);
        }
        if (kind == Kind.GreaterOrEqual && isNumber(lValue) && isNumber(rValue)) {
            return toNumber(lValue) >= toNumber(rValue);
        }

        return false;
    }

    @Override
    public void generate() {
        Map<Kind, Instruction> instructions = Map.of(
                Kind.Equal, Instruction.Equal,
                Kind.NotEqual, Instruction.NotEqual,
                Kind.LessThan, Instruction.LessThan,
                Kind.GreaterThan, Instruction.GreaterThan,
                Kind.LessOrEqual, Instruction.LessOrEqual,
                Kind.GreaterOrEqual, Instruction.GreaterOrEqual
        );

        lhs.generate();
        rhs.generate();
        writeCode(instructions.get(kind));
    }
}
// 관계 연산자
// !=, == 등
