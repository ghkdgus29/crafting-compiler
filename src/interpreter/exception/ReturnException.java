package interpreter.exception;

public class ReturnException extends RuntimeException {

    private Object result;

    public ReturnException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
