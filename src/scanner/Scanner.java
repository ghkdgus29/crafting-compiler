package scanner;

import parser.Kind;
import parser.Token;

import java.util.ArrayList;
import java.util.List;

public class Scanner {

    private static int index;

    public List<Token> scan(String sourceCode) {                    // 주어진 소스코드의 어휘 분석
        List<Token> result = new ArrayList<>();
        sourceCode += '\0';
        index = 0;

        while (sourceCode.charAt(index) != '\0') {
            char ch = sourceCode.charAt(index);
            switch (getCharType(ch)) {
                case WhiteSpace -> index += 1;

                case NumberLiteral -> result.add(scanNumberLiteral(sourceCode));

                case StringLiteral -> result.add(scanStringLiteral(sourceCode));

                case IdentifierAndKeyword -> result.add(scanIdentifierAndKeyword(sourceCode));

                case OperatorAndPunctuator -> result.add(scanOperatorAndPunctuator(sourceCode));

                default -> throw new RuntimeException(ch + " 사용할 수 없는 문자입니다.");
            }
        }

        result.add(new Token(Kind.EndOfToken));
        return result;
    }

    private CharType getCharType(char ch) {                             // 문자열의 시작부분을 읽고, 종류를 판별하여 반환
        if (' ' == ch || '\t' == ch || '\r' == ch || '\n' == ch) {
            return CharType.WhiteSpace;
        }

        if ('0' <= ch && ch <= '9') {
            return CharType.NumberLiteral;
        }

        if (ch == '\'') {
            return CharType.StringLiteral;
        }

        if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z') {
            return CharType.IdentifierAndKeyword;
        }

        if (33 <= ch && ch <= 47 && ch != '\'' ||
                58 <= ch && ch <= 64 ||
                91 <= ch && ch <= 96 ||
                123 <= ch && ch <= 126) {
            return CharType.OperatorAndPunctuator;
        }

        return CharType.Unknown;
    }

    private Token scanNumberLiteral(String sourceCode) {                // 숫자 리터럴을 분석하여 토큰으로 반환
        String string = "";
        while (isCharType(sourceCode.charAt(index), CharType.NumberLiteral)) {
            string += sourceCode.charAt(index);
            index += 1;
        }
        if (sourceCode.charAt(index) == '.') {
            string += sourceCode.charAt(index);
            index += 1;
            while (isCharType(sourceCode.charAt(index), CharType.NumberLiteral)) {
                string += sourceCode.charAt(index);
                index += 1;
            }
        }

        return new Token(Kind.NumberLiteral, string);
    }

    private Token scanStringLiteral(String sourceCode) {              // 문자 리터럴을 분석하여 토큰으로 반환
        String string = "";
        index += 1;

        while (isCharType(sourceCode.charAt(index), CharType.StringLiteral)) {
            string += sourceCode.charAt(index);
            index += 1;
        }

        if (sourceCode.charAt(index) != '\'') {
            throw new RuntimeException("문자열의 종료 문자가 없습니다.");
        }
        index += 1;

        return new Token(Kind.StringLiteral, string);
    }

    private Token scanIdentifierAndKeyword(String sourceCode) {         // 식별자와 키워드를 분석하여 토큰으로 반환
        String string = "";
        while (isCharType(sourceCode.charAt(index), CharType.IdentifierAndKeyword)) {
            string += sourceCode.charAt(index);
            index += 1;
        }

        Kind kind = Kind.toKind(string);
        if (kind == Kind.UnKnown) {
            kind = Kind.Identifier;
        }
        return new Token(kind, string);
    }

    private Token scanOperatorAndPunctuator(String sourceCode) {        // 연산자와 구분자를 분석하여 토큰으로 반환
        StringBuilder sb = new StringBuilder();
        while (isCharType(sourceCode.charAt(index), CharType.OperatorAndPunctuator)) {
            sb.append(sourceCode.charAt(index));
            index += 1;
        }
        while ((sb.length() > 0) && Kind.toKind(sb.toString()) == Kind.UnKnown) {
            sb.deleteCharAt(sb.length() - 1);
            index -= 1;
        }

        if (sb.isEmpty()) {
            throw new RuntimeException(sourceCode.charAt(index) + " 사용할 수 없는 문자입니다.");
        }

        String string = sb.toString();

        return new Token(Kind.toKind(string), string);
    }

    private boolean isCharType(char ch, CharType type) {            // 문자열이 어디까지 연속되는지 판단
        switch (type) {
            case NumberLiteral -> {
                return '0' <= ch && ch <= '9';
            }

            case StringLiteral -> {
                return 32 <= ch && ch <= 126 && ch != '\'';
            }

            case IdentifierAndKeyword -> {
                return '0' <= ch && ch <= '9' ||
                        'a' <= ch && ch <= 'z' ||
                        'A' <= ch && ch <= 'Z';
            }

            case OperatorAndPunctuator -> {
                return 33 <= ch && ch <= 47 ||
                        58 <= ch && ch <= 64 ||
                        91 <= ch && ch <= 96 ||
                        123 <= ch && ch <= 126;
            }

            default -> {
                return false;
            }
        }
    }
}