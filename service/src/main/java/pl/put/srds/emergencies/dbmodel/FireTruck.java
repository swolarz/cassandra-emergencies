package pl.put.srds.emergencies.dbmodel;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("FireTrucks")
public class FireTruck {
    @PrimaryKeyColumn(name = "BrigadeId", ordinal = 0,  type = PrimaryKeyType.PARTITIONED)
    private int brigadeId;

    @PrimaryKeyColumn(name = "TypeId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private int typeId;

    @PrimaryKeyColumn(name = "IsAssigned", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private boolean isAssigned;

    @Column("TruckId")
    private int truckId;

    public FireTruck(
            final int brigadeId,
            final int typeId,
            final boolean isAssigned,
            final int truckId) {
        this.brigadeId = brigadeId;
        this.typeId = typeId;
        this.isAssigned = isAssigned;
        this.truckId = truckId;
    }

    public int getBrigadeId() {
        return brigadeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public int getTruckId() {
        return truckId;
    }
}
