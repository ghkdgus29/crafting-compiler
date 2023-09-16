package machine;

import generator.Code;
import interpreter.Datatype;
import javafx.util.Pair;

import java.util.*;

import static interpreter.BuiltInFunctionTable.builtinFunctionTable;
import static interpreter.Datatype.*;

public class Machine {

    private static Stack<StackFrame> callStack = new Stack<>();         // 스택 프레임들을 보관하는 호출 스택
    private static Map<String, Object> global = new HashMap<>();        // 전역 변수 관리
    private static Map<Object, Boolean> objects = new HashMap<>();      // 가비지 콜렉션을 위한 해쉬맵, true -> mark, false -> unmark

    public void execute(Pair<List<Code>, Map<String, Integer>> objectCode) {            // 가상 머신이 목적 코드를 읽으면서 프로그램을 실행한다.
        callStack.push(new StackFrame());

        List<Code> codeList = objectCode.getKey();
        Map<String, Integer> functionTable = objectCode.getValue();

        while (true) {
            int instructionPointer = callStack.peek().getInstructionPointer();
            Code code = codeList.get(instructionPointer);
            switch (code.getInstruction()) {
                case Exit -> {                                                      // 프로그램 종료
                    callStack.pop();
                    return;
                }

                case GetGlobal -> {                                                 // 전역 변수 참조
                    String name = (String) code.getOperand();
                    if (functionTable.containsKey(name)) {
                        pushOperand(functionTable.get(name));                           // 함수 이름인 경우, 함수 시작 주소를 피연산자에 push
                    } else if (builtinFunctionTable.containsKey(name)) {
                        pushOperand(name);                                              // 내장 함수 이름인 경우, 내장 함수 이름을 피연산자에 push
                    } else if (global.containsKey(name)) {
                        pushOperand(global.get(name));                                  // 전역 변수인 경우, 전역 변수값을 피연산자에 push
                    } else {
                        throw new RuntimeException("존재하지 않는 전역변수 참조입니다.");
                    }
                }

                case SetGlobal -> {                                                 // 전역 변수 설정
                    String name = Datatype.toString(code.getOperand());
                    global.put(name, peekOperand());                                    // 피연산자 스택에서 peek, 전역 변수 선언식을 감싸는 ExpressionStatement 문이 피연산자 스택에서 1개 pop 하기 때문에
                }

                case Call -> {                                                      // 함수 호출
                    Object operand = popOperand();                                      // 피연산자 스택에서 함수 시작 주소 pop
                    if (isSize(operand)) {                                              // 주소인 경우 -> 사용자 정의 함수
                        StackFrame stackFrame = new StackFrame();
                        stackFrame.setInstructionPointer(toSize(operand));              // 호출 함수 스택 프레임 생성 후, 명령어 포인터를 함수 시작 주소로 설정
                        for (int i = 0; i < toSize(code.getOperand()); i++) {
                            stackFrame.addVariable(popOperand());                       // 파라미터를 호출 함수 변수 공간에 넣는다.
                        }
                        callStack.push(stackFrame);                                     // 호출 스택에 호출 함수 스택 프레임을 push
                        continue;                                                       // 위에서 명령어 주소를 설정하였기 때문에, 밑에서 명령어 포인터가 1 증가하지 않도록 설정
                    }
                    if (isBuiltinFunction(operand)) {                                   // 이름인 경우 -> 내장 함수
                        List<Object> arguments = new ArrayList<>();
                        for (int i = 0; i < toSize(code.getOperand()); i++) {
                            arguments.add(popOperand());                                // 파라미터 리스트를 만든다.
                        }
                        pushOperand(toBuiltinFunction(operand).apply(arguments));       // 내장 함수를 실행한 후, 결과값을 피연산자 스택에 push
                        break;                                                          // case 문 탈출
                    }
                    pushOperand(null);                                            // 잘못된 함수 호출의 경우 null 을 피연산자 스택에 push
                }

                case Alloca -> {
                    // 현재 스택 프레임이 필요로 하는 함수 내의 지역 변수 공간의 크기는 현재 실행하는 명령어가 갖고 있다.
                    // Alloca 명령어는 함수 내의 지역 변수 공간의 크기를 보장해준다.
                    // 자바의 List 는 동적으로 크기를 증가시키므로, 지역 변수 공간의 크기를 보장해주는 로직은 특별히 필요없다.
                }

                case Print -> {                                                    // 출력
                    for (int i = 0; i < toSize(code.getOperand()); i++) {
                        Object value = popOperand();                                    // 피연산자 스택에서 꺼내서 출력
                        System.out.print(value);
                    }
                }

                case PrintLine -> System.out.println();                            // 줄바꿈 출력 명령어

                case Return -> {                                                    // 함수 결과값 반환
                    Object result = null;                               // 반환값이 없는 경우 null
                    if (!callStack.peek().isOperandStackEmpty()) {      // 반환값이 있는 경우
                        result = popOperand();
                    }
                    callStack.pop();                                // 현재 호출된 함수가 종료되었으므로, 현재 스택프레임 제거
                    pushOperand(result);                            // 피호출 스택프레임의 피연산자 스택에 result 추가
                                                                    // 반환값이 없더라도 null을 피연산자 스택에 넣어야 한다.
                                                                    // 호출함수를 감싸는 ExpressionStatement, Variable 문이 피연산자 스택에서 1개를 pop 하기 때문이다.
                    collectGarbage();
                }

                case Add -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) + toNumber(rValue));
                    } else if (isString(lValue) && isString(rValue)) {
                        pushOperand(Datatype.toString(lValue) + Datatype.toString(rValue));
                    } else {
                        throw new RuntimeException("잘못된 연산입니다.");
                    }
                }

                case Subtract -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) - toNumber(rValue));
                    } else {
                        throw new RuntimeException("잘못된 연산입니다.");
                    }
                }

                case Multiply -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) * toNumber(rValue));
                    } else {
                        throw new RuntimeException("잘못된 연산입니다.");
                    }
                }

                case Divide -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue) && toNumber(rValue) == 0) {
                        pushOperand("INF");
                    } else if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) / toNumber(rValue));
                    } else {
                        throw new RuntimeException("잘못된 연산입니다.");
                    }
                }

                case Modulo -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue) && toNumber(rValue) == 0) {
                        pushOperand("NaN");
                    } else if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) % toNumber(rValue));
                    } else {
                        throw new RuntimeException("잘못된 연산입니다.");
                    }
                }

                case Absolute -> {
                    Object value = popOperand();
                    if (isNumber(value)) {
                        pushOperand(Math.abs(toNumber(value)));
                    } else {
                        throw new RuntimeException("잘못된 연산입니다.");
                    }
                }

                case ReverseSign -> {
                    Object value = popOperand();
                    if (isNumber(value)) {
                        pushOperand(toNumber(value) * -1);
                    } else {
                        throw new RuntimeException("잘못된 연산입니다.");
                    }
                }

                case LogicalOr -> {                                                 // or 연산
                    Object value = popOperand();
                    if (isTrue(value)) {
                        pushOperand(value);                                                     // 앞에서 검사를 위해 뺀 value 값을 다시 push 하여 참/거짓 여부를 결정하는 키로 사용
                        callStack.peek().setInstructionPointer(toSize(code.getOperand()));      // 만약 왼쪽 항목이 참인 경우, 오른쪽 항목을 검사하지 않고 JUMP
                        continue;                                                               // 위에서 명령어 주소를 설정하였기 때문에, 밑에서 명령어 포인터가 1 증가하지 않도록 설정
                    }
                    // 만약 lValue 가 참이 아닌 경우, rValue 값이 피연산자 스택에 남아 참/거짓 여부를 결정하는 키가 된다.
                }
                case LogicalAnd -> {                                                // and 연산
                    Object value = popOperand();
                    if (isFalse(value)) {
                        pushOperand(value);                                                     // 앞에서 검사를 위해 뺀 value 값을 다시 push 하여 참/거짓 여부를 결정하는 키로 사용
                        callStack.peek().setInstructionPointer(toSize(code.getOperand()));      // 만약 왼쪽 항목이 거짓인 경우, 오른쪽 항목을 검사하지 않고 JUMP
                        continue;                                                               // 위에서 명령어 주소를 설정하였기 때문에, 밑에서 명령어 포인터가 1 증가하지 않도록 설정
                    }
                    // 만약 lValue 가 거짓이 아닌 경우, rValue 값이 피연산자 스택에 남아 참/거짓 여부를 결정하는 키가 된다.
                }

                case Equal -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNull(lValue) && isNull(rValue)) {
                        pushOperand(true);
                    } else if (isBoolean(lValue) && isBoolean(rValue)) {
                        pushOperand(toBoolean(lValue) == toBoolean(rValue));
                    } else if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue).equals(toNumber(rValue)));
                    } else if (isString(lValue) && isString(rValue)) {
                        pushOperand(Datatype.toString(lValue).equals(Datatype.toString(rValue)));
                    } else {
                        throw new RuntimeException("잘못된 비교입니다.");
                    }
                }

                case NotEqual -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNull(lValue) && isNull(rValue)) {
                        pushOperand(false);
                    } else if (isNull(lValue) || isNull(rValue)) {
                        pushOperand(true);
                    } else if (isBoolean(lValue) && isBoolean(rValue)) {
                        pushOperand(toBoolean(lValue) != toBoolean(rValue));
                    } else if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(!toNumber(lValue).equals(toNumber(rValue)));
                    } else if (isString(lValue) && isString(rValue)) {
                        pushOperand(!Datatype.toString(lValue).equals(Datatype.toString(rValue)));
                    } else {
                        throw new RuntimeException("잘못된 비교입니다.");
                    }
                }

                case LessThan -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) < (toNumber(rValue)));
                    } else {
                        throw new RuntimeException("잘못된 비교입니다.");
                    }
                }

                case GreaterThan -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) > (toNumber(rValue)));
                    } else {
                        throw new RuntimeException("잘못된 비교입니다.");
                    }
                }

                case LessOrEqual -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) <= (toNumber(rValue)));
                    } else {
                        throw new RuntimeException("잘못된 비교입니다.");
                    }
                }

                case GreaterOrEqual -> {
                    Object rValue = popOperand();
                    Object lValue = popOperand();
                    if (isNumber(lValue) && isNumber(rValue)) {
                        pushOperand(toNumber(lValue) >= (toNumber(rValue)));
                    } else {
                        throw new RuntimeException("잘못된 비교입니다.");
                    }
                }

                case GetLocal -> {                                                      // 지역 변수 참조
                    int index = toSize(code.getOperand());
                    pushOperand(callStack.peek().getVariableAt(index));                     // 명령어에 입력된 index 를 사용해, 함수 내의 변수 공간의 index 번째 변수값을 피연산자 스택에 push
                }

                case SetLocal -> {                                                      // 지역 변수 변경
                    int index = toSize(code.getOperand());
                    callStack.peek().changeValueAt(index, peekOperand());                   // 명령어에 입력된 index 를 사용해, 함수 내의 변수 공간의 index 번째 변수값을 피연산자 스택에 peek 값으로 변경
                                                                                            // SetLocal 식을 포함하는 ExpressionStatement 문이 피연산자 스택에서 1개를 pop 한다.
                }

                case Jump -> {                                                          // 점프 명령
                    callStack.peek().setInstructionPointer(toSize(code.getOperand()));      // 점프 명령어에 입력된 주소로 명령어 포인터 값을 바꾼다.
                    continue;                                                               // 위에서 명령어 주소를 설정하였기 때문에, 밑에서 명령어 포인터가 1 증가하지 않도록 설정
                }

                case ConditionJump -> {                                                 // 조건을 만족하지 않는 경우 Jump 한다.
                    Object condition = popOperand();
                    if (isTrue(condition)) {                                                // 조건을 만족하면 case 문 탈출
                        break;
                    }
                    callStack.peek().setInstructionPointer(toSize(code.getOperand()));      // 컨디션점프 명령어에 입력된 주소로 명령어 포인터 값을 바꾼다.
                    continue;                                                           // 위에서 명령어 주소를 설정하였기 때문에, 밑에서 명령어 포인터가 1 증가하지 않도록 설정
                }

                case PushNull -> pushOperand(null);

                case PushBoolean -> pushOperand(code.getOperand());

                case PushNumber -> pushOperand(code.getOperand());

                case PushString -> pushOperand(code.getOperand());

                case PushArray -> {
                    List<Object> result = new ArrayList<>();
                    for (int i = 0; i < toSize(code.getOperand()); i++) {
                        result.add(popOperand());
                    }
                    pushOperand(result);                                                // 피연산자 스택에 push 되는 것은 실제 배열이 아니고, 참조이다.
                    objects.put(result, false);                                         // 실제로는 힙 공간에 생성 -> 가비지 콜렉션의 대상이 된다.
                }

                case PushMap -> {
                    Map<String, Object> result = new HashMap<>();
                    for (int i = 0; i < toSize(code.getOperand()); i++) {
                        Object value = popOperand();
                        String key = Datatype.toString(popOperand());
                        result.put(key, value);
                    }
                    pushOperand(result);                                                // 피연산자 스택에 push 되는 것은 실제 맵이 아니고, 참조이다.
                    objects.put(result, false);                                         // 실제로는 힙 공간에 생성 -> 가비지 콜렉션의 대상이 된다.
                }

                case GetElement -> {
                    Object index = popOperand();
                    Object sub = popOperand();

                    if (isArray(sub) && isNumber(index)) {
                        pushOperand(getValueOfArray(sub, index));
                    } else if (isMap(sub) && isString(index)) {
                        pushOperand(getValueOfMap(sub, index));
                    } else {
                        throw new RuntimeException("잘못된 인덱스 접근입니다.");
                    }
                }

                case SetElement -> {                                    // 배열, 맵의 요소 변경
                    Object index = popOperand();
                    Object sub = popOperand();                              // sub는 힙 공간에 있는 배열/맵의 참조이다.
                                                                            // 따라서 sub 내의 요소를 변경하면 힙 공간에 있는 참조된 배열/맵 요소들이 변경된다.
                    if (isArray(sub) && isNumber(index)) {
                        setValueOfArray(sub, index, peekOperand());
                    } else if (isMap(sub) && isString(index)) {
                        setValueOfMap(sub, index, peekOperand());                   // peekOperand() = 바꾸려는 값, SetElement를 감싸는 ExpressionStatement 문이 피연산자 스택에서 1개 pop 한다.
                    } else {
                        throw new RuntimeException("잘못된 인덱스 접근입니다.");
                    }

                }

                case PopOperand -> popOperand();

            }

            callStack.peek().increaseInstructionPointer();              // 명령어 포인터 1 증가
        }
    }

    private void pushOperand(Object value) {                    // 현재 실행하는 스택 프레임의 피연산자 스택에 value push
        callStack.peek().addOperand(value);
    }

    private Object popOperand() {                               // 현재 실행하는 스택 프레임의 피연산자 스택에서 pop
        return callStack.peek().popOperand();
    }

    private Object peekOperand() {                              // 현재 실행하는 스택 프레임의 피연산자 스택에서 peek
        return callStack.peek().peekOperand();
    }

    private void collectGarbage() {                             // mark and sweep 방식 가비지 콜렉션
        for (StackFrame stackFrame : callStack) {                   // 호출 스택을 다 돌면서 마킹
            for (Object value : stackFrame.getOperandStack()) {
                markObject(value);
            }
            for (Object value : stackFrame.getVariables()) {
                markObject(value);
            }
        }

        for (Object value : global.values()) {                      // 전역 변수가 참조하는 공간들도 마킹
            markObject(value);
        }
        sweepObject();                                              // 스윕
    }

    private void markObject(Object value) {                         // 힙 공간에 생성되는 배열, 맵을 재귀적으로 마킹
        if (isArray(value)) {
            if (!objects.get(value)) {
                objects.put(value, true);
                for (Object element : toArray(value)) {
                    markObject(element);
                }
            }
        }

        else if (isMap(value)) {
            if (!objects.get(value)) {
                objects.put(value, true);
                for (Object element : toMap(value).values()) {
                    markObject(element);
                }
            }
        }
    }

    private void sweepObject() {                                // 마킹되지 않은 객체들을 힙 공간에서 제거
        List<Object> remove = new ArrayList<>();

        objects.forEach((ref, mark) -> {
            if (mark) {
                objects.replace(ref, false);
            } else {
                remove.add(ref);
            }
        });

        remove.forEach(ref -> {
            objects.remove(ref);
        });
    }
}
