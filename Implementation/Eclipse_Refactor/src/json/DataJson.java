package json;

import java.util.List;

import ast.Entity.Field;

public class DataJson extends APIJson{
    public DataJson(String className, String methodName, String methodKey, List<Field> readList, List<Field> writeList, List<Field> nativeList) {
        super(className, methodName, methodKey, readList, writeList, nativeList);
    }
}
