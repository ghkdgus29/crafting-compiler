package parser.node.expression;

import generator.Instruction;

import static generator.Generator.patchAddress;
import static generator.Generator.writeCode;
import static interpreter.Datatype.isTrue;
import static parser.Printer.indent;

public class Or implements Expression {

    private Expression lhs;
    private Expression rhs;

    public void setLhs(Expression lhs) {
        this.lhs = lhs;
    }

    public void setRhs(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("OR:");

        indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);

        indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }

    @Override
    public Object interpret() {
        return isTrue(lhs.interpret()) ? true : rhs.interpret();
    }

    @Override
    public void generate() {
        lhs.generate();
        int logicalOr = writeCode(Instruction.LogicalOr);       // lhs 연산이 끝난 후, LogicalOr 명령어를 생성하여 명령어 목록에 저장
        rhs.generate();
        patchAddress(logicalOr);                                // 만약 lhs 연산이 참인 경우, rhs 연산을 skip 하는 위치로 LogicalOr 명령어의 점프주소를 설정
    }
}
