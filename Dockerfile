FROM java:8-jre-alpine
ARG JAR_NAME
COPY "service/build/libs/${JAR_NAME}" "/usr/src/app/app.jar"
EXPOSE 8280 8281 8200
RUN echo ${JAR_NAME}
ADD https://s3.eu-west-2.amazonaws.com/uk-gov-dft-common-artefacts/import-rds-certs.sh /import-rds-certs.sh
RUN echo ${JAR_NAME} && apk --no-cache add coreutils openssl curl && chmod +x /import-rds-certs.sh && /import-rds-certs.sh
CMD ["java","-jar","/usr/src/app/app.jar"]
