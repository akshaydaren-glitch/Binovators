*ROM maven:3.9-eclipse-temurin-17

*ORKDIR /app

COPY . .

RUN ls -la
*RUN find /app -name "pom.xml"

RUN*./mvnw clean package -DskipTests

*XPOSE 8080

CMD ["java","-jar","target/Binovators-0.0.1-SNAPSHOT.jar"]
