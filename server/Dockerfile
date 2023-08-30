FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /opt/app

COPY . .
RUN ./mvnw dependency:go-offline
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /opt/app

COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar

EXPOSE 8080
ENV MONGODB_URI="mongodb+srv://<username>:<password>@<clusterName>.mongodb.net"
ENV MONGODB_DB="mangawatcher"
ENV TELEGRAM_BOT_USERNAME="<bot_username>"
ENV TELEGRAM_BOT_TOKEN="<token>"
ENV TELEGRAM_CHAT_ID="<chat_id>"

ENTRYPOINT [ "java", "-jar", "/opt/app/*.jar" ]
