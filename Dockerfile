FROM openjdk:17-jdk-slim
WORKDIR /app
COPY ./target/hs2-booking-0.0.1-SNAPSHOT.jar ./hs2-booking.jar
ENTRYPOINT ["java","-jar","/app/hs2-booking.jar"]