package parser.node.statement;

import generator.Instruction;
import parser.node.expression.Expression;

import static generator.Generator.*;
import static interpreter.Interpreter.local;
import static parser.Printer.indent;

public class Variable implements Statement{

    private String name;
    private Expression expression;

    public void setName(String name) {
        this.name = name;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("VAR " + name + ":");
        expression.print(depth + 1);
    }

    @Override
    public void interpret() {
        local.get(local.size() - 1).get(0).put(name, expression.interpret());
    }

    @Override
    public void generate() {
        setLocal(name);                                         // 지역 변수로 설정
        expression.generate();
        writeCode(Instruction.SetLocal, getLocal(name));        // 지역 변수의 실제 위치 설정
        writeCode(Instruction.PopOperand);                      // 대입을 위해 사용한 피연산자 스택의 피연산자 제거
    }
}
