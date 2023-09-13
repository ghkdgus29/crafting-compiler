package generator;

import javafx.util.Pair;
import parser.Program;
import parser.node.statement.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {

    public static List<Code> codeList = new ArrayList<>();                      // 명령어 리스트
    public static Map<String, Integer> functionTable = new HashMap<>();         // 함수 테이블
    public static int localSize;                                                // 지역 변수 크기
    public static List<List<Integer>> continueStack = new ArrayList<>();        // Continue 명령어 실행 위치를 기록

    public static List<List<Integer>> breakStack = new ArrayList<>();           // Break 명령어 실행 위치를 기록

    private static List<Map<String, Integer>> symbolStack = new ArrayList<>();  // 함수 프레임 내의 위치한 모든 블록들의 지역 변수 위치(인덱스)를 나타낸다.
    private static List<Integer> offsetStack = new ArrayList<>();               // 함수 프레임 내의 위치한 각각의 블록들에서 지역 변수를 저장할 수 있는 위치(블록내의 지역 변수의 개수)를 나타낸다.

    public Pair<List<Code>, Map<String, Integer>> generate(Program program) {
        codeList.clear();
        functionTable.clear();
        writeCode(Instruction.GetGlobal, "main");
        writeCode(Instruction.Call, 0);
        writeCode(Instruction.Exit);
        for (Statement node : program.getFunctions()) {                         // Program 의 모든 함수들을 실행
            node.generate();
        }
        return new Pair<>(codeList, functionTable);
    }

    public static int writeCode(Instruction instruction) {                          // 명령어 목록에 명령어를 생성하여 넣고, 해당 명령어의 위치 반환
        codeList.add(new Code(instruction));
        return codeList.size() - 1;
    }

    public static int writeCode(Instruction instruction, Object operand) {          // 명령어 목록에 명령어와 연산자를 생성하여 넣고, 해당 명령어의 위치 반환
        codeList.add(new Code(instruction, operand));
        return codeList.size() - 1;
    }

    public static void patchAddress(int codeIndex) {                                // codeIndex에 위치한 명령어를 찾아서 해당 명령어의 연산자를, 명령어 목록 개수 + 1 위치를 가리키도록 설정
        codeList.get(codeIndex).setOperand(codeList.size());
    }

    public static int getLocal(String name) {                                       // symbolStack을 사용해 지역변수의 위치 반환
        for (Map<String, Integer> symbolTable : symbolStack) {
            if (symbolTable.containsKey(name)) {
                return symbolTable.get(name);
            }
        }
        return Integer.MAX_VALUE;                                               // symbolStack에 존재하지 않는 경우, 전역변수이다.
    }

    public static void setLocal(String name) {                                                      // 지역 변수 설정
        symbolStack.get(0).put(name, offsetStack.get(offsetStack.size() - 1));                          // symbolStack 에 지역 변수 이름과 지역 변수 위치 (offsetStack 마지막 값) 를 설정
        offsetStack.set(offsetStack.size() - 1, offsetStack.get(offsetStack.size() - 1) + 1);           // offsetStack 의 현재 블록의 지역 변수 개수를 1 증가 시킨다.
        localSize = Math.max(localSize, offsetStack.get(offsetStack.size() - 1));                       // localSize 를 큰 값으로 갱신한다.
    }

    public static void initBlock() {                                            // 블록 초기화, 지역 변수 개수와 offsetStack 을 초기화하고
        localSize = 0;                                                          // symbolStack 에 현재 블록의 지역 변수 공간 추가
        offsetStack.add(0);
        symbolStack.add(0, new HashMap<>());
    }

    public static void pushBlock() {                                            // 블록 추가, 상위 블록의 offsetStack 값을 가져와 새로운 요소로 추가한다.
        symbolStack.add(0, new HashMap<>());
        offsetStack.add(offsetStack.get(offsetStack.size() - 1));
    }

    public static void popBlock() {                                             // 블록 제거, 현재 블록의 offsetStack 요소를 제거한다.
        offsetStack.remove(offsetStack.size() - 1);
        symbolStack.remove(0);
    }

    public static void patchOperand(int codeIndex, int operand) {               //  codeIndex에 위치한 명령어를 찾아서 해당 명령어의 연산자를, operand 로 설정
        codeList.get(codeIndex).setOperand(operand);
    }
}
