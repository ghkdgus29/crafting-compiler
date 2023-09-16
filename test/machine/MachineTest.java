package machine;

import generator.Code;
import generator.Generator;
import javafx.util.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.Program;
import scanner.Scanner;
import scanner.Token;

import java.util.List;
import java.util.Map;

class MachineTest {

    private final Scanner scanner = new Scanner();
    private final Parser parser = new Parser();
    private final Generator generator = new Generator();
    private final Machine machine = new Machine();

    @DisplayName("문자열을 출력")
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
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("산술 연산자 실행")
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
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("식을 임의로 소비하는 문 실행")
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
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("논리 연산 실행")
    @Test
    void logical() {
        // given
        String sourceCode = """
                function main() {
                  printLine true or 'Hello, world!';
                  printLine false or 'Hello, world!';
                  printLine true and 'Hello, world!';
                  printLine false and 'Hello, world!';
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("변수 선언, 참조, 대입")
    @Test
    void variable() {
        // given
        String sourceCode = """
                function main() {
                  global = 4;
                  var local = 13;
                  printLine 'global: ', global;
                  printLine 'local: ', local;
                  
                  global = local = 7;
                  printLine 'global: ', global;
                  printLine 'local: ', local;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("단순 반복문 실행")
    @Test
    void executeFor() {
        // given
        String sourceCode = """
                function main() {
                  for i=0, i<3, i=i+1 {
                    printLine i;
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("조건문 실행")
    @Test
    void executeIf() {
        // given
        String sourceCode = """
                function main() {
                  for i=0, i<6, i=i+1 {
                    if i == 1 {
                        printLine 'one';
                    } elif i == 2 {
                        printLine 'two';
                    } elif i == 3 {
                        printLine 'three';
                    } else {
                        printLine i;
                    }
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("Continue, Break 반복문 실행")
    @Test
    void continueFor() {
        // given
        String sourceCode = """
                function main() {
                  for i=0, i<10, i=i+1 {
                    if i == 1 {
                        printLine 'one';
                    } elif i == 2 {
                        continue;
                        printLine 'two';
                    } elif i == 3 {
                        continue;
                        printLine 'three';
                    } elif i == 7 {
                        break;
                    } else {
                        printLine i;
                    }
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("함수 호출")
    @Test
    void callFunc() {
        // given
        String sourceCode = """
                function main() {
                  sayHo();
                  printLine add(3, 4);
                  sayHo();
                }
                
                function add(a, b) {
                  return a + b;
                }
                
                function sayHo() {
                  printLine 'Ho!';
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("내장 함수 호출")
    @Test
    void callBuiltinFunction() {
        // given
        String sourceCode = """
                function main() {
                    printLine sqrt(25);
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("배열과 맵 선언")
    @Test
    void arrayAndMapLiteral() {
        // given
        String sourceCode = """
                function main() {
                  var a = [1, 2, 'element'];
                  var b = {'a' : 'a_val', 'b' : 1};
                  
                  printLine a;
                  printLine b;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("배열과 맵 참조")
    @Test
    void arrayAndMapLiteralGetElement() {
        // given
        String sourceCode = """
                function main() {
                  printLine [1, 2, 'element'][1];
                  printLine {'a' : 'a_val', 'b' : 1}['b'];
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("배열 원소 변경")
    @Test
    void arraySetElement() {
        // given
        String sourceCode = """
                function main() {
                  var a = [1, 2, 'element'];
                  
                  a[2] = 3;
                  
                  printLine a;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }

    @DisplayName("종합적인 실행")
    @Test
    void execute() {
        // given
        String sourceCode = """
                function main() {
                  printLine 'Hello, World!', 1+2 ;
                  printLine 1 + 2 * 3;
                  a = b = 3;
                  arr = [1+3, 7, 'str'][2];

                  sum = 0;
                  for i = 0, i < 5, i = i + 1 {
                     sum = sum + i;
                  }
                  
                  printLine sum;
                  printLine add(27, 3);
                  printLine sqrt(100);
                }
                
                function add(a, b){
                    return a + b;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);
        Pair<List<Code>, Map<String, Integer>> objectCode = generator.generate(syntaxTree);

        // when // then
        machine.execute(objectCode);
    }
}