package me.zhacked.faketab.helper;

import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeLiteralHelper {

    public static <T> TypeLiteral<T> getType(Class<T> clazz) {
        return TypeLiteral.get(clazz);
    }

    @SuppressWarnings("rawtypes")
    public static TypeLiteral getType(Type type) {
        return TypeLiteral.get(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeLiteral<T> getParameterizedTypeLiteral(Class<T> clazz, Class<?>... typeParameters) {
        ParameterizedType parameterizedType = Types.newParameterizedType(clazz, typeParameters);

        return getType(parameterizedType);
    }
}
