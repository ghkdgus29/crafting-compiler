package interpreter;

import parser.node.statement.Function;

import java.util.List;
import java.util.Map;

import static interpreter.BuiltInFunctionTable.builtinFunctionTable;

public class Datatype {

    public static Boolean isNull(Object value) {
        return value == null;
    }

    public static Boolean isTrue(Object value) {
        return isBoolean(value) && toBoolean(value);
    }

    public static Boolean isFalse(Object value) {
        return isBoolean(value) && (toBoolean(value) == false);
    }

    public static Boolean isBoolean(Object value) {
        return value instanceof Boolean;
    }

    public static Boolean toBoolean(Object value) {
        return (Boolean) value;
    }

    public static Boolean isNumber(Object value) {
        return value instanceof Double;
    }

    public static Double toNumber(Object value) {
        return (Double) value;
    }

    public static Boolean isString(Object value) {
        return value instanceof String;
    }

    public static String toString(Object value) {
        return (String) value;
    }

    public static Boolean isArray(Object value) {
        return value instanceof List;
    }

    public static List toArray(Object value) {
        return (List) value;
    }

    public static Object getValueOfArray(Object object, Object index) {
        int i = toNumber(index).intValue();
        if (i >= 0 && i < toArray(object).size()) {
            return toArray(object).get(i);
        }

        return null;
    }

    public static Object setValueOfArray(Object object, Object index, Object value) {
        int i = toNumber(index).intValue();
        if (i >= 0 && i < toArray(object).size()) {
            toArray(object).set(i, value);
        }
        return value;
    }

    public static Boolean isMap(Object value) {
        return value instanceof Map;
    }

    public static Map toMap(Object value) {
        return (Map) value;
    }

    public static Object getValueOfMap(Object object, Object key) {
        if (toMap(object).containsKey(toString(key))) {
            return toMap(object).get(toString(key));
        }
        return null;
    }

    public static Object setValueOfMap(Object object, Object key, Object value) {
        toMap(object).put(toString(key), value);
        return value;
    }

    public static Boolean isFunction(Object value) {
        return value instanceof Function;
    }

    public static Function toFunction(Object value) {
        return (Function) value;
    }

    public static Boolean isBuiltinFunction(Object value) {
        if (isString(value)) {
            return builtinFunctionTable.containsKey(toString(value));
        }

        return false;
    }

    public static java.util.function.Function<List<Object>, Object> toBuiltinFunction(Object value) {
        return builtinFunctionTable.get(toString(value));
    }

}
