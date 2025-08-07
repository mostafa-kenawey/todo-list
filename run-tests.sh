#!/bin/bash

set -e

echo "Running tests and generating coverage report with Docker..."

docker-compose -f docker-compose.test.yml down --remove-orphans 2>/dev/null || true
docker-compose -f docker-compose.test.yml up --build test-runner

if [ $? -eq 0 ]; then
    echo "Tests completed successfully!"
    docker-compose -f docker-compose.test.yml up -d coverage-server
    
    echo ""
    echo "Coverage report is now available at: http://localhost:8081"
    echo ""
    echo "Test results summary:"
    echo "   - Test reports: ./target/surefire-reports/"
    echo "   - Coverage report: ./target/site/jacoco/index.html"
    echo "   - Coverage XML: ./target/site/jacoco/jacoco.xml"
    echo "   - Coverage CSV: ./target/site/jacoco/jacoco.csv"
    echo ""
    echo "To stop the coverage server, run:"
    echo "  docker-compose -f docker-compose.test.yml down"
else
    echo "Tests failed! Check the output above for details."
    exit 1
fi
