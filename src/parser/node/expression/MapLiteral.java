package parser.node.expression;

import generator.Instruction;

import java.util.HashMap;
import java.util.Map;

import static generator.Generator.writeCode;
import static parser.Printer.indent;

public class MapLiteral implements Expression {

    private Map<String, Expression> values = new HashMap<>();

    public void put(String key, Expression value) {
        values.put(key, value);
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("{");

        for (String key : values.keySet()) {
            System.out.print(key + ": ");
            values.get(key).print(depth + 1);
        }
        indent(depth);
        System.out.println("}");
    }

    @Override
    public Object interpret() {
        HashMap<String, Object> result = new HashMap<>();
        for (String key : values.keySet()) {
            result.put(key, values.get(key).interpret());
        }
        return result;
    }

    @Override
    public void generate() {
        for (String key : values.keySet()) {
            writeCode(Instruction.PushString, key);             // key값을 피연산자 스택에 push
            values.get(key).generate();                         // value 값 목적 코드 생성
        }
        writeCode(Instruction.PushMap, values.size());          // 피연산자 스택에서 [n] 개 쌍을 꺼내 맵을 만들고, 피연산자 스택에 다시 넣는다.
    }
}
