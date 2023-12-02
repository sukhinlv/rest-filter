package com.github.sukhin.condition;

import com.github.sukhin.Qualifier;
import org.jooq.Condition;
import org.jooq.Converter;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Optional;
import java.util.function.Function;

import static com.github.sukhin.Qualifier.EQUAL;
import static com.github.sukhin.Qualifier.GREATER;
import static com.github.sukhin.Qualifier.GREATER_EQUAL;
import static com.github.sukhin.Qualifier.LESS;
import static com.github.sukhin.Qualifier.LESS_EQUAL;
import static com.github.sukhin.Qualifier.NOT_EQUAL;

public abstract class AbstractRangeFilterCondition<T> extends AbstractFilterCondition<T> {
    /**
     * Creates a new filtering condition for a field with a converter, see {@link Converter}.
     *
     * @param field     field {@link Field}
     * @param converter converter to convert text representation to value, see {@link Converter}.
     */
    protected AbstractRangeFilterCondition(Field<T> field, Converter<T, String> converter) {
        super(field, converter);
    }

    /**
     * Creates a new filtering condition for a field with a converter, see {@link Function}.
     *
     * @param field          field {@link Field}
     * @param basicConverter converter to convert text representation to value, see {@link Function}.
     */
    protected AbstractRangeFilterCondition(Field<T> field, Function<String, T> basicConverter) {
        super(field, basicConverter);
    }

    @Override
    public Condition getCondition(Qualifier qualifier, String stringValue) {
        Optional<T> optionalValue = getConvertedValue(stringValue);
        if (optionalValue.isEmpty()) {
            return DSL.noCondition();
        }

        T value = optionalValue.get();

        // The order in which conditions are checked matters
        if (EQUAL == qualifier) {
            return field.equal(value);
        } else if (NOT_EQUAL == qualifier) {
            return field.notEqual(value);
        } else if (LESS_EQUAL == qualifier) {
            return field.lessOrEqual(value);
        } else if (GREATER_EQUAL == qualifier) {
            return field.greaterOrEqual(value);
        } else if (LESS == qualifier) {
            return field.lessThan(value);
        } else if (GREATER == qualifier) {
            return field.greaterThan(value);
        }
        return DSL.noCondition();
    }
}
