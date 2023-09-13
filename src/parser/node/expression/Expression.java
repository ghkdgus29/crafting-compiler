package parser.node.expression;

public interface Expression {

    void print(int depth);

    Object interpret();               // 4장 인터프리터에서 사용하기 위한 메서드

    void generate();                  // 5장 코드 생성에서 사용하기 위한 메서드
}
