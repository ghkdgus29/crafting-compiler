package parser.node.expression;

import generator.Instruction;
import interpreter.exception.ReturnException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static generator.Generator.writeCode;
import static interpreter.Datatype.*;
import static interpreter.Interpreter.local;
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

    @Override
    public Object interpret() {
        Object value = sub.interpret();
        if (isBuiltinFunction(value)) {                       // 함수가 내장함수인지 체크
            List<Object> parameters = new ArrayList<>();
            for (int i = 0; i < arguments.size(); i++) {
                parameters.add(arguments.get(i).interpret());           // 파라미터 세팅
            }
            return toBuiltinFunction(value).apply(parameters);      // 내장함수 Map 에서 내장함수를 찾아, 파라미터를 넘겨주고 실행
        }

        if (!isFunction(value)) {                                   // 존재하는 사용자 정의 함수가 아닌 경우
            return null;
        }

        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < arguments.size(); i++) {
            String name = toFunction(value).getParameterOf(i);      // 함수 노드로 만들고, 함수 노드의 파라미터명을 가져온다.
            parameters.put(name, arguments.get(i).interpret());     // 파라미터 명과 식을 매핑하여 파라미터 세팅
        }
        local.add(new ArrayList<>());                                 // 함수 프레임을 생성
        local.get(local.size() - 1).add(0, parameters);         // 함수 프레임의 맨 앞에 파라미터(매개변수) Map 추가

        try {
            toFunction(value).interpret();                          // 함수 실행
        } catch (ReturnException re) {
            local.remove(local.size() - 1);                    // 호출함수 프레임 제거
            return re.getResult();                                  // 호출한 함수의 반환값
        }

        local.remove(local.size() - 1);                     // 호출함수 프레임 제거
        return null;
    }

    @Override
    public void generate() {
        for (int i = arguments.size() - 1; i >= 0; i--) {
            arguments.get(i).generate();                        // 피연산자 스택에 넣으므로, 역순으로 인자들의 목적 코드 생성
        }
        sub.generate();                                         // 함수명 목적 코드를 생성하여 호출한 함수의 주소를 얻어온다.
        writeCode(Instruction.Call, arguments.size());          // 피연산자 스택[-1] 값 (호출한 함수 주소)으로 Jump 한다. + 피연산자 스택에서 인자 개수만큼 파라미터로 가져간다.
    }
}
// 함수의 호출
// ex) add(1, 2)