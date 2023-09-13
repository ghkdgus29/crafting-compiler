package parser.node.statement;

import generator.Instruction;
import parser.node.expression.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static generator.Generator.*;
import static interpreter.Datatype.isTrue;
import static interpreter.Interpreter.local;
import static parser.Printer.indent;

public class If implements Statement {

    private List<Expression> conditions = new ArrayList<>();
    private List<List<Statement>> blocks = new ArrayList<>();
    private List<Statement> elseBlock = new ArrayList<>();

    public void add(Expression condition) {
        this.conditions.add(condition);
    }

    public void add(List<Statement> block) {
        this.blocks.add(block);
    }

    public void setElseBlock(List<Statement> elseBlock) {
        this.elseBlock = elseBlock;
    }

    @Override
    public void print(int depth) {
        for (int i = 0; i < conditions.size(); i++) {
            indent(depth);
            System.out.println((i == 0 ? "IF:" : "ELIF:"));

            indent(depth + 1);
            System.out.println("CONDITION:");
            conditions.get(i).print(depth + 2);

            indent(depth + 1);
            System.out.println("BLOCK:");

            for (Statement node : blocks.get(i)) {
                node.print(depth + 2);
            }
        }

        if (elseBlock == null) {
            return;
        }

        indent(depth);
        System.out.println("ELSE:");
        for (Statement node : elseBlock) {
            node.print(depth + 1);
        }
    }

    @Override
    public void interpret() {
        for (int i = 0; i < conditions.size(); i++) {
            Object result = conditions.get(i).interpret();
            if (!isTrue(result)) {                                          // 현재 조건문이 참이 아닌 경우, skip
                continue;
            }
            local.get(local.size() - 1).add(0, new HashMap<>());
            for (Statement node : blocks.get(i)) {                           // 조건문 블록 안의 문들 실행
                node.interpret();
            }
            local.get(local.size() - 1).remove(0);
            return;
        }
        if (elseBlock.isEmpty()) {
            return;
        }
        local.get(local.size() - 1).add(0, new HashMap<>());            // else 조건에 걸리는 경우
        for (Statement node : elseBlock) {                                    // else 블록 안의 문들 실행
            node.interpret();
        }
        local.get(local.size() - 1).remove(0);
    }

    @Override
    public void generate() {
        List<Integer> jumpList = new ArrayList<>();             // 하나의 조건문 블록 수행 시, 남은 여러개의 조건문들을 건너뛰고 종료해야 한다.
                                                                // 이때 필요한 각각의 조건문 블록이 끝난 뒤 사용되는 jump 명령어들을 저장하는 리스트
        for (int i = 0; i < conditions.size(); i++) {
            conditions.get(i).generate();                                       // 조건식 목적 코드 생성
            int conditionJump = writeCode(Instruction.ConditionJump);               // 조건식이 거짓인 경우 다음 조건문으로 ConditionJump

            pushBlock();                                            // 함수 프레임 내에 블록 추가
            for (Statement node : blocks.get(i)) {
                node.generate();                                            // 조건문의 본문 목적 코드 생성
            }
            popBlock();                                             // 블록 제거

            jumpList.add(writeCode(Instruction.Jump));                      // 현재 조건문의 종료 지점에 남은 조건문들을 건너뛰기 위한 Jump 명령어 생성
            patchAddress(conditionJump);                                            // 현재 조건문의 종료 지점 바로 다음을 CoditionJump 명령어에 Jump 할 주소로 설정
        }

        if (!elseBlock.isEmpty()) {                                            // elseBlock 목적 코드 생성
            pushBlock();                                            // 함수 프레임 내에 블록 추가
            for (Statement node : elseBlock) {
                node.generate();                                            // elseBlock 본문 목적 코드 생성
            }
            popBlock();                                             // 블록 제거
        }

        for (Integer jump : jumpList) {                         // 남은 조건문들을 건너뛰기 위한 Jump 명령어들의 Jump 할 주소를 마지막 주소 바로 다음 주소로 설정
            patchAddress(jump);
        }
    }
}
