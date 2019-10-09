FROM openjdk:8-jre-alpine
RUN mkdir /opt/app/
ADD target/*.jar /opt/app/app.jar
ADD entrypoint.sh /opt/app/entrypoint.sh
EXPOSE 8080
ENTRYPOINT [“sh”, “/opt/app/entrypoint.sh”]