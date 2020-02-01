package pl.put.srds.emergencies.dbmodel.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.put.srds.emergencies.dbmodel.Assignment;
import pl.put.srds.emergencies.dbmodel.AssignmentKey;


@Repository
public interface AssignmentRepository extends CassandraRepository<Assignment, AssignmentKey> {

    @AllowFiltering
    Assignment findFirstByKeyTruckId(int truckId);

    @AllowFiltering
    Assignment findFirstByKeyTruckIdAndKeyRequestId(int truckId, String requestId);
}
