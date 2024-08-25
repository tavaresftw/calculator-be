FROM gradle:8.3-jdk17 AS build

COPY . /app

WORKDIR /app

RUN gradle build --no-daemon

FROM openjdk:17-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]