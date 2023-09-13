package parser.node.expression;

import generator.Instruction;

import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class NumberLiteral implements Expression {

    private double value = 0.0;

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println(value);
    }

    @Override
    public Object interpret() {
        return value;
    }

    @Override
    public void generate() {
        writeCode(Instruction.PushNumber, value);
    }
}
