FROM node:18-alpine AS builder

WORKDIR /opt/app

COPY . .

RUN npm install && npm run build

FROM nginx:stable

COPY --from=builder /opt/app/dist/* /usr/share/nginx/html/
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
