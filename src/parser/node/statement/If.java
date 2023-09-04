package parser.node.statement;

import parser.node.expression.Expression;

import java.util.ArrayList;
import java.util.List;

import static parser.Printer.indent;

public class If implements Statement {

    private List<Expression> conditions = new ArrayList<>();
    private List<List<Statement>> blocks = new ArrayList<>();
    private List<Statement> elseBlock;

    public void add(Expression condition) {
        this.conditions.add(condition);
    }

    public void add(List<Statement> block) {
        this.blocks.add(block);
    }

    public void setElseBlock(List<Statement> elseBlock) {
        this.elseBlock = elseBlock;
    }

    @Override
    public void print(int depth) {
        for (int i = 0; i < conditions.size(); i++) {
            indent(depth);
            System.out.println((i == 0 ? "IF:" : "ELIF:"));

            indent(depth + 1);
            System.out.println("CONDITION:");
            conditions.get(i).print(depth + 2);

            indent(depth + 1);
            System.out.println("BLOCK:");

            for (Statement node : blocks.get(i)) {
                node.print(depth + 2);
            }
        }

        if (elseBlock == null) {
            return;
        }

        indent(depth);
        System.out.println("ELSE:");
        for (Statement node : elseBlock) {
            node.print(depth + 1);
        }
    }
}
