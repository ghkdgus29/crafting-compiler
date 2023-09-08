package interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static interpreter.Datatype.*;

public class BuiltInFunctionTable {

    public static Map<String, Function<List<Object>, Object>> builtinFunctionTable = initialize();

    private static Map<String, Function<List<Object>, Object>> initialize() {
        Map<String, Function<List<Object>, Object>> result = new HashMap<>();

        result.put("length", params -> {
            if (params.size() == 1 && isArray(params.get(0))) {
                return toArray(params.get(0)).size();
            }
            if (params.size() == 1 && isMap(params.get(0))) {
                return toMap(params.get(0)).size();
            }
            return 0.0;
        });

        result.put("push", params -> {
            if (params.size() == 2 && isArray(params.get(0))) {
                toArray(params.get(0)).add(params.get(1));
                return params.get(0);
            }
            return null;
        });

        result.put("pop", params -> {
            if (params.size() == 1 && isArray(params.get(0)) && toArray(params.get(0)).size() != 0) {
                Object pop = toArray(params.get(0)).get(toArray(params.get(0)).size() - 1);
                toArray(params.get(0)).remove(toArray(params.get(0)).size() - 1);

                return pop;
            }
            return null;
        });

        result.put("erase", params -> {
            if (params.size() == 2 && isMap(params.get(0)) && isString(params.get(1)) && toMap(params.get(0)).containsKey(Datatype.toString(params.get(1)))) {
                Object erase = toMap(params.get(0)).get(Datatype.toString(params.get(1)));
                toMap(params.get(0)).remove(Datatype.toString(params.get(1)));

                return erase;
            }
            return null;
        });

        result.put("sqrt", params -> Math.sqrt(toNumber(params.get(0))));

        return result;
    }
}
