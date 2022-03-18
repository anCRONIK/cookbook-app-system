package net.ancronik.cookbook.backend.data.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

/**
 * Represent measurement units for weight, size and volume both in imperial and metric unit system.
 * <p>
 * There are two flags {@link #isImperial} and {@link #isMetric} because some units can be used for both (for instance
 * teaspoon, spoon, drop).
 * <p>
 * This should be read only.
 *
 * @author Nikola Presecki
 */
@Table("measurement_units")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MeasurementUnit implements Serializable {

    private String name;

    @PrimaryKey
    private String code;

    private String category;

    @Column("is_imperial")
    private boolean isImperial;

    @Column("is_metric")
    private boolean isMetric;


}
