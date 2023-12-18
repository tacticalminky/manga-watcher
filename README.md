# Manga Watcher

<!-- https://shields.io/badges -->
[![GitHub release](https://img.shields.io/github/v/release/tacticalminky/manga-watcher)](https://github.com/tacticalminky/manga-watcher/releases)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/tacticalminky/manga-watcher/docker-publish.yml)
[![GitHub License](https://img.shields.io/github/license/tacticalminky/manga-watcher)](https://github.com/tacticalminky/manga-watcher/blob/master/LICENSE)

The inspiration for this project is because I like to read manga through [mangapill](https://mangapill.com). The issue with mangapill is that there is no easy way to keep track of specific manga or notifiy the user of new releases.

This app seeks to do that by creating a kind of wrapper, where the user is able to add their own manga on mangapill to the app. To do so, you must click the 'Add Manga' button, provide a name and the url to the manga. The user will be able to quickly navigate to each of their tracked manga. The user will be able to see which chapters they have read. Clicking on that chapter through the app will automaticaly update its read status. Users will also be notified of new chapters if they setup notifications.

This app currently only supports tracking manga on mangapill and any other platforms are not being considered.

## Table of Contents

* [Manga Watcher](#manga-watcher)
* [Dependencies](#dependencies)
* [Environmental Variables](#environmental-variables)
* [Deployment](#deployment)
    * [Option 1: Docker Compose (recommended)](#option-1-docker-compose-recommended)
    * [Option 2: Native](#option-2-native)

## Dependencies

* Java 17
* [Apache Maven](https://maven.apache.org)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
* [jsoup](https://jsoup.org/)
* [Angular](https://angular.io/)
* [Bootstrap](https://getbootstrap.com/)

## Environmental Variables

The following environmental variables are used with the docker compose file. The defaults given are defined in the [Dockerfile](./Dockerfile).

* `APPDATA_PATH` - the appdata path for the web app and mongo container (default: `""`)

* `MONGODB_USER` - the MongoDB user (default: none)
* `MONGODB_PASSWORD` - the MongoDB user's password (default: none)
* `MONGODB_DB` - the name of the MongoDB database to use (default: `"mangawatcher"`)

* `MONGODB_URI` - (optional) for use with a different URI, such as MongoDB Atlas (default: `"mongodb+srv://<username>:<password>@<clusterName>.mongodb.net"`)

The following environmental variables are used for Telegram notifications. They are currently needed as not defining them will lead to untested behavior. Probably a crash.

* `TELEGRAM_BOT_USERNAME` - the Telegram bot's username (default: `""`)
* `TELEGRAM_BOT_TOKEN` - the Telegram bot's token (default: `""`)
* `TELEGRAM_BOT_ID` - the Telegram bot's chat id (default: `""`)

## Deployment

<!-- NOTE: this utilizes a network called proxynet as a reverse proxy -->

### Option 1: Docker Compose (recommended)

The recomended form of deployement is to use the provided [docker compose file](./docker-compose.yml). To do so, use must create a `.env` file to define the above variables. Then you can deploy it as you would with any other docker compose file.

The compose file has a `proxynet` network which is used for reverse proxying. This network is defined outside of the compose file.

### Option 2: Native

Native deployment is not recommended outside of developement. To do so, you must have Angular and Java 17 JDK installed. At the minimum, the `MONGODB_URI` will need to be defined as an environmental variable for the server.

Run the client with `ng serve`.

Run the server with `./mvnw spring-boot:run`

Optionaly, you can build the client into the server's files. See [Dockerfile](./Dockerfile).
