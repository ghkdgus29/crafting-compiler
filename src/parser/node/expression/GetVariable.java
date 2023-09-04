package parser.node.expression;

import static parser.Printer.indent;

public class GetVariable implements Expression {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("GET_VARIABLE: " + name);
    }
}
// 변수의 참조 표현
