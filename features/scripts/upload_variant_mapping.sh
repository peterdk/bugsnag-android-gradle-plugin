#!/usr/bin/env bash

if [ -z "$VARIANT_OUTPUT_NAME" ]; then
    echo VARIANT_OUTPUT_NAME environment variable is not set
    exit 1
fi

cd $APP_FIXTURE_DIR
echo "Test fixture used: $APP_FIXTURE_DIR"
./gradlew :module:clean :module:assemble -x lint uploadBugsnag${VARIANT_OUTPUT_NAME}Mapping --stacktrace
