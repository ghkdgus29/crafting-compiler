package parser.node.expression;

import generator.Generator;
import generator.Instruction;

import static generator.Generator.writeCode;
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

    @Override
    public void generate() {
        writeCode(Instruction.PushString, value);           // 피연산자 스택에 문자열 push
    }
}
