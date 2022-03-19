package net.ancronik.cookbook.backend.data.codec;

import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Ingredient;

import javax.annotation.Nullable;

/**
 * Codec for mapping {@link Ingredient} so it can be saved/fetched to/from the database using defined udt.
 *
 * @author Nikola Presecki
 */
public class IngredientCodec extends MappingCodec<UdtValue, Ingredient> {


    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_MEASUREMENT_UNIT = "measurement_unit";

    /**
     * Constructor calling super.
     *
     * @param innerCodec codec.
     */
    public IngredientCodec(@NonNull TypeCodec<UdtValue> innerCodec) {
        super(innerCodec, GenericType.of(Ingredient.class));
    }

    @NonNull
    @Override
    public UserDefinedType getCqlType() {
        return (UserDefinedType) super.getCqlType();
    }

    @Nullable
    @Override
    protected Ingredient innerToOuter(@Nullable UdtValue udtValue) {
        if (null == udtValue) {
            return null;
        }

        return new Ingredient(udtValue.getString(KEY_NAME), udtValue.getString(KEY_QUANTITY), udtValue.getString(KEY_MEASUREMENT_UNIT));
    }

    @Nullable
    @Override
    protected UdtValue outerToInner(@Nullable Ingredient ingredient) {
        if (null == ingredient) {
            return null;
        }

        return getCqlType().newValue()
                .setString(KEY_NAME, ingredient.getName())
                .setString(KEY_QUANTITY, ingredient.getQuantity())
                .setString(KEY_MEASUREMENT_UNIT, ingredient.getMeasurementUnit());
    }
}
