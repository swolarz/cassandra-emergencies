package pl.put.srds.emergencies.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;


@Configuration
@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Getter
    @Value("${cassandra.contactpoints}")
    private String contactPoints;

    @Getter
    @Value("${cassandra.port}")
    private int port;

    @Getter
    @Value("${cassandra.keyspace}")
    private String keyspaceName;

    @Override
    protected boolean getMetricsEnabled() {
        return false;
    }
}
