FROM openjdk:17
EXPOSE 8080

ARG JWT_SIGN_KEY
ARG NEWS_API_KEY
ARG ADMIN_AUTH_USER
ARG ADMIN_AUTH_PASS
ARG ADMIN_ROLES
ARG USER_AUTH_USER
ARG USER_AUTH_PASS
ARG USER_ROLES

ENV JWT_SIGN_KEY=$JWT_SIGN_KEY
ENV NEWS_API_KEY=$NEWS_API_KEY
ENV ADMIN_AUTH_USER=$ADMIN_AUTH_USER
ENV ADMIN_AUTH_PASS=$ADMIN_AUTH_PASS
ENV ADMIN_ROLES=$ADMIN_ROLES
ENV USER_AUTH_USER=$USER_AUTH_USER
ENV USER_AUTH_PASS=$USER_AUTH_PASS
ENV USER_ROLES=$USER_ROLES

COPY target/news-group-service.jar news-group-service.jar
ENTRYPOINT ["java","-jar","/news-group-service.jar","--jwt.sign-key=${JWT_SIGN_KEY}", \
  "--news.api-key=${NEWS_API_KEY}", \
  "--news.users[0].authorizedUser=${ADMIN_AUTH_USER}", \
  "--news.users[0].password=${ADMIN_AUTH_PASS}", \
  "--news.users[0].roles=${ADMIN_ROLES}", \
  "--news.users[1].authorizedUser=${USER_AUTH_USER}", \
  "--news.users[1].password=${USER_AUTH_PASS}", \
  "--news.users[1].roles=${USER_ROLES}"]
