package pl.put.srds.emergencies.dbmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@PrimaryKeyClass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireTruckKey implements Serializable {
    @PrimaryKeyColumn(name = "BrigadeId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int brigadeId;

    @PrimaryKeyColumn(name = "TypeId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private int typeId;
}
