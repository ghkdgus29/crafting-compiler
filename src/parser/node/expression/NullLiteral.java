package parser.node.expression;

import generator.Instruction;

import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class NullLiteral implements Expression {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("null");
    }

    @Override
    public Object interpret() {
        return null;
    }

    @Override
    public void generate() {
        writeCode(Instruction.PushNull);
    }
}
