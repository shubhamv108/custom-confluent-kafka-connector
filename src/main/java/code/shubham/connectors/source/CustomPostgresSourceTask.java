package code.shubham.connectors.source;

import code.shubham.commons.utils.PostgresUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomPostgresSourceTask extends SourceTask {

    private String jdbcUrl;
    private String user;
    private String password;
    private String query;
    private String kafkaTopic;

    private Connection connection;

    @Override
    public void start(Map<String, String> props) {
        // Initialize PostgreSQL connection and configurations
        jdbcUrl = props.get("postgres.url");
        user = props.get("postgres.user");
        password = props.get("postgres.password");
        query = props.get("postgres.query");
        kafkaTopic = props.get("kafka.topic");

        log.info(String.format("Query: %s", query));

        try {
            connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }

    @Override
    public List<SourceRecord> poll() {
        List<SourceRecord> records = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

//            PostgresUtil.log(rs);

             while (rs.next()) {
                Map<String, Object> sourcePartition = Map.of("db", jdbcUrl);
                Map<String, Object> sourceOffset = Map.of("last_row_id", rs.getInt("id"));

                Map<String, Object> value = new HashMap<>();
                int columnCount = rs.getMetaData().getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object columnValue = rs.getObject(i);

                    // Convert java.sql.Timestamp to a string
                    if (columnValue instanceof Timestamp timestamp) {
                        columnValue = timestamp.getTime(); // for epoch
                    }

                    if (columnValue == null) {
                        columnValue = ""; // Replace null with an empty string
                    }

                    value.put(columnName, columnValue);
                }

                records.add(new SourceRecord(
                        sourcePartition,
                        sourceOffset,
                        kafkaTopic,
                        null, // key schema
                        null, // key value
                        null, // value schema
                        value
                ));
             }

        } catch (Exception e) {
            System.err.println("Error fetching data from PostgreSQL: " + e.getMessage());
        }

        return records;
    }

    @Override
    public void stop() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.err.println(String.format("Failed to close PostgreSQL connection: %s", e.getMessage()));
            }
        }
    }

    @Override
    public String version() {
        return "1.0";
    }
}
