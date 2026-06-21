#!/bin/sh

echo "Running Spotless check..."

./gradlew spotlessCheck

if [ $? -ne 0 ]; then
  echo ""
  echo "❌ Spotless check failed."
  echo "Run: ./gradlew spotlessApply"
  exit 1
fi

echo "✅ Spotless check passed."