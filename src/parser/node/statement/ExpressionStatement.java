package parser.node.statement;

import generator.Instruction;
import parser.node.expression.Expression;

import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class ExpressionStatement implements Statement{

    private Expression expression;

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("EXPRESSION:");
        expression.print(depth + 1);
    }

    @Override
    public void interpret() {
        expression.interpret();
    }

    @Override
    public void generate() {
        expression.generate();
        writeCode(Instruction.PopOperand);              // 피연산자 스택에 남아있는 연산결과물을 제거
    }
}
// 식을 임의로 소비시키기 위해 식을 감싸는 문 노드

// 식은 항상 결과값을 남긴다. -> 식의 결과값은 문의 목적에 따라 소비된다.
// return 1+2; 에 속한 1+2 식은 결과값 3을 반환하고 소비된다.

// 그러나 어떠한 문에도 포함되지 않고, 사용되지 않는 식이 존재할 수 있다.
// ex) 반환값이 있는 함수를 호출했지만, 반환값은 사용하지 않는 경우
// 이 경우 소비되지 않는 결과값을 임의로 소비시키기 위해 식을 감싸는 문 노드를 정의한다.
