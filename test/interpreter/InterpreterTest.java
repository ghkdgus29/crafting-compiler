package interpreter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.Program;
import scanner.Scanner;
import scanner.Token;

import java.util.List;

class InterpreterTest {

    private final Scanner scanner = new Scanner();
    private final Parser parser = new Parser();
    private final Interpreter interpreter = new Interpreter();

    @DisplayName("문자열을 출력한다.")
    @Test
    void printString() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
                  printLine 'Hello, World!';
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("산술연산을 수행한다.")
    @Test
    void calculateArithmetic() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
                  printLine 1 * 2 + 3 * 4;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("논리연산을 수행한다.")
    @Test
    void calculateLogical() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
                  printLine true or 'Hello, world!';
                  printLine false or 'Hello, world!';
                  printLine true and 'Hello, world!';
                  printLine false and 'Hello, world!';
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("변수의 선언과 참조, 대입을 수행한다.")
    @Test
    void manageVariable() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
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

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("반복문을 실행한다.")
    @Test
    void executeFor() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
                  for i=0, i<3, i=i+1 {
                    printLine 'i: ', i;
                  }
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("조건문을 실행한다.")
    @Test
    void executeConditionStatement() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
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

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("Continue, Break 문을 실행한다.")
    @Test
    void continueAndBreakInFor() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
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

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("반환값, 매개변수가 없는 함수 호출")
    @Test
    void call() {
        // given
        String sourceCode = """
                function main() {
                    sayHo();
                }
                
                function sayHo() {
                    printLine 'Ho!';
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("매개변수가 있는 함수 호출")
    @Test
    void callWithParameters() {
        // given
        String sourceCode = """
                function main() {
                    add(1, 3);
                }
                
                function add(a, b) {
                    printLine a + b;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("매개변수와 반환값이 있는 함수 호출")
    @Test
    void callWithParametersAndReturn() {
        // given
        String sourceCode = """
                function main() {
                    var ans = add(3, 4);
                    printLine ans;
                }
                
                function add(a, b) {
                    return a * a + b * b;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
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

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("배열과 맵 리터럴 출력")
    @Test
    void printListAndMapLiteral() {
        // given
        String sourceCode = """
                function main() {
                    var list = [1, 2, 3];
                    var map = {'a':1, 'b':2, 'c':3};
                    
                    printLine list;
                    printLine map;
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("배열과 맵의 원소값 참조")
    @Test
    void getElement() {
        // given
        String sourceCode = """
                function main() {
                    var list = [1, 2, 3];
                    var map = {'a':1, 'b':2, 'c':3};
                    
                    printLine list[2];
                    printLine map['a'];
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }

    @DisplayName("배열과 맵의 원소값 변경")
    @Test
    void modifyElement() {
        // given
        String sourceCode = """
                function main() {
                    var list = [1, 2, 3];
                    var map = {'a':1, 'b':2, 'c':3};
                    
                    list[2] = 10;
                    map['a'] = 'abc';
                    
                    printLine list[2];
                    printLine map['a'];
                }
                """;

        List<Token> tokenList = scanner.scan(sourceCode);
        Program syntaxTree = parser.parse(tokenList);

        // when // then
        interpreter.interpret(syntaxTree);
    }
}