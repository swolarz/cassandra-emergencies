package pl.put.srds.emergencies.dbmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.LocalDateTime;


@PrimaryKeyClass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentKey implements Serializable {
    @PrimaryKeyColumn(name = "TruckId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int truckId;

    @PrimaryKeyColumn(name = "Timestamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private LocalDateTime timestamp;

    @PrimaryKeyColumn(name = "RequestId", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String requestId;
}
