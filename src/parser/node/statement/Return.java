package parser.node.statement;

import generator.Instruction;
import interpreter.exception.ReturnException;
import parser.node.expression.Expression;

import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class Return implements Statement {

    private Expression expression;

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("RETURN:");
        expression.print(depth + 1);
    }

    @Override
    public void interpret() {
        throw new ReturnException(expression.interpret());
    }

    @Override
    public void generate() {
        expression.generate();
        writeCode(Instruction.Return);          // 피연산자 스택에 있는 연산결과값을 반환
    }
}
