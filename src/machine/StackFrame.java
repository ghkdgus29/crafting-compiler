package machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackFrame {

    List<Object> variables = new ArrayList<>();                // 함수내의 변수 공간
    Stack<Object> operandStack = new Stack<>();                // 피연산자 스택
    int instructionPointer = 0;                                // 명령어 포인터

    public Object getVariableAt(int index) {                // 변수 공간의 n 번째 인덱스의 변수를 가져온다.
        return variables.get(index);
    }

    public void changeValueAt(int index, Object value) {            // 변수 공간의 n 번째 인덱스의 변수를 바꾼다.
        if (variables.size() > index) {
            variables.set(index, value);
        } else {
            variables.add(value);
        }
    }

    public void addVariable(Object variable) {              // 변수 공간에 변수를 추가한다.
        variables.add(variable);
    }

    public List<Object> getVariables() {
        return variables;
    }

    public void addOperand(Object value) {                  // 피연산자 스택에 피연산자를 push
        operandStack.push(value);
    }

    public Object popOperand() {                            // 피연산자 스택에 피연산자를 pop
        return operandStack.pop();
    }

    public Object peekOperand() {                           // 피연산자 스택에 top 값을 peek
        return operandStack.peek();
    }

    public boolean isOperandStackEmpty() {
        return operandStack.isEmpty();
    }

    public Stack<Object> getOperandStack() {
        return operandStack;
    }

    public int getInstructionPointer() {
        return instructionPointer;
    }

    public void setInstructionPointer(int instructionPointer) {            // 명령어 포인터 값을 변경한다. (Jump, ConditionJump)
        this.instructionPointer = instructionPointer;
    }

    public void increaseInstructionPointer() {                             // 명령어 포인터 값을 1 증가한다. (일반적인 명령어 진행 흐름)
        instructionPointer += 1;
    }
}
