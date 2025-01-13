package code.shubham.connectors.source;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.List;
import java.util.Map;

public class CustomPostgresSourceConnector extends SourceConnector {

    private Map<String, String> config;

    @Override
    public void start(Map<String, String> props) {
        this.config = props; // Save configuration
    }

    @Override
    public Class<? extends Task> taskClass() {
        return CustomPostgresSourceTask.class; // Link to the task class
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        // Pass configuration to tasks
        return List.of(config);
    }

    @Override
    public void stop() {
        // Handle any cleanup when the connector is stopped
    }

    @Override
    public ConfigDef config() {
        // Define connector-specific configurations
        return new ConfigDef()
                .define("postgres.url", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "PostgreSQL JDBC URL")
                .define("postgres.user", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "PostgreSQL User")
                .define("postgres.password", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "PostgreSQL Password")
                .define("postgres.query", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Query to fetch data")
                .define("kafka.topic", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Kafka topic to push data");
    }

    @Override
    public String version() {
        return "1.0";
    }
}

