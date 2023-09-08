package interpreter;

import interpreter.exception.BreakException;
import interpreter.exception.ContinueException;
import interpreter.exception.ReturnException;
import parser.Program;
import parser.node.statement.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    private static final String ENTRY_POINT = "main";

    public static Map<String, Object> global = new HashMap<>();
    public static List<List<Map<String, Object>>> local = new ArrayList<>();
    public static Map<String, Function> functionTable = new HashMap<>();


    public void interpret(Program program) {
        functionTable.clear();
        global.clear();
        local.clear();

        for (Function node : program.getFunctions()) {
            functionTable.put(node.getName(), node);
        }
        if (!functionTable.containsKey(ENTRY_POINT)) {          // 만약 main 함수가 없으면 바로 종료
            return;
        }

        local.add(new ArrayList<>());                                   // 함수 블록 (스택 프레임) 은 뒤로 차곡차곡 쌓인다.
        local.get(local.size() - 1).add(0, new HashMap<>());      // for, if 와 같은 문 블록들은 함수 블록 내에서 맨 앞으로 쌓인다.

        try {
            functionTable.get(ENTRY_POINT).interpret();         // main 함수부터 시작
        } catch (ReturnException | BreakException | ContinueException e) {}

        local.remove(local.size() - 1);
    }
}
