package com.github.sukhin.condition;

import org.jooq.Converter;
import org.jooq.Field;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * Filter condition for field with type {@link LocalDateTime}.
 */
public class LocalDateTimeFilterCondition extends
        AbstractRangeFilterCondition<LocalDateTime> {
    public LocalDateTimeFilterCondition(Field<LocalDateTime> field, Converter<LocalDateTime, String> converter) {
        super(field, converter);
    }

    public LocalDateTimeFilterCondition(Field<LocalDateTime> field, Function<String, LocalDateTime> basicConverter) {
        super(field, basicConverter);
    }

    public LocalDateTimeFilterCondition(Field<LocalDateTime> field) {
        super(field, BASIC_LOCAL_DATE_TIME_CONVERTER);
    }
}
