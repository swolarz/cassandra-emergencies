package pl.put.srds.emergencies.dbmodel.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.put.srds.emergencies.dbmodel.FireTruck;
import pl.put.srds.emergencies.dbmodel.FireTruckKey;

import java.util.List;

public interface FireTruckRepository extends CassandraRepository<FireTruck, FireTruckKey> {
    FireTruck findFirstByKeyBrigadeIdAndKeyTypeIdAndKeyIsAssigned(int brigadeId, int typeId, boolean isAssigned);
}
