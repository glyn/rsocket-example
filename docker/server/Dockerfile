# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine
# copy example code and dependencies into image
COPY rsocket-example.jar /rsocket-example.jar
COPY dependencies /dependencies
# run application with this command line. Listen on port 8081 on all IPv4 addresses in the container.
CMD ["/usr/bin/java","-cp", "rsocket-example.jar:dependencies/*", "server.Server", "8081", "0.0.0.0"]
