FROM quay.io/wildfly/wildfly:25.0.1.Final

# Establecer variables de entorno
ENV DEPLOYMENT_DIR=/opt/jboss/wildfly/standalone/deployments/
ENV CONFIGURATION_DIR=/opt/jboss/wildfly/standalone/configuration/
ENV MODULES_DIR=/opt/jboss/wildfly/modules/com/mysql/main/

# Crear la estructura de directorios necesaria
RUN mkdir -p $MODULES_DIR /opt/jboss/wildfly/uploads

# Cambiar permisos del directorio uploads
RUN chown -R jboss:jboss /opt/jboss/wildfly/uploads

#Cambios agregados para crear el directorio si no existe
RUN mkdir -p /opt/jboss/wildfly/img
RUN chown -R jboss:jboss /opt/jboss/wildfly/img


# Copiar el archivo WAR al directorio de despliegue de WildFly
COPY target/JaviCook.war $DEPLOYMENT_DIR

# Copiar el archivo JAR del controlador JDBC
COPY mysql-connector-java-5.1.48.jar $MODULES_DIR/

# Copiar el archivo module.xml
COPY module.xml $MODULES_DIR/

# Copiar el archivo standalone.xml personalizado
COPY standalone.xml $CONFIGURATION_DIR/

# Exponer el puerto 8080 para acceder a la app
EXPOSE 8080

# Comando para iniciar WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
