# dockerfile for totask2.
#
# java8 spring-boot application, running totask2.jar on port 8080
#
# @author man-at-home
# @since     2015-04-10
#
FROM java:8
MAINTAINER man-at-home
VOLUME /tmp
ADD toTask2.jar app.jar
# RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
