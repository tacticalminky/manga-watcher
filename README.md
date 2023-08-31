# Manga Watcher

<!-- https://shields.io/badges -->
[![GitHub release](https://img.shields.io/github/v/release/tacticalminky/manga-watcher)](https://github.com/tacticalminky/manga-watcher/releases)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/tacticalminky/manga-watcher/docker-publish.yml)
[![GitHub License](https://img.shields.io/github/license/tacticalminky/manga-watcher)](https://github.com/tacticalminky/manga-watcher/blob/master/LICENSE)

Add summary

## Table of Contents

[Manga Watcher](#manga-watcher)

[Debendencies](#dependencies)

[Development](#development)

[Deployment](#deployment)

## Dependencies

* [Apache Maven](https://maven.apache.org)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
* [jsoup](https://jsoup.org/)
* [Angular](https://angular.io/)
* [Bootstrap](https://getbootstrap.com/)

## Development

Create an .env file to define the following

```text
MONGODB_URI=
MONGODB_USER=
MONGODB_PASSWORD=
MONGODB_DB=
LOCAL_DB_DIR=
TELEGRAM_BOT_USERNAME=
TELEGRAM_BOT_TOKEN=
TELEGRAM_CHAT_ID=
```

### Option 1: Running localy

### Option 2: Runnigh through Docker

## Deployment

<!-- NOTE: this utilizes a network called proxynet as a reverse proxy -->
