package parser.node.expression;

import java.util.ArrayList;
import java.util.List;

import static parser.Printer.indent;

public class Call implements Expression {

    private Expression sub;
    private List<Expression> arguments = new ArrayList<>();

    public void setSub(Expression sub) {
        this.sub = sub;
    }

    public void add(Expression expression) {
        arguments.add(expression);
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("CALL:");

        indent(depth + 1);
        System.out.println("EXPRESSION:");
        sub.print(depth + 2);

        for (Expression node : arguments) {
            indent(depth + 1);
            System.out.println("ARGUMENT:");
            node.print(depth + 2);
        }
    }
}
// 함수의 호출
// ex) add(1, 2)