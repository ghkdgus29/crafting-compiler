package parser.node.statement;

import generator.Instruction;
import interpreter.exception.ContinueException;

import static generator.Generator.continueStack;
import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class Continue implements Statement {

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("CONTINUE");
    }

    @Override
    public void interpret() {
        throw new ContinueException();
    }

    @Override
    public void generate() {
        if (continueStack.isEmpty()) {
            return;
        }

        int jumpCode = writeCode(Instruction.Jump);
        continueStack.get(continueStack.size() - 1).add(jumpCode);      // 현재 반복문의 continueStack 에 현재 jump 명령어 추가
    }
}
