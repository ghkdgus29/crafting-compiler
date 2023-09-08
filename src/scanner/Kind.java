package scanner;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

public enum Kind {
    UnKnown("#unknown"), EndOfToken("#EndOfToken"),

    NullLiteral("null"),
    TrueLiteral("true"), FalseLiteral("false"),
    NumberLiteral("#Number"), StringLiteral("#String"),
    Identifier("#identifier"),

    Function("function"), Return("return"),
    Variable("var"),
    For("for"), Break("break"), Continue("continue"),
    If("if"), Elif("elif"), Else("else"),
    Print("print"), PrintLine("printLine"),

    LogicalAnd("and"), LogicalOr("or"),
    Assignment("="),
    Add("+"), Subtract("-"),
    Multiply("*"), Divide("/"), Modulo("%"),
    Equal("=="), NotEqual("!="),
    LessThan("<"), GreaterThan(">"),
    LessOrEqual("<="), GreaterOrEqual(">="),

    Comma(","), Colon(":"), Semicolon(";"),
    LeftParen("("), RightParen(")"),
    LeftBrace("{"), RightBrace("}"),
    LeftBracket("["), RightBracket("]"),
    ;

    private final String string;

    Kind(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    private static final Map<String, Kind> stringToKind = Collections.unmodifiableMap(
            Stream.of(values())
                    .collect(Collectors.toMap(Kind::getString, identity()))
    );

    public static Kind toKind(String string) {
        if (stringToKind.containsKey(string)) {
            return stringToKind.get(string);
        }
        return Kind.UnKnown;
    }
}

// 소스 코드에서 사용되는 어휘들을 정의