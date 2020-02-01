package pl.put.srds.emergencies.dbmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("FireTrucks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireTruck {
    @PrimaryKey
    private FireTruckKey key;

    @Column("Assigned")
    private boolean assigned;

    @Column("TruckId")
    private int truckId;
}
