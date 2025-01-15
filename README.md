# Setup
Download confluent-connect.
make setup
$(CONFLUENT_HOME)/bin/connect-distributed $(CONFLUENT_HOME)/etc/kafka/connect-distributed.properties

# Run
## Add Schema
```
    curl --location 'http://localhost:8081/subjects/persons-value/versions' \
        --header 'Content-Type: application/vnd.schemaregistry.v1+json' \
        --data '{
            "schema": "{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"code.shubham\",\"fields\":[{\"name\":\"id\",\"type\":\"int\"},{\"name\":\"first_name\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"middle_name\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"last_name\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"full_name\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"email\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"mobile\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"uid\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"pan\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"uan\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"passport\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"instagram\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"twitter\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"facebook\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"snapchat\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"github\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"hackerrank\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"hackernews\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"created_at\",\"type\":\"long\"},{\"name\":\"updated_at\",\"type\":\"long\"}]}"
        }'
```
## Curl to configure custom connectors
### Source
```
    curl --location 'http://localhost:8083/connectors' \
    --header 'Content-Type: application/json' \
    --data '{
      "name": "custom-postgres-source-connector",
      "config": {
        "connector.class": "code.shubham.connectors.source.CustomPostgresSourceConnector",
        "tasks.max": "1",
        "postgres.url": "jdbc:postgresql://localhost:5432/info",
        "postgres.user": "postgres",
        "postgres.password": "postgres",
        "postgres.query": "SELECT * FROM persons",
        "kafka.topic": "persons"
      }
    }'
```

### Sink
```
    curl --location 'http://localhost:8083/connectors' \
    --header 'Content-Type: application/json' \
    --data '{
        "name": "custom-mongo-sink-connector",
        "config": {
            "connector.class": "code.shubham.connectors.sink.CustomMongoSinkConnector",
            "tasks.max": "1",
            "topics": "persons",
            "mongodb.uri": "mongodb://localhost:27017",
            "mongodb.database": "sink",
            "mongodb.collection": "persons"
        }
    }'
```

# Teardown
```Ctrl-Shift+c``` to stop connect service
```make teardown```


# Build
```
    ./gradlew buildFatJar
```

# cp connector
```
    mkdir -p $CONFLUENT_HOME/share/custom && cp build/libs/custom-confluent-kafka-connector-1.0.0.jar $CONFLUENT_HOME/share/custom/custom-confluent-kafka-connector-1.0.0.jar
```

# Edit connect-distributed.properties
```
    vim $CONFLUENT_HOME/etc/kafka/connect-distributed.properties
```
## Edit plugin.path
Add path to custom connectors jar
```
    plugin.path=/usr/share/java,/home/shubham/confluent-7.8.0/share/custom/custom-confluent-kafka-connector-1.0.0.jar
```

# Postgres - Run
```
    docker run --name postgres \
        -p 5432:5432 \
        -e POSTGRES_PASSWORD=postgres \
        -e POSTGRES_DB=info \
        -v $(pwd)/src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql \
        -d postgres
```

# Mongo - Run
```
    docker run --name mongodb -p 27017:27017 -d mongodb/mongodb-community-server:latest
```

# Kafka - Run
```
    docker run -d -p 9092:9092 --name broker \
    -e auto.create.topics.enable=true \
    apache/kafka-native:latest
```

# Kafka - Confluent - SchemaRegistry
```
    docker run -d --name schema-registry \
      -e SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS=PLAINTEXT://kafka:9092 \
      -e SCHEMA_REGISTRY_HOST_NAME=schema-registry \
      -e SCHEMA_REGISTRY_LISTENERS=http://0.0.0.0:8081 \
      confluentinc/cp-schema-registry:latest
```

# Kafka - Connect - Distributed - Run
```
    $CONFLUENT_HOME/bin/connect-distributed $CONFLUENT_HOME/etc/kafka/connect-distributed.properties
```

