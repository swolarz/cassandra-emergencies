package pl.put.srds.emergencies.dbmodel.repositories.interfaces;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.put.srds.emergencies.dbmodel.Assignment;
import pl.put.srds.emergencies.dbmodel.AssignmentKey;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends CassandraRepository<Assignment, AssignmentKey> {
    List<Assignment> findByKeyTruckIdAndRequestId(int truckId, UUID requestId);
}
