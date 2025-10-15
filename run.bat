@echo off
echo Compilando y ejecutando la aplicación...
mvnw.cmd clean compile
mvnw.cmd spring-boot:run
pause
