package parser;

import parser.node.statement.Function;

import java.util.ArrayList;
import java.util.List;

public class Program {
    List<Function> functions = new ArrayList<>();

    public List<Function> getFunctions() {
        return functions;
    }

    public void add(Function function) {
        functions.add(function);
    }
}
