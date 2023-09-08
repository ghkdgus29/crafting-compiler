package parser.node.expression;

import java.util.Map;

import static interpreter.Interpreter.global;
import static interpreter.Interpreter.local;
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

    @Override
    public Object interpret() {
        for (Map<String, Object> variables : local.get(local.size() - 1)) {     // 지역 변수 테이블에 변수명이 존재한다면
            if (variables.containsKey(name)) {
                variables.put(name, value.interpret());
                return value.interpret();
            }
        }
        return global.put(name, value.interpret());             // 지역 변수 테이블에 변수명이 존재하지 않는다면, 전역 변수로 설정
    }
}
// 변수의 수정 표현
