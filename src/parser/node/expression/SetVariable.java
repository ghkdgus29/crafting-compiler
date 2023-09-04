package parser.node.expression;

import static parser.Printer.indent;

public class SetVariable implements Expression {

    private String name;
    private Expression value;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("SET_VARIABLE: " + name);
        value.print(depth + 1);
    }
}
// 변수의 수정 표현
