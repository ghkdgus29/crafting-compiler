package parser.node.expression;

import static parser.Printer.indent;

public class GetElement implements Expression {

    private Expression sub;
    private Expression index;

    public Expression getSub() {
        return sub;
    }

    public void setSub(Expression sub) {
        this.sub = sub;
    }

    public Expression getIndex() {
        return index;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("GET_ELEMENT:");

        indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);

        indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);
    }
}
// 배열과 맵의 원소 참조
// array[0], map['property']
