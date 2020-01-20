package pl.put.srds.emergencies.dbmodel.repositories.interfaces;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.put.srds.emergencies.dbmodel.FireTruck;
import pl.put.srds.emergencies.dbmodel.FireTruckKey;

import java.util.List;

public interface FireTruckRepository extends CassandraRepository<FireTruck, FireTruckKey> {
    List<FireTruck> findByKeyBrigadeIdAndTypeIdAndIsAssigned(int brigadeId, int typeId, boolean sAssigned)
;}
