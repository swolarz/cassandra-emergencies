package pl.put.srds.emergencies.dbmodel.repository;

import pl.put.srds.emergencies.assigners.exceptions.UnableToReleaseException;
import pl.put.srds.emergencies.dbmodel.Assignment;
import pl.put.srds.emergencies.dbmodel.FireTruck;

public interface EmergenciesRepository {

    Assignment makeAssignment(FireTruck fireTruck, String requestId);
    boolean hasFirstAssignment(Assignment assignment);

    void rollbackAssignment(Assignment assignment);
    boolean releaseAssignment(FireTruck fireTruck, String requestId) throws UnableToReleaseException;
}
