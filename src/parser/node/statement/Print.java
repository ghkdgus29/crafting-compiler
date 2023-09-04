package parser.node.statement;

import parser.node.expression.Expression;

import java.util.ArrayList;
import java.util.List;

import static parser.Printer.indent;

public class Print implements Statement{

    private boolean lineFeed = false;
    private List<Expression> arguments = new ArrayList<>();

    public void setLineFeed(boolean lineFeed) {
        this.lineFeed = lineFeed;
    }

    public void add(Expression argument) {
        arguments.add(argument);
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println(lineFeed?"PRINT_LINE:":"PRINT:");
        for (Expression node : arguments) {
            node.print(depth + 1);
        }
    }
}
