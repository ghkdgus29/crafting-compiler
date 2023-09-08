package scanner;

public class Token {

    Kind kind = Kind.UnKnown;
    String string;

    public Token(Kind kind) {
        this.kind = kind;
    }

    public Token(Kind kind, String string) {
        this.kind = kind;
        this.string = string;
    }

    public Kind getKind() {
        return kind;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return "Token{" +
                "kind=" + kind +
                ", string='" + string + '\'' +
                '}';
    }
}