package parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scanner.Scanner;
import scanner.Token;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private final Scanner scanner = new Scanner();
    private final Parser parser = new Parser();
    private final Printer printer = new Printer();

    @DisplayName("토큰 리스트를 입력으로 받아 구문 분석을 수행한다.")
    @Test
    void parse() {
        // given
        String sourceCode = """
                function main(arg1, arg2) {
                  printLine 'Hello, World!', 1+2 ;
                  printLine 1 + 2 * 3;
                  printLine 1 < 3 < 5;
                  a = b = 3;
                  arr = [1+3, 7, 'str'][2];

                  sum = 0;
                  for i = 0, i < 5, i = i + 1 {
                     sum = sum + i;
                  }
                }
                """;
        List<Token> tokenList = scanner.scan(sourceCode);

        // when
        Program syntaxTree = parser.parse(tokenList);

        // then
        printer.printSyntaxTree(syntaxTree);
    }
}