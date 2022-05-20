FROM openjdk:17
MAINTAINER nikitocheck
COPY build/libs/*.jar /opt/app.jar
EXPOSE 8080
CMD ["java","-jar","/opt/app.jar"]