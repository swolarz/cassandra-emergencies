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
    private final AssignmentRepository assignmentRepository;

    @Override
    public Assignment makeAssignment(FireTruck fireTruck, String requestId) {
        String batchCql =
                "BEGIN BATCH " +
                        "update firetrucks set isassigned = true\n" +
                            "where brigadeid = ?\n" +
                                "and typeid = ?;\n" +
                        "insert into assignments (truckid, requestid, timestamp)\n" +
                            "values (?, ?, toTimestamp(now()));\n" +
                "APPLY BATCH;";

        boolean batchApplied = cassandraTemplate.getCqlOperations().execute(
                batchCql,
                fireTruck.getKey().getBrigadeId(),
                fireTruck.getKey().getTypeId(),
                fireTruck.getTruckId(),
                requestId
        );

        if (!batchApplied)
            return null;

        return assignmentRepository.findFirstByKeyTruckIdAndKeyRequestId(fireTruck.getTruckId(), requestId);
    }

    @Override
    public boolean hasFirstAssignment(Assignment assignment) {
        Assignment firstAssignment = assignmentRepository.findFirstByKeyTruckId(assignment.getKey().getTruckId());
        return (firstAssignment.getKey().getRequestId().equals(assignment.getKey().getRequestId()));
    }

    @Override
    public void rollbackAssignment(Assignment assignment) {
        cassandraTemplate.delete(assignment);
    }

    @Override
    public boolean releaseAssignment(FireTruck fireTruck, String requestId) {
        String batchCql =
                "BEGIN BATCH\n" +
                        "delete from assignments\n" +
                            "where truckid = ?\n" +
                                "and requestid = ?;\n" +
                        "update firetrucks set isassigned = false\n" +
                            "where brigadeid = ?\n" +
                                "and typeid = ?;\n" +
                "APPLY BATCH;";

        return cassandraTemplate.getCqlOperations().execute(
                batchCql,
                fireTruck.getTruckId(),
                requestId,
                fireTruck.getKey().getBrigadeId(),
                fireTruck.getKey().getTypeId()
        );
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
