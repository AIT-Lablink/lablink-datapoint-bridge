#!/bin/sh
set -eu

# Derive URLs at container start so -e LLCONFIG=... takes effect.
LOGGER_CONFIG="-Dlog4j.configurationFile=${LLCONFIG}ait.all.log4j2"
CONFIG_FILE_URI="${LLCONFIG}${DPB_CONFIG}"

# IPv4 configuration.
IPV4_CONFIG=-Djava.net.preferIPv4Stack=true

echo LLCONFIG: $LLCONFIG
echo LOGGER_CONFIG: $LOGGER_CONFIG
echo MEMORY_FLAG: $MEMORY_FLAG
echo DPB_JAR_FILE: $DPB_JAR_FILE
echo CONFIG_FILE_URI: $CONFIG_FILE_URI

# Start the data point bridge. Extra arguments are passed through.
exec java $IPV4_CONFIG $LOGGER_CONFIG $MEMORY_FLAG -jar "$DPB_JAR_FILE" -c "$CONFIG_FILE_URI" "$@"
