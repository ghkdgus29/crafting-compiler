package parser.node.expression;

import generator.Instruction;

import static generator.Generator.patchAddress;
import static generator.Generator.writeCode;
import static interpreter.Datatype.isFalse;
import static parser.Printer.indent;

public class And implements Expression {

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
        System.out.println("AND:");

        indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);

        indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }

    @Override
    public Object interpret() {
        return isFalse(lhs.interpret()) ? false : rhs.interpret();
    }

    @Override
    public void generate() {
        lhs.generate();
        int logicalAnd = writeCode(Instruction.LogicalAnd);      // lhs 연산이 끝난 후, LogicalAnd 명령어를 생성하여 명령어 목록에 저장
        rhs.generate();
        patchAddress(logicalAnd);                                // 만약 lhs 연산이 거짓인 경우, rhs 연산을 skip 하는 위치로 LogicalAnd 명령어의 점프주소를 설정
    }
}
