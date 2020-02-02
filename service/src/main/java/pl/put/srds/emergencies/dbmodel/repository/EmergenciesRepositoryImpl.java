package pl.put.srds.emergencies.dbmodel.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;
import pl.put.srds.emergencies.dbmodel.Assignment;
import pl.put.srds.emergencies.dbmodel.FireTruck;


@Repository
@Slf4j
public class EmergenciesRepositoryImpl implements EmergenciesRepository {

    private static final String MAKE_ASSIGNMENT_CQL =
            "BEGIN BATCH " +
                    "update firetrucks set assigned = true\n" +
                        "where brigadeid = ?\n" +
                            "and typeid = ?;\n" +
                    "insert into assignments (truckid, requestid, timestamp)\n" +
                        "values (?, ?, now());\n" +
            "APPLY BATCH;";

    private static final String RELEASE_ASSIGNMENT_CQL =
            "BEGIN BATCH\n" +
                    "delete from assignments\n" +
                        "where truckid = ?\n" +
                            "and timestamp = ?\n" +
                            "and requestid = ?;\n" +
                    "update firetrucks set assigned = false\n" +
                        "where brigadeid = ?\n" +
                            "and typeid = ?;\n" +
            "APPLY BATCH;";


    private final CassandraTemplate cassandraTemplate;
    private final AssignmentRepository assignmentRepository;

    private final PreparedStatement makeAssignmentStatement;
    private final PreparedStatement releaseAssignmentStatement;


    public EmergenciesRepositoryImpl(CassandraTemplate cassandraTemplate,
                                     AssignmentRepository assignmentRepository,
                                     Session session) {

        this.cassandraTemplate = cassandraTemplate;
        this.assignmentRepository = assignmentRepository;

        this.makeAssignmentStatement = session.prepare(MAKE_ASSIGNMENT_CQL);
        this.releaseAssignmentStatement = session.prepare(RELEASE_ASSIGNMENT_CQL);
    }

    @Override
    public Assignment makeAssignment(FireTruck fireTruck, String requestId) {
        BoundStatement statement = makeAssignmentStatement.bind(
                fireTruck.getKey().getBrigadeId(),
                fireTruck.getKey().getTypeId(),
                fireTruck.getTruckId(),
                requestId
        );

        boolean batchApplied = cassandraTemplate.getCqlOperations().execute(statement);

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
        Assignment assignment = assignmentRepository.findFirstByKeyTruckIdAndKeyRequestId(fireTruck.getTruckId(), requestId);

        BoundStatement statement = releaseAssignmentStatement.bind(
                fireTruck.getTruckId(),
                assignment.getKey().getTimestamp(),
                requestId,
                fireTruck.getKey().getBrigadeId(),
                fireTruck.getKey().getTypeId()
        );

        return cassandraTemplate.getCqlOperations().execute(statement);
    }
}
