package parser.node.expression;

import generator.Instruction;

import static generator.Generator.writeCode;
import static interpreter.Datatype.*;
import static parser.Printer.indent;

public class GetElement implements Expression {

    private Expression sub;
    private Expression index;

    public Expression getSub() {
        return sub;
    }

    public void setSub(Expression sub) {
        this.sub = sub;
    }

    public Expression getIndex() {
        return index;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("GET_ELEMENT:");

        indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);

        indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);
    }

    @Override
    public Object interpret() {
        Object object = sub.interpret();
        Object index = this.index.interpret();
        if (isArray(object) && isNumber(index)) {
            return getValueOfArray(object, index);
        }
        if (isMap(object) && isString(index)) {
            return getValueOfMap(object, index);
        }
        return null;
    }

    @Override
    public void generate() {
        sub.generate();                                         // 배열, 맵의 정보를 알아내기 위한 피연산자 목적 코드 생성
        index.generate();                                       // 인덱스 목적 코드 생성
        writeCode(Instruction.GetElement);                      // 피연산자 스택[-2] (배열, 맵)의  피연산자 스택[-1] (인덱스) 번째 값을 가져와 다시 피연산자 스택에 push
    }
}
// 배열과 맵의 원소 참조
// array[0], map['property']
