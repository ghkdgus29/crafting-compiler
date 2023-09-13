package parser.node.expression;

import generator.Instruction;

import static generator.Generator.writeCode;
import static interpreter.Datatype.*;
import static parser.Printer.indent;

public class SetElement implements Expression {

    private Expression sub;
    private Expression index;
    private Expression value;

    public void setSub(Expression sub) {
        this.sub = sub;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    @Override
    public void print(int depth) {
        indent(depth);
        System.out.println("SET_ELEMENT:");

        indent(depth + 1);
        System.out.println("SUB:");
        sub.print(depth + 2);

        indent(depth + 1);
        System.out.println("INDEX:");
        index.print(depth + 2);

        indent(depth + 1);
        System.out.println("VALUE:");
        value.print(depth + 2);
    }

    @Override
    public Object interpret() {
        Object object = sub.interpret();
        Object index = this.index.interpret();
        Object value = this.value.interpret();

        if (isArray(object) && isNumber(index)) {
            return setValueOfArray(object, index, value);
        }
        if (isMap(object) && isString(index)) {
            return setValueOfMap(object, index, value);
        }
        return null;
    }

    @Override
    public void generate() {
        value.generate();                               // 대입할 값 목적 코드 생성
        sub.generate();
        index.generate();
        writeCode(Instruction.SetElement);              // 피연산자 스택[-2] (배열, 맵) 의 피연산자 스택[-1] (인덱스) 번째 값을 피연산자 스택[-3] 으로 수정
    }
}
// 배열과 맵의 원소 수정
// arr[0] = 3; map['property'] = 3;

