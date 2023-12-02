package com.github.sukhin.condition;

import com.github.sukhin.Qualifier;
import org.jooq.Condition;
import org.jooq.Converter;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Optional;
import java.util.function.Function;

import static com.github.sukhin.Qualifier.EQUAL;
import static com.github.sukhin.Qualifier.NOT_EQUAL;

/**
 * Filter condition for field with type {@link Boolean}.
 */
public class BooleanFilterCondition extends AbstractFilterCondition<Boolean> {
    public BooleanFilterCondition(Field<Boolean> field, Converter<Boolean, String> converter) {
        super(field, converter);
    }

    public BooleanFilterCondition(Field<Boolean> field, Function<String, Boolean> basicConverter) {
        super(field, basicConverter);
    }

    public BooleanFilterCondition(Field<Boolean> field) {
        super(field, BASIC_BOOLEAN_CONVERTER);
    }

    @Override
    public Condition getCondition(Qualifier qualifier, String stringValue) {
        Optional<Boolean> optionalValue = getConvertedValue(stringValue);
        if (optionalValue.isEmpty()) {
            return DSL.noCondition();
        }

        Boolean value = optionalValue.get();

        // The order in which conditions are checked matters
        if (EQUAL == qualifier) {
            return field.equal(value);
        } else if (NOT_EQUAL == qualifier) {
            return field.notEqual(value);
        }
        return DSL.noCondition();
    }
}
