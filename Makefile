.PHONY: build test test-simple coverage-server clean help

help:
	@echo "Available targets:"
	@echo "  build         - Build the application JAR file"
	@echo "  test          - Run tests and start coverage server (comprehensive approach)"
	@echo "  test-simple   - Run tests using simple Docker approach"
	@echo "  coverage-server - Start only the coverage server (after tests have run)"
	@echo "  clean         - Clean up Docker containers and volumes"
	@echo "  help          - Show this help message"

build:
	@echo "Building the application..."
	mvn clean compile package -DskipTests
	@echo "Build completed! JAR file available at: ./target/todo-list-0.0.1-SNAPSHOT.jar"

test:
	@echo "Running tests and starting coverage server..."
	docker-compose -f docker-compose.test.yml down --remove-orphans 2>/dev/null || true
	docker-compose -f docker-compose.test.yml up --build

test-simple:
	@echo "Running tests with simple Docker approach..."
	@echo "Cleaning target directory..."
	rm -rf target
	mkdir -p target
	docker build -f Dockerfile.simple-test -t todo-test .
	docker run --rm -v $$(pwd)/target:/app/target todo-test
	@echo "Tests completed! Coverage report available at: ./target/site/jacoco/index.html"

coverage-server:
	@echo "Starting coverage server..."
	docker-compose -f docker-compose.test.yml up -d coverage-server
	@echo "Coverage report available at: http://localhost:8081"

clean:
	@echo "Cleaning up Docker containers and build artifacts..."
	docker-compose -f docker-compose.test.yml down --remove-orphans --volumes 2>/dev/null || true
	docker rmi todo-test 2>/dev/null || true
	rm -rf target
	@echo "Cleanup completed!"
