package pl.put.srds.emergencies.dbmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("Assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {

    @PrimaryKey
    private AssignmentKey key;

    public UUID getRequestId() {
        return key.getRequestId();
    }

    public void setRequestId(UUID requestId) {
        key.setRequestId(requestId);
    }
}
