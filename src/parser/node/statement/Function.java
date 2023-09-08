package parser.node.statement;

import java.util.ArrayList;
import java.util.List;

import static parser.Printer.indent;

public class Function implements Statement {

    private String name;
    private List<String> parameters = new ArrayList<>();
    private List<Statement> block;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameterOf(int index) {
        return parameters.get(index);
    }

    public void add(String parameter) {
        this.parameters.add(parameter);
    }

    public void setBlock(List<Statement> block) {
        this.block = block;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("FUNCTION " + name + ": ");
        if (!parameters.isEmpty()) {
            indent(depth + 1);
            System.out.print("PARAMETERS:");
            for (String name : parameters) {
                System.out.print(name + " ");
            }
            System.out.println();
        }
        indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement node : block) {
            node.print(depth + 2);
        }
    }

    @Override
    public void interpret() {
        for (Statement node : block) {
            node.interpret();
        }
    }
}
