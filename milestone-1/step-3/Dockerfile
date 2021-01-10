# Execute stage
FROM openjdk:15-alpine AS app-image

ENV SPRING_PROFILES_ACTIVE docker

# Unset SUID and GUID permissions to get a hardened image
RUN for i in `find / -perm +6000 -type f`; do chmod a-s $i; done

ENTRYPOINT ["java", "-jar", "app.jar"]

# Get jar file produces by Maven dockerfile goal
ARG JAR_FILE
RUN echo 'jar file location: ' ${JAR_FILE}
COPY ${JAR_FILE} app.jar
