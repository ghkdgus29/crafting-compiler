package parser.node.expression;

import generator.Instruction;

import java.util.ArrayList;
import java.util.List;

import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class ArrayLiteral implements Expression {

    private List<Expression> values = new ArrayList<>();

    public void add(Expression expression) {
        values.add(expression);
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("[");
        for (Expression node : values) {
            node.print(depth + 1);
        }
        indent(depth);
        System.out.println("]");
    }

    @Override
    public Object interpret() {
        ArrayList<Object> result = new ArrayList<>();
        for (Expression node : values) {
            result.add(node.interpret());
        }
        return result;
    }

    @Override
    public void generate() {
        for (int i = values.size() - 1; i >= 0; i--) {
            values.get(i).generate();                           // 뒤에서 부터 목적 코드 생성
        }
        writeCode(Instruction.PushArray, values.size());        // 피연산자 스택에서 [n]개를 꺼내 배열로 만들고, 피연산자 스택에 다시 넣는다.
    }
}
// 배열 리터럴
// [1, 2, 3];
