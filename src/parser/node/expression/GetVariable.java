package parser.node.expression;

import generator.Instruction;
import interpreter.Interpreter;

import java.util.Map;

import static generator.Generator.getLocal;
import static generator.Generator.writeCode;
import static interpreter.BuiltInFunctionTable.builtinFunctionTable;
import static interpreter.Interpreter.global;
import static interpreter.Interpreter.local;
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

    @Override
    public Object interpret() {
        for (Map<String, Object> variables : local.get(local.size() - 1)) {     // 지역 변수에 이름이 존재한다면
            if (variables.containsKey(name)) {
                return variables.get(name);
            }
        }
        if (global.containsKey(name)) {                         // 전역 변수에 이름이 존재한다면
            return global.get(name);
        }
        if (Interpreter.functionTable.containsKey(name)) {                  // 함수 테이블에 이름이 존재한다면
            return Interpreter.functionTable.get(name);
        }
        if (builtinFunctionTable.containsKey(name)) {           // 내장 함수에 이름이 존재한다면
            return name;
        }

        return null;
    }

    @Override
    public void generate() {
        if (getLocal(name) == Integer.MAX_VALUE) {                      // 전역 볌수 참조
            writeCode(Instruction.GetGlobal, name);
        } else {
            writeCode(Instruction.GetLocal, getLocal(name));            // 지역 변수 참조
        }
    }
}
// 변수의 참조 표현
