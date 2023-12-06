FROM node:18-alpine AS client-builder

WORKDIR /opt/app

COPY client/package*.json client/angular.json client/tsconfig*.json ./

RUN npm install

COPY client/src src

RUN npm run build

FROM eclipse-temurin:17-jdk-jammy AS server-builder

WORKDIR /opt/app

COPY server/.mvn .mvn
COPY server/mvnw server/pom.xml ./

RUN ./mvnw dependency:go-offline

COPY server/src src

COPY --from=client-builder /opt/app/dist/* src/main/resources/static

RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /opt/app

COPY --from=server-builder /opt/app/target/*.jar /opt/app/*.jar

EXPOSE 8091

ENV MONGODB_URI="mongodb+srv://<username>:<password>@<clusterName>.mongodb.net"
ENV MONGODB_DB="mangawatcher"

ENV TELEGRAM_BOT_USERNAME="<bot_username>"
ENV TELEGRAM_BOT_TOKEN="<token>"
ENV TELEGRAM_CHAT_ID="<chat_id>"

ENTRYPOINT [ "java", "-jar", "/opt/app/*.jar" ]
