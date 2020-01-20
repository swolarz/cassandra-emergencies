package pl.put.srds.emergencies.dbmodel;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("Assignments")
public class Assignment {

    @PrimaryKey
    private AssignmentKey key;

    public Assignment(
            final AssignmentKey key) {
        this.setKey(key);
    }

    public AssignmentKey getKey() {
        return key;
    }

    public void setKey(AssignmentKey key) {
        this.key = key;
    }
}
