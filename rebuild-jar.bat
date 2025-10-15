@echo off
echo ========================================
echo   RECONSTRUYENDO JAR DEL PROYECTO
echo ========================================
echo.

echo [1/4] Limpiando proyecto...
call mvnw.cmd clean -q
if %errorlevel% neq 0 (
    echo ERROR: Fallo en la limpieza
    pause
    exit /b 1
)

echo [2/4] Compilando proyecto...
call mvnw.cmd compile -q
if %errorlevel% neq 0 (
    echo ERROR: Fallo en la compilacion
    pause
    exit /b 1
)

echo [3/4] Ejecutando tests...
call mvnw.cmd test -q
if %errorlevel% neq 0 (
    echo ADVERTENCIA: Algunos tests fallaron, continuando...
)

echo [4/4] Generando JAR...
call mvnw.cmd package -q
if %errorlevel% neq 0 (
    echo ERROR: Fallo en la generacion del JAR
    pause
    exit /b 1
)

echo.
echo ========================================
echo   JAR GENERADO EXITOSAMENTE
echo ========================================
echo.
echo Ubicacion: target\service-pedido-0.0.1-SNAPSHOT.jar
echo.
echo Para ejecutar: java -jar target\service-pedido-0.0.1-SNAPSHOT.jar
echo.
pause
