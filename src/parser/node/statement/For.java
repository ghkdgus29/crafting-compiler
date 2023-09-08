package parser.node.statement;

import interpreter.exception.BreakException;
import interpreter.exception.ContinueException;
import parser.node.expression.Expression;

import java.util.HashMap;
import java.util.List;

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
}
