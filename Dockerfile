# Etapa de construcción
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa final
FROM jboss/wildfly:latest
COPY --from=build /app/target/JaviCook.war /opt/jboss/wildfly/standalone/deployments/
COPY --from=build /app/mysql-connector-java-5.1.48.jar /opt/jboss/wildfly/modules/com/mysql/main/
COPY --from=build /app/module.xml /opt/jboss/wildfly/modules/com/mysql/main/
COPY --from=build /app/standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Exponer el puerto 8080
EXPOSE 8080

# Comando para iniciar WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
