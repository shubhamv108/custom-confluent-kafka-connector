package code.shubham.connectors.sink;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;

import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkConnector;

public class CustomMongoSinkConnector extends SinkConnector {

    private Map<String, String> props;

    @Override
    public void start(Map<String, String> props) {
        this.props = props;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return CustomMongoSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        return List.of(props);
    }

    @Override
    public void stop() {
        // Handle any cleanup when the connector is stopped
    }

    @Override
    public ConfigDef config() {
        // Define connector-specific configurations
        return new ConfigDef()
                .define("mongodb.uri", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "MongoDB connection URI")
                .define("mongodb.database", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "MongoDB database")
                .define("mongodb.collection", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "MongoDB collection");
    }

    @Override
    public String version() {
        return "1.0";
    }
}
