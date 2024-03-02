FROM maven:3.9.6-sapmachine-21
WORKDIR /app
COPY pom.xml .
COPY src ./src
CMD ["mvn", "clean", "test"]