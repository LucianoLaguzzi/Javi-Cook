FROM quay.io/wildfly/wildfly:25.0.1.Final-alpine

# Crear directorios, cambiar permisos y copiar archivos en un solo paso
RUN mkdir -p /opt/jboss/wildfly/{uploads,img} /opt/jboss/wildfly/modules/com/mysql/main/ && \
    chown -R jboss:jboss /opt/jboss/wildfly/uploads /opt/jboss/wildfly/img

# Copiar el archivo WAR y otros archivos en un solo paso
COPY target/JaviCook.war /opt/jboss/wildfly/standalone/deployments/ \
     mysql-connector-java-5.1.48.jar module.xml /opt/jboss/wildfly/modules/com/mysql/main/ \
     standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Exponer el puerto 8080 para acceder a la app
EXPOSE 8080

# Comando para iniciar WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
