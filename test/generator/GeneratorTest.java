package generator;

import interpreter.Interpreter;
import javafx.util.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.Program;
import scanner.Scanner;
import scanner.Token;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    private final Scanner scanner = new Scanner();
    private final Parser parser = new Parser();
    private final Generator generator = new Generator();

    @DisplayName("문자열을 출력하는 목적 코드 생성")
    @Test
    void printString() {
        // given
        String sourceCode = """
                function main() {
                  print 'Hello, World!';
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("산술 연산자 목적 코드 생성")
    @Test
    void arithmetic() {
        // given
        String sourceCode = """
                function main() {
                  print 1 * 2 + 3 * 4;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("식을 임의로 소비하는 문의 목적 코드 생성")
    @Test
    void expressionStatement() {
        // given
        String sourceCode = """
                function main() {
                  1 + 2;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("논리 연산 목적 코드 생성")
    @Test
    void logical() {
        // given
        String sourceCode = """
                function main() {
                  print false or 1 + 2;
                  
                  print true and 1 + 2;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("변수 선언 목적 코드 생성")
    @Test
    void defineVariable() {
        // given
        String sourceCode = """
                function main() {
                  var a = 'first';
                  var b = 'second';
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("변수 참조 및 수정 목적 코드 생성")
    @Test
    void getVariable() {
        // given
        String sourceCode = """
                function main() {
                  var local = 'first';
                  local = 'second';
                  
                  print local;
                  
                  global = 'first';
                  global = 'second';
                  
                  print global;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("단순 반복문 목적 코드 생성")
    @Test
    void executeFor() {
        // given
        String sourceCode = """
                function main() {
                  for i=0, i<3, i=i+1 {
                    print i;
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("Continue 반복문 목적 코드 생성")
    @Test
    void continueFor() {
        // given
        String sourceCode = """
                function main() {
                  for i=0, i<3, i=i+1 {
                    continue;
                    print i;
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("Break 반복문 목적 코드 생성")
    @Test
    void breakFor() {
        // given
        String sourceCode = """
                function main() {
                  for i=0, i<3, i=i+1 {
                    break;
                    print i;
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("if 문 목적 코드 생성")
    @Test
    void executeIf() {
        // given
        String sourceCode = """
                function main() {
                  if true {
                    print 'if';
                  } elif true {
                    print 'elif';
                  } else {
                    print 'else';
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("함수 호출 목적 코드 생성")
    @Test
    void callFunc() {
        // given
        String sourceCode = """
                function main() {
                  print add(3, 4);
                }
                
                function add(a, b) {
                  return a + b;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("배열과 맵 목적 코드 생성")
    @Test
    void arrayAndMapLiteral() {
        // given
        String sourceCode = """
                function main() {
                  [1, 2, 'element'];
                  
                  {'a' : 'a_val', 'b' : 1};
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    @DisplayName("배열과 맵 참조 코드 생성")
    @Test
    void arrayAndMapLiteralGetElement() {
        // given
        String sourceCode = """
                function main() {
                  [1, 2, 'element'][1];
                  
                  {'a' : 'a_val', 'b' : 1}['b'];
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }


    @DisplayName("배열 원소 변경 코드 생성")
    @Test
    void arraySetElement() {
        // given
        String sourceCode = """
                function main() {
                  var a = [1, 2, 'element'];
                  
                  a[2] = 3;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // then
        printObjectCode(objectCode);
    }

    private void printObjectCode(Pair<List<Code>, Map<String, Integer>> objectCode) {
        List<Code> codeList = objectCode.getKey();
        Map<String, Integer> functionTable = objectCode.getValue();

        System.out.printf("%-11s %s\n", "FUNCTION", "ADDRESS");
        System.out.println("-".repeat(18));

        for (String key : functionTable.keySet()) {
            System.out.printf("%-11s %s\n", key, functionTable.get(key));
        }
        System.out.println();

        System.out.printf("%s %-15s %s\n", "ADDR", "INSTRUCTION", "OPERAND");
        System.out.println("-".repeat(36));

        for (int i = 0; i < codeList.size(); i++) {
            System.out.printf("%4d %s\n", i, codeList.get(i));
        }
    }
}