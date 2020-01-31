package pl.put.srds.emergencies.dbmodel.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.put.srds.emergencies.dbmodel.FireTruck;
import pl.put.srds.emergencies.dbmodel.FireTruckKey;


public interface FireTruckRepository extends CassandraRepository<FireTruck, FireTruckKey> {

    @AllowFiltering
    FireTruck findFirstByKeyBrigadeIdAndKeyTypeIdAndAssigned(int brigadeId, int typeId, boolean assigned);
}
