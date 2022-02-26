package net.ancronik.cookbook.backend.data.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Represent measurement units for weight, size and volume both in imperial and metric unit system.
 *
 * @author Nikola Presecki
 */
@Getter
@ToString
public enum MeasurementUnit {

    // weight
    KG("kg"), G("g"), LB("lb"),
    // size
    M("m"), CM("cm"), INCH("inch"), FOOT("foot"),
    // volume
    L("l"), DL("dl"), ML("ml"), CUP("cup"),
    SPOON("spoon"), TEA_SPOON("tea_spoon"), DROP("DROP");

    private final String unit;

    MeasurementUnit(String unit) {
        this.unit = unit;
    }

}
