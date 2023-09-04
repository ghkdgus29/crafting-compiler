package scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScannerTest {

    private final Scanner scanner = new Scanner();

    @DisplayName("Hello World 의 어휘를 분석해보기")
    @Test
    void printHelloWorldToken() {
        // given
        String sourceCode = """
                function main() {
                    print 'Hello, World!';
                }
                """;

        // when
        List<Token> tokenList = scanner.scan(sourceCode);

        // then
        printTokenList(tokenList);
    }

    @DisplayName("분석할 수 없는 연산자의 경우 예외가 발생")
    @Test
    void notAcceptableOperatorException() {
        // given
        String sourceCode = """
                function main() {
                    print !!;
                }
                """;

        // when, then
        assertThatThrownBy(() -> scanner.scan(sourceCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("! 사용할 수 없는 문자입니다.");
    }

    private void printTokenList(List<Token> tokenList) {
        System.out.println("KIND               STRING     ");
        System.out.println("------------------------------");
        for (Token token : tokenList) {
            System.out.println(token);
        }
    }
}