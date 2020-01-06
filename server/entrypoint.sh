#!/bin/sh
exec java ${JAVA_OPTS} \
-Djava.security.egd=file:/dev/./urandom \
-XX:+UnlockExperimentalVMOptions \
-XX:+UseCGroupMemoryLimitForHeap \
-jar "/opt/app/app.jar" "$@"