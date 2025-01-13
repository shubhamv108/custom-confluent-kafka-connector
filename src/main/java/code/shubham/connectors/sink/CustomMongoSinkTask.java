package code.shubham.connectors.sink;

import code.shubham.commons.utils.JsonUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.bson.Document;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class CustomMongoSinkTask extends SinkTask {

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    @Override
    public void start(Map<String, String> props) {
        // Initialize MongoDB client
        String uri = props.get("mongodb.uri");
        String database = props.get("mongodb.database");
        String collectionName = props.get("mongodb.collection");

        mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase(database);
        collection = db.getCollection(collectionName);
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        for (SinkRecord record : records) {
            try {
                log.info("Sink Record Key: {}", record.key());
                log.info("Sink Record Value: {}", record.value());

                // Convert Kafka record value to MongoDB document
                Document document = Document.parse(JsonUtils.as(record.value()));
                collection.insertOne(document);
            } catch (Exception e) {
                // Log errors
                System.err.println(String.format("Failed to write record to MongoDB: %s", e.getMessage()));
            }
        }
    }

    @Override
    public void stop() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public String version() {
        return "1.0";
    }
}
