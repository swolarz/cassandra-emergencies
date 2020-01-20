package pl.put.srds.emergencies.dbmodel;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Table("Assignments")
public class Assignment {
    @PrimaryKeyColumn(name = "TruckId", type = PrimaryKeyType.PARTITIONED)
    private int truckId;

    @PrimaryKeyColumn(name = "RequestId", type = PrimaryKeyType.PARTITIONED)
    private UUID requestId;

    @Column("Timestamp")
    private Date timestamp;

    public Assignment(
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
