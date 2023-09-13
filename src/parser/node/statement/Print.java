package parser.node.statement;

import generator.Generator;
import generator.Instruction;
import parser.node.expression.Expression;

import java.util.ArrayList;
import java.util.List;

import static generator.Generator.writeCode;
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

    @Override
    public void interpret() {
        for (Expression node : arguments) {
            Object value = node.interpret();
            System.out.print(value);
        }
        if (lineFeed) {
            System.out.println();
        }
    }

    @Override
    public void generate() {
        for (int i = arguments.size()-1; i >= 0; i--) {
            arguments.get(i).generate();                            // 피연산자 스택에 인자의 마지막 값부터 넣는다.
        }
        writeCode(Instruction.Print, arguments.size());             // 출력 명령어 + 인자 개수
        if (lineFeed) {
            writeCode(Instruction.PrintLine);
        }
    }
}
