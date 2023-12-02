package com.github.sukhin.condition;

import org.jooq.Converter;
import org.jooq.Field;

import java.util.function.Function;

/**
 * Filter condition for field with type {@link Short}.
 */
public class ShortFilterCondition extends AbstractRangeFilterCondition<Short> {
    public ShortFilterCondition(Field<Short> field, Converter<Short, String> converter) {
        super(field, converter);
    }

    public ShortFilterCondition(Field<Short> field, Function<String, Short> basicConverter) {
        super(field, basicConverter);
    }

    public ShortFilterCondition(Field<Short> field) {
        super(field, BASIC_SHORT_CONVERTER);
    }
}
