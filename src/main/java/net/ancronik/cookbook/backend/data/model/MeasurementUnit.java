package net.ancronik.cookbook.backend.data.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Represent measurement units for weight, size and volume both in imperial and metric unit system.
 */
@Getter
@ToString
public enum MeasurementUnit {

    // weight
    KG("kg"), G("g"), LB("lb"),
    // size
    CM("cm"), INCH("inch"),
    // volume
    DL("dl"), ML("ml"), CUP("cup"), SPOON("spoon"), DROP("DROP");

    private final String unit;

    MeasurementUnit(String unit) {
        this.unit = unit;
    }

}
