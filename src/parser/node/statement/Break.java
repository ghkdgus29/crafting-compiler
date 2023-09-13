package parser.node.statement;

import generator.Instruction;
import interpreter.exception.BreakException;

import static generator.Generator.breakStack;
import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class Break implements Statement {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("BREAK");
    }

    @Override
    public void interpret() {
        throw new BreakException();
    }

    @Override
    public void generate() {
        if (breakStack.isEmpty()) {
            return;
        }
        int jumpCode = writeCode(Instruction.Jump);
        breakStack.get(breakStack.size() - 1).add(jumpCode);         // 현재 반복문의 breakStack 에 현재 jump 명령어 추가
    }
}
