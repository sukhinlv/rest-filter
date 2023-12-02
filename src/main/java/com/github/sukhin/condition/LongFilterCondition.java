package com.github.sukhin.condition;

import org.jooq.Converter;
import org.jooq.Field;

import java.util.function.Function;

/**
 * Filter condition for field with type {@link Long}.
 */
public class LongFilterCondition extends AbstractRangeFilterCondition<Long> {
    public LongFilterCondition(Field<Long> field, Converter<Long, String> converter) {
        super(field, converter);
    }

    public LongFilterCondition(Field<Long> field, Function<String, Long> basicConverter) {
        super(field, basicConverter);
    }

    public LongFilterCondition(Field<Long> field) {
        super(field, BASIC_LONG_CONVERTER);
    }
}
