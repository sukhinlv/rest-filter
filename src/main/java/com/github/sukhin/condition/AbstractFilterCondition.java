package com.github.sukhin.condition;

import org.jooq.Converter;
import org.jooq.Field;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public abstract class AbstractFilterCondition<T> implements FilterCondition {
    protected final Field<T> field;
    protected final Converter<T, String> converter;
    protected final Function<String, T> basicConverter;

    /**
     * Creates a new filtering condition for a field with a converter, see {@link Converter}.
     *
     * @param field     field {@link Field}
     * @param converter converter to convert text representation to value, see {@link Converter}.
     */
    protected AbstractFilterCondition(Field<T> field, Converter<T, String> converter) {
        requireNonNull(field);
        requireNonNull(converter);
        this.field = field;
        this.converter = converter;
        this.basicConverter = null;
    }

    /**
     * Creates a new filtering condition for a field with a converter, see {@link Function}.
     *
     * @param field          field {@link Field}
     * @param basicConverter converter to convert text representation to value, see {@link Function}.
     */
    protected AbstractFilterCondition(Field<T> field, Function<String, T> basicConverter) {
        requireNonNull(field);
        requireNonNull(basicConverter);
        this.field = field;
        this.converter = null;
        this.basicConverter = basicConverter;
    }

    /**
     * Returns the result of converting a string value.
     *
     * @param stringValue string value
     * @return converted typed value
     */
    protected Optional<T> getConvertedValue(String stringValue) {
        try {
            if (nonNull(converter)) {
                return Optional.of(converter.to(stringValue));
            } else if (nonNull(basicConverter)) {
                return Optional.of(basicConverter.apply(stringValue));
            }
        } catch (Exception e) {
            // Do nothing, empty value will be returned
        }
        return Optional.empty();
    }
}
