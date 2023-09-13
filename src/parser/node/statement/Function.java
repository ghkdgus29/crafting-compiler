package parser.node.statement;

import generator.Instruction;

import java.util.ArrayList;
import java.util.List;

import static generator.Generator.*;
import static parser.Printer.indent;

public class Function implements Statement {

    private String name;
    private List<String> parameters = new ArrayList<>();
    private List<Statement> block;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameterOf(int index) {
        return parameters.get(index);
    }

    public void add(String parameter) {
        this.parameters.add(parameter);
    }

    public void setBlock(List<Statement> block) {
        this.block = block;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("FUNCTION " + name + ": ");
        if (!parameters.isEmpty()) {
            indent(depth + 1);
            System.out.print("PARAMETERS:");
            for (String name : parameters) {
                System.out.print(name + " ");
            }
            System.out.println();
        }
        indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement node : block) {
            node.print(depth + 2);
        }
    }

    @Override
    public void interpret() {
        for (Statement node : block) {
            node.interpret();
        }
    }

    @Override
    public void generate() {
        functionTable.put(name, codeList.size());               // 함수 테이블에 함수의 이름과 명령어가 시작하는 위치 저장
        int temp = writeCode(Instruction.Alloca);               // 함수내의 지역 변수 저장을 위한 공간 할당 명령어 생성, 아직 몇 개의 변수 공간이 필요한지는 모른다.
        initBlock();                                        // 함수 블록 초기화
        for (String name : parameters) {
            setLocal(name);                                     // 파라미터들을 지역 변수로 설정
        }
        for (Statement node : block) {
            node.generate();                                    // 함수 내의 문들 실행
        }
        popBlock();                                         // 함수 블록 제거
        patchOperand(temp, localSize);                          // 함수내의 지역 변수 저장을 위한 공간 크기 설정, 이제는 몇 개의 변수 공간이 필요한지 안다.
        writeCode(Instruction.Return);                          // 반환 명령어 생성
    }
}
