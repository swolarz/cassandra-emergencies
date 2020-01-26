package pl.put.srds.emergencies.dbmodel.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;
import pl.put.srds.emergencies.dbmodel.Assignment;
import pl.put.srds.emergencies.dbmodel.FireTruck;


@Repository
@RequiredArgsConstructor
@Slf4j
public class EmergenciesRepositoryImpl implements EmergenciesRepository {

    private final CassandraTemplate cassandraTemplate;

    @Override
    public Assignment makeAssignment(FireTruck fireTruck, String requestId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasFirstAssignment(Assignment assignment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean rollbackAssignment(Assignment assignment) {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean releaseAssignment(FireTruck fireTruck, String requestId) {
        throw new UnsupportedOperationException();
    }

    //    public Assignment makeAssignment(int truckId, int truckTypeId, int brigadeId, int requestId) {
//        InsertOptions options = InsertOptions.builder().build();
//        WriteResult result = cassandraTemplate.batchOps()
//                .insert(new Assignment(), options)
//                .execute();
//        return null;
//    }

    //    public Assignment makeAssignment(int truckId, int requestId) {
//
//        try {
//            EntityWriteResult<Assignment> result = cassandraTemplate.insert(new Assignment(), options);
//            return result.getEntity();
//        }
//        catch (DataAccessException e) {
//            log.warn("Failed to make assignment", e);
//            return null;
//        }
//
//        String rawInsert = "insert into assignments (truckid, requestid, timestamp) values (?, ?, toTimestamp(now())";
//        return cqlTemplate.execute(rawInsert, truckId, requestId);
//    }
}
