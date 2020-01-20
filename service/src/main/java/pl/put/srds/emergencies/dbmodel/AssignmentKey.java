package pl.put.srds.emergencies.dbmodel;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@PrimaryKeyClass
public class AssignmentKey implements Serializable {
    @PrimaryKeyColumn(name = "TruckId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int truckId;

    @PrimaryKeyColumn(name = "RequestId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private UUID requestId;

    @PrimaryKeyColumn(name = "Timestamp", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private Date timestamp;

    public AssignmentKey(
            final int truckId,
            final UUID requestId,
            final Date timestamp) {
        this.truckId = truckId;
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
