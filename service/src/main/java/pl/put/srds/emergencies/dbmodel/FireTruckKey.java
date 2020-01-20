package pl.put.srds.emergencies.dbmodel;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@PrimaryKeyClass
public class FireTruckKey implements Serializable {
    @PrimaryKeyColumn(name = "BrigadeId", ordinal = 0,  type = PrimaryKeyType.PARTITIONED)
    private int brigadeId;

    @PrimaryKeyColumn(name = "TypeId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private int typeId;

    @PrimaryKeyColumn(name = "IsAssigned", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private boolean isAssigned;

    public FireTruckKey(
            final int brigadeId,
            final int typeId,
            final boolean isAssigned) {
        this.setBrigadeId(brigadeId);
        this.setTypeId(typeId);
        this.setAssigned(isAssigned);
    }

    public int getBrigadeId() {
        return brigadeId;
    }

    public void setBrigadeId(int brigadeId) {
        this.brigadeId = brigadeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
