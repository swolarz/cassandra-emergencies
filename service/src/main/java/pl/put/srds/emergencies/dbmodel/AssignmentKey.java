package pl.put.srds.emergencies.dbmodel;

import com.datastax.driver.core.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;


@PrimaryKeyClass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentKey implements Serializable {
    @PrimaryKeyColumn(name = "TruckId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int truckId;

    @PrimaryKeyColumn(name = "Timestamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = DataType.Name.TIMEUUID)
    private UUID timestamp;

    @PrimaryKeyColumn(name = "RequestId", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String requestId;
}
