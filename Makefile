# Makefile for building Fat JAR

export CONFLUENT_HOME=CONFLUENT_HOME


# Path to Gradle wrapper
GRADLE = ./gradlew

# Default target
all: build

# Target for building the fat JAR
build:
	$(GRADLE) clean buildFatJar

# Clean target to remove any previous builds
clean:
	$(GRADLE) clean

# Target to check the Gradle build environment
check:
	$(GRADLE) --version

setup: build
	docker-compose up -d

# Run Kafka Connect
run-connect:
	$(CONFLUENT_HOME)/bin/connect-distributed $(CONFLUENT_HOME)/etc/kafka/connect-distributed.properties

# Run the build, setup, and run-connect targets
run: build setup run-connect

teardown:
	docker-compose down --volumes --remove-orphans

# Help target to display makefile usage
help:
	@echo "Available targets:"
	@echo "  make build  - Build the Fat JAR"
	@echo "  make clean  - Clean the build directory"
	@echo "  make check  - Check Gradle version"
	@echo "  make setup  - Start services with Docker Compose"
	@echo "  make run-connect  - Start Kafka Connect in distributed mode"
	@echo "  make run          - Run build, setup, and run-connect"
	@echo "  make teardown     - Stop and remove Docker containers, networks, and volumes"
	@echo "  make help   - Show this message"
