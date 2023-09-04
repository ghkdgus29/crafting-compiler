package parser;

import parser.node.expression.*;
import parser.node.statement.*;
import scanner.Kind;
import scanner.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Parser {

    private static int index;


    public Program parse(List<Token> tokens) {
        Program result = new Program();
        index = 0;

        while (tokens.get(index).getKind() != Kind.EndOfToken) {
            switch (tokens.get(index).getKind()) {
                case Function -> {
                    result.add(parseFunction(tokens));
                }

                default -> {
                    throw new RuntimeException(tokens.get(index) + " 잘못된 구문입니다.");
                }
            }
        }

        return result;
    }

    private Function parseFunction(List<Token> tokens) {
        Function result = new Function();
        skipCurrent(tokens, Kind.Function);
        result.setName(tokens.get(index).getString());
        skipCurrent(tokens, Kind.Identifier);
        skipCurrent(tokens, Kind.LeftParen);
        if (tokens.get(index).getKind() != Kind.RightParen) {
            do {
                result.add(tokens.get(index).getString());
                skipCurrent(tokens, Kind.Identifier);
            } while (skipCurrentIf(tokens, Kind.Comma));
        }
        skipCurrent(tokens, Kind.RightParen);
        skipCurrent(tokens, Kind.LeftBrace);
        result.setBlock(parseBlock(tokens));
        skipCurrent(tokens, Kind.RightBrace);
        return result;
    }

    private List<Statement> parseBlock(List<Token> tokens) {
        List<Statement> result = new ArrayList<>();

        while (tokens.get(index).getKind() != Kind.RightBrace) {
            switch (tokens.get(index).getKind()) {
                default -> result.add(parseExpressionStatement(tokens));

                case Variable -> result.add(parseVariable(tokens));

                case For -> result.add(parseFor(tokens));

                case If -> result.add(parseIf(tokens));

                case Print, PrintLine -> result.add(parsePrint(tokens));

                case Return -> result.add(parseReturn(tokens));

                case Break -> result.add(parseBreak(tokens));

                case Continue -> result.add(parseContinue(tokens));

                case EndOfToken -> throw new RuntimeException(tokens.get(index) + " 잘못된 구문입니다.");
            }
        }

        return result;
    }

    private Variable parseVariable(List<Token> tokens) {
        Variable result = new Variable();
        skipCurrent(tokens, Kind.Variable);
        result.setName(tokens.get(index).getString());
        skipCurrent(tokens, Kind.Identifier);
        skipCurrent(tokens, Kind.Assignment);
        result.setExpression(parseExpression(tokens));
        if (result.getExpression() == null) {
            throw new RuntimeException("변수 선언에 초기화식이 없습니다.");
        }
        skipCurrent(tokens, Kind.Semicolon);
        return result;
    }

    private For parseFor(List<Token> tokens) {
        For result = new For();
        skipCurrent(tokens, Kind.For);

        result.setVariable(new Variable());
        result.getVariable().setName(tokens.get(index).getString());
        skipCurrent(tokens, Kind.Identifier);
        skipCurrent(tokens, Kind.Assignment);

        result.getVariable().setExpression(parseExpression(tokens));
        if (result.getVariable().getExpression() == null) {
            throw new RuntimeException("for 문에 초기화식이 없습니다.");
        }
        skipCurrentIf(tokens, Kind.Comma);

        result.setCondition(parseExpression(tokens));
        if (result.getCondition() == null) {
            throw new RuntimeException("for 문에 조건식이 없습니다.");
        }
        skipCurrent(tokens, Kind.Comma);

        result.setExpression(parseExpression(tokens));
        if (result.getExpression() == null) {
            throw new RuntimeException("for 문에 증감식이 없습니다.");
        }
        skipCurrent(tokens, Kind.LeftBrace);

        result.setBlock(parseBlock(tokens));
        skipCurrent(tokens, Kind.RightBrace);
        return result;
    }

    private If parseIf(List<Token> tokens) {
        If result = new If();
        skipCurrent(tokens, Kind.If);
        do {
            Expression condition = parseExpression(tokens);
            if (condition == null) {
                throw new RuntimeException("if 문에 조건식이 없습니다.");
            }
            result.add(condition);

            skipCurrent(tokens, Kind.LeftBrace);
            result.add(parseBlock(tokens));
            skipCurrent(tokens, Kind.RightBrace);
        } while (skipCurrentIf(tokens, Kind.Elif));

        if (skipCurrentIf(tokens, Kind.Else)) {
            skipCurrent(tokens, Kind.LeftBrace);
            result.setElseBlock(parseBlock(tokens));
            skipCurrent(tokens, Kind.RightBrace);
        }

        return result;
    }

    private Print parsePrint(List<Token> tokens) {
        Print result = new Print();
        result.setLineFeed(tokens.get(index).getKind() == Kind.PrintLine);
        skipCurrent();
        if (tokens.get(index).getKind() != Kind.Semicolon) {
            do {
                result.add(parseExpression(tokens));
            } while (skipCurrentIf(tokens, Kind.Comma));
        }
        skipCurrent(tokens, Kind.Semicolon);
        return result;
    }

    private Return parseReturn(List<Token> tokens) {
        Return result = new Return();
        skipCurrent(tokens, Kind.Return);
        result.setExpression(parseExpression(tokens));

        if (result.getExpression() == null) {
            throw new RuntimeException("return 문에 식이 없습니다.");
        }
        skipCurrent(tokens, Kind.Semicolon);
        return result;
    }

    private Break parseBreak(List<Token> tokens) {
        Break result = new Break();
        skipCurrent(tokens, Kind.Break);
        skipCurrent(tokens, Kind.Semicolon);
        return result;
    }

    private Continue parseContinue(List<Token> tokens) {
        Continue result = new Continue();
        skipCurrent(tokens, Kind.Continue);
        skipCurrent(tokens, Kind.Semicolon);
        return result;
    }

    private ExpressionStatement parseExpressionStatement(List<Token> tokens) {
        ExpressionStatement result = new ExpressionStatement();
        result.setExpression(parseExpression(tokens));
        skipCurrent(tokens, Kind.Semicolon);
        return result;
    }

    private Expression parseExpression(List<Token> tokens) {
        return parseAssignment(tokens);
    }

    private Expression parseAssignment(List<Token> tokens) {
        Expression result = parseOr(tokens);
        if (tokens.get(index).getKind() != Kind.Assignment) {
            return result;
        }
        skipCurrent(tokens, Kind.Assignment);

        if (result instanceof GetVariable getVariable) {
            SetVariable setVariable = new SetVariable();
            setVariable.setName(getVariable.getName());
            setVariable.setValue(parseAssignment(tokens));
            return setVariable;
        }

        if (result instanceof GetElement getElement) {
            SetElement setElement = new SetElement();
            setElement.setSub(getElement.getSub());
            setElement.setIndex(getElement.getIndex());
            setElement.setValue(parseAssignment(tokens));
            return setElement;
        }

        throw new RuntimeException("잘못된 대입 연산 식입니다.");
    }

    private Expression parseOr(List<Token> tokens) {
        Expression result = parseAnd(tokens);
        while (skipCurrentIf(tokens, Kind.LogicalOr)) {
            Or temp = new Or();
            temp.setLhs(result);
            temp.setRhs(parseAnd(tokens));
            result = temp;
        }
        return result;
    }

    private Expression parseAnd(List<Token> tokens) {
        Expression result = parseRelational(tokens);
        while (skipCurrentIf(tokens, Kind.LogicalAnd)) {
            And temp = new And();
            temp.setLhs(result);
            temp.setRhs(parseRelational(tokens));
            result = temp;
        }
        return result;
    }

    private Expression parseRelational(List<Token> tokens) {
        Set<Kind> operators = Set.of(
                Kind.Equal,
                Kind.NotEqual,
                Kind.LessThan,
                Kind.GreaterThan,
                Kind.LessOrEqual,
                Kind.GreaterOrEqual);

        Expression result = parseArithmetic1(tokens);
        while (operators.contains(tokens.get(index).getKind())) {
            Relational temp = new Relational();
            temp.setKind(tokens.get(index).getKind());
            skipCurrent();
            temp.setLhs(result);
            temp.setRhs(parseArithmetic1(tokens));
            result = temp;
        }
        return result;
    }

    private Expression parseArithmetic1(List<Token> tokens) {
        Set<Kind> operators = Set.of(
                Kind.Add,
                Kind.Subtract
        );

        Expression result = parseArithmetic2(tokens);
        while (operators.contains(tokens.get(index).getKind())) {
            Arithmetic temp = new Arithmetic();
            temp.setKind(tokens.get(index).getKind());
            skipCurrent();
            temp.setLhs(result);
            temp.setRhs(parseArithmetic2(tokens));
            result = temp;
        }
        return result;
    }

    private Expression parseArithmetic2(List<Token> tokens) {
        Set<Kind> operators = Set.of(
                Kind.Multiply,
                Kind.Divide,
                Kind.Modulo
        );

        Expression result = parseUnary(tokens);
        while (operators.contains(tokens.get(index).getKind())) {
            Arithmetic temp = new Arithmetic();
            temp.setKind(tokens.get(index).getKind());
            skipCurrent();
            temp.setLhs(result);
            temp.setRhs(parseUnary(tokens));
            result = temp;
        }
        return result;
    }

    private Expression parseUnary(List<Token> tokens) {
        Set<Kind> operators = Set.of(
                Kind.Add,
                Kind.Subtract
        );

        while (operators.contains(tokens.get(index).getKind())) {
            Unary result = new Unary();
            result.setKind(tokens.get(index).getKind());
            skipCurrent();
            result.setSub(parseUnary(tokens));
            return result;
        }

        return parseOperand(tokens);
    }

    private Expression parseOperand(List<Token> tokens) {
        Expression result = null;
        switch (tokens.get(index).getKind()) {
            case NullLiteral -> result = parseNullLiteral(tokens);

            case TrueLiteral, FalseLiteral -> result = parseBooleanLiteral(tokens);

            case NumberLiteral -> result = parseNumberLiteral(tokens);

            case StringLiteral -> result = parseStringLiteral(tokens);

            case LeftBracket -> result = parseListLiteral(tokens);

            case LeftBrace -> result = parseMapLiteral(tokens);

            case Identifier -> result = parseIdentifier(tokens);

            case LeftParen -> result = parseInnerExpression(tokens);

            default -> throw new RuntimeException("잘못된 식입니다.");
        }

        return parsePostfix(tokens, result);
    }

    private Expression parseNullLiteral(List<Token> tokens) {
        skipCurrent(tokens, Kind.NullLiteral);
        NullLiteral result = new NullLiteral();
        return result;
    }

    private Expression parseBooleanLiteral(List<Token> tokens) {
        BooleanLiteral result = new BooleanLiteral();
        result.setValue(tokens.get(index).getKind() == Kind.TrueLiteral);
        skipCurrent();
        return result;
    }

    private Expression parseNumberLiteral(List<Token> tokens) {
        NumberLiteral result = new NumberLiteral();
        result.setValue(Double.parseDouble(tokens.get(index).getString()));
        skipCurrent(tokens, Kind.NumberLiteral);
        return result;
    }

    private Expression parseStringLiteral(List<Token> tokens) {
        StringLiteral result = new StringLiteral();
        result.setValue(tokens.get(index).getString());
        skipCurrent(tokens, Kind.StringLiteral);
        return result;
    }

    private Expression parseListLiteral(List<Token> tokens) {
        ArrayLiteral result = new ArrayLiteral();
        skipCurrent(tokens, Kind.LeftBracket);
        if (tokens.get(index).getKind() != Kind.RightBracket) {
            do {
                result.add(parseExpression(tokens));
            } while (skipCurrentIf(tokens, Kind.Comma));
        }
        skipCurrent(tokens, Kind.RightBracket);
        return result;
    }

    private Expression parseMapLiteral(List<Token> tokens) {
        MapLiteral result = new MapLiteral();
        skipCurrent(tokens, Kind.LeftBrace);
        if (tokens.get(index).getKind() != Kind.RightBrace) {
            do {
                String name = tokens.get(index).getString();
                skipCurrent(tokens, Kind.StringLiteral);
                skipCurrent(tokens, Kind.Colon);
                Expression value = parseExpression(tokens);
                result.put(name, value);
            } while (skipCurrentIf(tokens, Kind.Comma));
        }
        skipCurrent(tokens, Kind.RightBrace);
        return result;
    }

    private Expression parseIdentifier(List<Token> tokens) {
        GetVariable result = new GetVariable();
        result.setName(tokens.get(index).getString());
        skipCurrent(tokens, Kind.Identifier);
        return result;
    }

    private Expression parseInnerExpression(List<Token> tokens) {
        skipCurrent(tokens, Kind.LeftParen);
        Expression result = parseExpression(tokens);
        skipCurrent(tokens, Kind.RightParen);
        return result;
    }

    private Expression parsePostfix(List<Token> tokens, Expression sub) {
        while (true) {
            switch (tokens.get(index).getKind()) {
                case LeftParen -> sub = parseCall(tokens, sub);

                case LeftBracket -> sub = parseElement(tokens, sub);

                default -> {
                    return sub;
                }
            }
        }
    }

    private Expression parseCall(List<Token> tokens, Expression sub) {
        Call result = new Call();
        result.setSub(sub);
        skipCurrent(tokens, Kind.LeftParen);
        if (tokens.get(index).getKind() != Kind.RightParen) {
            do {
                result.add(parseExpression(tokens));
            } while (skipCurrentIf(tokens, Kind.Comma));
        }
        skipCurrent(tokens, Kind.RightParen);
        return result;
    }

    private Expression parseElement(List<Token> tokens, Expression sub) {
        GetElement result = new GetElement();
        result.setSub(sub);
        skipCurrent(tokens, Kind.LeftBracket);
        result.setIndex(parseExpression(tokens));
        skipCurrent(tokens, Kind.RightBracket);
        return result;
    }

    private void skipCurrent() {
        index += 1;
    }

    private void skipCurrent(List<Token> tokens, Kind kind) {
        if (tokens.get(index).getKind() != kind) {
            throw new RuntimeException(kind + " 토큰이 필요합니다.");
        }
        index += 1;
    }

    private boolean skipCurrentIf(List<Token> tokens, Kind kind) {
        if (tokens.get(index).getKind() != kind) {
            return false;
        }
        index += 1;
        return true;
    }


}
