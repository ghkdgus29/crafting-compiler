package parser;

import java.util.HashMap;
import java.util.Map;

public enum Kind {
    UnKnown, EndOfToken,

    NullLiteral,
    TrueLiteral, FalseLiteral,
    NumberLiteral, StringLiteral,
    Identifier,

    Function, Return,
    Variable,
    For, Break, Continue,
    If, Elif, Else,
    Print, PrintLine,

    LogicalAnd, LogicalOr,
    Assignment,
    Add, Subtract,
    Multiply, Divide, Modulo,
    Equal, NotEqual,
    LessThan, GreaterThan,
    LessOrEqual, GreaterOrEqual,

    Comma, Colon, Semicolon,
    LeftParen, RightParen,
    LeftBrace, RightBrace,
    LeftBracket, RightBracket,
    ;


    private static final Map<String, Kind> stringToKind = initializeTokenMap();

    private static Map<String, Kind> initializeTokenMap() {
        Map<String, Kind> tokenMap = new HashMap<>();

        tokenMap.put("#unkown", Kind.UnKnown);
        tokenMap.put("#EndOfToekn", Kind.EndOfToken);

        tokenMap.put("null", Kind.NullLiteral);
        tokenMap.put("true", Kind.TrueLiteral);
        tokenMap.put("false", Kind.FalseLiteral);
        tokenMap.put("#Number", Kind.NumberLiteral);
        tokenMap.put("#String", Kind.StringLiteral);
        tokenMap.put("#identifier", Kind.Identifier);

        tokenMap.put("function", Kind.Function);
        tokenMap.put("return", Kind.Return);
        tokenMap.put("var", Kind.Variable);
        tokenMap.put("for", Kind.For);
        tokenMap.put("break", Kind.Break);
        tokenMap.put("continue", Kind.Continue);
        tokenMap.put("if", Kind.If);
        tokenMap.put("elif", Kind.Elif);
        tokenMap.put("else", Kind.Else);
        tokenMap.put("print", Kind.Print);
        tokenMap.put("printLine", Kind.PrintLine);

        tokenMap.put("and", Kind.LogicalAnd);
        tokenMap.put("or", Kind.LogicalOr);

        tokenMap.put("=", Kind.Assignment);

        tokenMap.put("+", Kind.Add);
        tokenMap.put("-", Kind.Subtract);
        tokenMap.put("*", Kind.Multiply);
        tokenMap.put("/", Kind.Divide);
        tokenMap.put("%", Kind.Modulo);

        tokenMap.put("==", Kind.Equal);
        tokenMap.put("!=", Kind.NotEqual);
        tokenMap.put("<", Kind.LessThan);
        tokenMap.put(">", Kind.GreaterThan);
        tokenMap.put("<=", Kind.LessOrEqual);
        tokenMap.put(">=", Kind.GreaterOrEqual);

        tokenMap.put(",", Kind.Comma);
        tokenMap.put(":", Kind.Colon);
        tokenMap.put(";", Kind.Semicolon);
        tokenMap.put("(", Kind.LeftParen);
        tokenMap.put(")", Kind.RightParen);
        tokenMap.put("{", Kind.LeftBrace);
        tokenMap.put("}", Kind.RightBrace);
        tokenMap.put("[", Kind.LeftBracket);
        tokenMap.put("]", Kind.RightBracket);

        return tokenMap;
    }

    public static Kind toKind(String string) {
        if (stringToKind.containsKey(string)) {
            return stringToKind.get(string);
        }
        return Kind.UnKnown;
    }
}

// 소스 코드에서 사용되는 어휘들을 정의