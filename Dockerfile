FROM openjdk:8-jdk-alpine

COPY target/bank-0.0.1-SNAPSHOT.jar /Users/cyril/bank/

EXPOSE 8080

WORKDIR /Users/cyril/bank/

ENTRYPOINT ["/usr/bin/java"]

CMD ["-jar", "bank-0.0.1-SNAPSHOT.jar"]