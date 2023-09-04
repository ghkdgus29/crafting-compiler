package parser.node.expression;

import java.util.ArrayList;
import java.util.List;

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
}
// 배열 리터럴
// [1, 2, 3];
