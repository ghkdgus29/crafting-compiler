package parser.node.expression;

import generator.Instruction;
import scanner.Kind;

import java.util.Map;

import static generator.Generator.writeCode;
import static interpreter.Datatype.isNumber;
import static interpreter.Datatype.toNumber;
import static parser.Printer.indent;

public class Unary implements Expression {

    private Kind kind;
    private Expression sub;

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public void setSub(Expression sub) {
        this.sub = sub;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println(kind.getString());
        sub.print(depth + 1);
    }

    @Override
    public Object interpret() {
        Object value = sub.interpret();
        if (kind == Kind.Add && isNumber(value)) {
            return Math.abs(toNumber(value));
        }
        if (kind == Kind.Subtract && isNumber(value)) {
            return toNumber(value) * -1;
        }
        return 0.0;
    }

    @Override
    public void generate() {
        Map<Kind, Instruction> instructions = Map.of(
                Kind.Add, Instruction.Absolute,
                Kind.Subtract, Instruction.ReverseSign
        );

        sub.generate();
        writeCode(instructions.get(kind));
    }
}
// 단항 연산자
// + -> 절댓값
// - -> 부호 반전
