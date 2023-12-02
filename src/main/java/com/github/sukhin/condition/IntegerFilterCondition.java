package com.github.sukhin.condition;

import org.jooq.Converter;
import org.jooq.Field;

import java.util.function.Function;

/**
 * Filter condition for field with type {@link Integer}.
 */
public class IntegerFilterCondition extends AbstractRangeFilterCondition<Integer> {
    public IntegerFilterCondition(Field<Integer> field, Converter<Integer, String> converter) {
        super(field, converter);
    }

    public IntegerFilterCondition(Field<Integer> field, Function<String, Integer> basicConverter) {
        super(field, basicConverter);
    }

    public IntegerFilterCondition(Field<Integer> field) {
        super(field, BASIC_INTEGER_CONVERTER);
    }
}
