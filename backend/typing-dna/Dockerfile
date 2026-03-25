# --- Paso 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias primero para aprovechar el caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar el proyecto saltando los tests
COPY src ./src
RUN mvn clean package -DskipTests

# --- Paso 2: Ejecución (Runtime) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear un usuario sin privilegios para mayor seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR generado desde el paso de construcción
# Asegúrate de que el nombre del JAR coincida con el de tu pom.xml
COPY --from=build /app/target/*.jar typing-dna-0.0.1-SNAPSHOT.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Configuración de límites de memoria para contenedores Java
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "typing-dna-0.0.1-SNAPSHOT.jar"]