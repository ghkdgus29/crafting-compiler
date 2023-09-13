package parser.node.statement;

import generator.Instruction;
import interpreter.exception.BreakException;
import interpreter.exception.ContinueException;
import parser.node.expression.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static generator.Generator.*;
import static interpreter.Datatype.isTrue;
import static interpreter.Interpreter.local;
import static parser.Printer.indent;

public class For implements Statement {

    private Variable variable;
    private Expression condition;
    private Expression expression;
    private List<Statement> block;

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setBlock(List<Statement> block) {
        this.block = block;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("FOR:");

        indent(depth + 1);
        System.out.println("VARIABLE:");
        variable.print(depth + 2);

        indent(depth + 1);
        System.out.println("CONDITION:");
        condition.print(depth + 2);

        indent(depth + 1);
        System.out.println("EXPRESSION:");
        expression.print(depth + 2);

        indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement node : block) {
            node.print(depth + 2);
        }
    }

    @Override
    public void interpret() {
        local.get(local.size() - 1).add(0, new HashMap<>());        // 함수 프레임 내에 For문 지역 변수 블록 추가
        variable.interpret();

        while (true) {
            Object result = condition.interpret();
            if (!isTrue(result)) {                                       // 탈출 조건
                break;
            }

            try {
                for (Statement node : block) {                          // 반복문 내의 블록 실행
                    node.interpret();
                }
            } catch (ContinueException ce) {                            // Continue 문을 만난 경우

            } catch (BreakException be) {                               // Break 문을 만난 경우
                break;
            }

            expression.interpret();                                     // 증감식 실행
        }
        local.get(local.size() - 1).remove(0);                  // For 문 지역 변수 블록 제거
    }

    @Override
    public void generate() {
        breakStack.add(new ArrayList<>());              // break 를 의미하는 jump 명령어들을 저장할 스택
        continueStack.add(new ArrayList<>());           // continue 를 의미하는 jump 명령어들을 저장할 스택

        pushBlock();                             // 현재 함수 프레임에 블록 추가
        variable.generate();                            // 초기화식 목적 코드 생성

        int jumpAddress = codeList.size();                  // for 문을 반복 시 다시 되돌아올 주소 -> 조건식 시작 위치
        condition.generate();                           // 조건식 목적 코드 생성

        int conditionJump = writeCode(Instruction.ConditionJump);       // 조건식을 만족하지 않을 경우, for문 반복 탈출을 위한 conditionJump 명령어 작성
        for (Statement node : block) {
            node.generate();                            // for문 본문 블록 목적 코드 생성
        }

        int continueAddress = codeList.size();              // for문 본문 블록이 끝난 주소 -> continue 명령어가 jump 할 위치
        expression.generate();                          // 증감식 목적 코드 생성
        writeCode(Instruction.PopOperand);              // 증감식의 결과물을 피연산자 스택에서 제거

        writeCode(Instruction.Jump, jumpAddress);           // for 문 반복을 위해 다시 되돌아갈 주소로 jump

        patchAddress(conditionJump);                        // for 문 반복 종료 시, 탈출할 위치 주소를 conditionJump 명령어에 설정
        popBlock();                              // 블록 제거

        for (Integer jump : continueStack.get(continueStack.size() - 1)) {      // 모든 continue 명령어의 jump 할 위치를 continueAddress (for 문 본문 블록이 끝난 주소) 로 설정
            patchOperand(jump, continueAddress);
        }
        continueStack.remove(continueStack.size() - 1);               // for 문이 끝났으니 continue 스택 제거

        for (Integer jump : breakStack.get(breakStack.size() - 1)) {            // 모든 break 명령어의 jump 할 위치를 for 문 반복 종료 후 탈출할 위치 주소로 설정
            patchAddress(jump);
        }
        breakStack.remove(breakStack.size() - 1);                     // for 문이 끝났으니 break 스택 제거
    }
}
