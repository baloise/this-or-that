FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/this-or-that
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/WEB-INF/lib /app/lib
COPY ${DEPENDENCY}/WEB-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.baloise.open.thisorthat.Application","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"]