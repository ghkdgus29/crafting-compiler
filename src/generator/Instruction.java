package generator;

public enum Instruction {

    Exit,
    Call, Alloca, Return,
    Jump, ConditionJump,
    Print, PrintLine,

    LogicalOr, LogicalAnd,
    Add, Subtract,
    Multiply, Divide, Modulo,
    Equal, NotEqual,
    LessThan, GreaterThan,
    LessOrEqual, GreaterOrEqual,
    Absolute, ReverseSign,

    GetElement, SetElement,
    GetGlobal, SetGlobal,
    GetLocal, SetLocal,

    PushNull, PushBoolean,
    PushNumber, PushString,
    PushArray, PushMap,
    PopOperand,
}


