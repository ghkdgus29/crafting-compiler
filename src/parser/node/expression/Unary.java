package parser.node.expression;

import scanner.Kind;

import static parser.Printer.indent;

public class Unary implements Expression {

    private Kind kind;
    private Expression sub;

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public void setSub(Expression sub) {
        this.sub = sub;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println(kind.getString());
        sub.print(depth + 1);
    }
}
// 단항 연산자
// + -> 절댓값
// - -> 부호 반전
