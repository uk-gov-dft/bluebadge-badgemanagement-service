FROM java:8-alpine
ARG JAR_NAME
COPY "service/build/libs/${JAR_NAME}" "/usr/src/app/app.jar"
EXPOSE 8280 8281 8200
RUN echo ${JAR_NAME}
ENTRYPOINT ["java","-jar","/usr/src/app/app.jar"]
