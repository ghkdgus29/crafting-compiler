package generator;

public class Code {

    Instruction instruction;
    Object operand;

    public Code(Instruction instruction) {
        this.instruction = instruction;
    }

    public Code(Instruction instruction, Object operand) {
        this.instruction = instruction;
        this.operand = operand;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public Object getOperand() {
        return operand;
    }

    public void setOperand(Object operand) {
        this.operand = operand;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-15s", instruction));

        if (operand instanceof Integer) {
            sb.append("[" + operand + "]");
        } else if (operand instanceof Boolean) {
            sb.append(operand);
        } else if (operand instanceof Double) {
            sb.append(operand);
        } else if (operand instanceof String) {
            sb.append("\"" + operand + "\"");
        }

        return sb.toString();
    }
}
