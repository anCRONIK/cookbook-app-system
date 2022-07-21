package net.ancronik.cookbook.backend.api.data.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

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
