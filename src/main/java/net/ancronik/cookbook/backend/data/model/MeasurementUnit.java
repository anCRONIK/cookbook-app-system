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
    KG("KG"), G("G"), LB("LB"),
    // size
    M("M"), CM("CM"), INCH("INCH"), FOOT("FOOT"),
    // volume
    L("L"), DL("DL"), ML("ML"), CUP("CUP"),
    SPOON("SPOON"), TEA_SPOON("TEA_SPOON"), DROP("DROP");

    private final String unit;

    MeasurementUnit(String unit) {
        this.unit = unit;
    }

}
