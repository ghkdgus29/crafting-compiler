package parser.node.statement;

import parser.node.expression.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static interpreter.Datatype.isTrue;
import static interpreter.Interpreter.local;
import static parser.Printer.indent;

public class If implements Statement {

    private List<Expression> conditions = new ArrayList<>();
    private List<List<Statement>> blocks = new ArrayList<>();
    private List<Statement> elseBlock = new ArrayList<>();

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

    @Override
    public void interpret() {
        for (int i = 0; i < conditions.size(); i++) {
            Object result = conditions.get(i).interpret();
            if (!isTrue(result)) {                                          // 현재 조건문이 참이 아닌 경우, skip
                continue;
            }
            local.get(local.size() - 1).add(0, new HashMap<>());
            for (Statement node : blocks.get(i)) {                           // 조건문 블록 안의 문들 실행
                node.interpret();
            }
            local.get(local.size() - 1).remove(0);
            return;
        }
        if (elseBlock.isEmpty()) {
            return;
        }
        local.get(local.size() - 1).add(0, new HashMap<>());            // else 조건에 걸리는 경우
        for (Statement node : elseBlock) {                                    // else 블록 안의 문들 실행
            node.interpret();
        }
        local.get(local.size() - 1).remove(0);
    }
}
