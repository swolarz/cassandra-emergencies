package pl.put.srds.emergencies.dbmodel;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("FireTrucks")
public class FireTruck {
    @PrimaryKey
    private FireTruckKey key;

    @Column("TruckId")
    private int truckId;

    public FireTruck(
            final FireTruckKey key,
            final int truckId) {
        this.setKey(key);
        this.setTruckId(truckId);
    }

    public FireTruckKey getKey() {
        return key;
    }

    public void setKey(FireTruckKey key) {
        this.key = key;
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }
}
