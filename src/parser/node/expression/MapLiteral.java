package parser.node.expression;

import java.util.HashMap;
import java.util.Map;

import static parser.Printer.indent;

public class MapLiteral implements Expression {

    private Map<String, Expression> values = new HashMap<>();

    public void put(String key, Expression value) {
        values.put(key, value);
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("{");

        for (String key : values.keySet()) {
            System.out.print(key + ": ");
            values.get(key).print(depth + 1);
        }
        indent(depth);
        System.out.println("}");
    }
}
