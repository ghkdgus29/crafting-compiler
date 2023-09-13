package parser.node.expression;

import generator.Instruction;

import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class BooleanLiteral implements Expression {

    private boolean value = false;

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println((value ? "true" : "false"));
    }

    @Override
    public Object interpret() {
        return value;
    }

    @Override
    public void generate() {
        writeCode(Instruction.PushBoolean, value);
    }
}
