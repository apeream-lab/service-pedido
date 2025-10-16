# 🧩 Proyecto Kafka - Productor y Consumidor (Capacitación Universidad Santo Tomás)

## 📘 Necesidad
Este proyecto fue desarrollado como parte de la **capacitación sobre Apache Kafka** dictada en la **Universidad Santo Tomás**.  
El objetivo es **mostrar el funcionamiento de un sistema de mensajería distribuido**, conformado por un **productor de mensajes** y un **consumidor**, ambos implementados en **Spring Boot**.

La práctica busca que los estudiantes comprendan el flujo completo:
- Cómo un microservicio **publica mensajes** en un tópico Kafka.
- Cómo otro microservicio **los consume** y procesa la información.

---

## 🎯 Alcance
El proyecto implementa dos componentes principales bajo la arquitectura Spring Boot:

1. **Productor de pedidos** → Algoritmo productor  
   Envía mensajes con información de pedidos al tópico **`pedidos`** de Kafka.

2. **Consumidor de pedidos** → Algoritmo consumidor  
   Escucha el mismo tópico y registra la información de los pedidos recibidos.

Ambos servicios se comunican mediante un **broker Kafka** desplegado en **Podman** dentro de **WSL2**.

---

## ⚙️ Requerimientos Técnicos

### 🧱 Software necesario
- **Windows 10/11 con WSL2**
- **Podman** y **Podman Compose**
- **Java 17 o superior**
- **Maven 3.9+**
- **Visual Studio Code** (o editor de preferencia)
- **Git** (para clonar o subir al repositorio)

---

### 🐳 Podman Compose

Se utiliza un archivo `podman-compose-kafka-3brokers.yml` con la siguiente estructura mínima:

```yaml
version: "3.8"

services:
  zookeeper:
    image: docker.io/confluentinc/cp-zookeeper:7.5.0
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports: ["2181:2181"]
    restart: unless-stopped

  kafka1:
    image: docker.io/confluentinc/cp-kafka:7.5.0
    container_name: kafka1
    hostname: kafka1
    depends_on: [zookeeper]
    ports: ["9092:9092"]
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_LISTENERS: INTERNAL://0.0.0.0:29092,EXTERNAL://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka1:29092,EXTERNAL://172.24.119.148:9092
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 2
      KAFKA_MIN_INSYNC_REPLICAS: 2
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
    restart: unless-stopped

  kafka2:
    image: docker.io/confluentinc/cp-kafka:7.5.0
    container_name: kafka2
    hostname: kafka2
    depends_on: [zookeeper]
    ports: ["9093:9092"]
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_LISTENERS: INTERNAL://0.0.0.0:29092,EXTERNAL://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka2:29092,EXTERNAL://172.24.119.148:9093
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 2
      KAFKA_MIN_INSYNC_REPLICAS: 2
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
    restart: unless-stopped

  kafka3:
    image: docker.io/confluentinc/cp-kafka:7.5.0
    container_name: kafka3
    hostname: kafka3
    depends_on: [zookeeper]
    ports: ["9094:9092"]
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_LISTENERS: INTERNAL://0.0.0.0:29092,EXTERNAL://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka3:29092,EXTERNAL://172.24.119.148:9094
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 2
      KAFKA_MIN_INSYNC_REPLICAS: 2
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
    restart: unless-stopped

  kafdrop:
    image: docker.io/obsidiandynamics/kafdrop:latest
    container_name: kafdrop
    depends_on:
      - kafka1
      - kafka2
      - kafka3
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKERCONNECT: "kafka1:29092,kafka2:29092,kafka3:29092"
      SERVER_PORT: 9000
      SERVER_SERVLET_CONTEXT_PATH: /
    restart: unless-stopped
```

#### 🧠 Nota:
La IP 172.24.119.148 corresponde a la dirección del entorno WSL.
Puedes obtenerla con el comando:

```bash
wsl hostname -I
```
y reemplazarla en el YAML y en el código Java donde se configura spring.kafka.bootstrap-servers.

### ⚙️ Configuración del proyecto
Archivo application.properties (extracto):

```code
spring.application.name=service-pedido
server.port=8083

# Kafka
spring.kafka.bootstrap-servers=172.24.119.148:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

spring.kafka.consumer.group-id=facturacion-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer

# Tópicos
kafka.topic.pedidos=pedidos
```
#### 🧠 Nota:
La IP 172.24.119.148 corresponde a la dirección del entorno WSL.

## 🧩 Verificar el Tópico `pedidos`

Antes de ejecutar el programa de este laboratorio, es necesario asegurarse de que el **tópico `pedidos`** exista en el clúster de Kafka.  
Este tópico es el canal de mensajería donde el productor publica los pedidos y el consumidor los procesa.

---

### 🔍 Verificar la existencia del tópico

En el entorno **WSL**, con los contenedores de Kafka levantados mediante `podman-compose`, ejecutar el siguiente comando dentro del contenedor `kafka1`:

```bash
podman exec -it kafka1 kafka-topics --bootstrap-server localhost:29092 --list
```

Si el tópico no aparece en la lista anterior, créalo manualmente ejecutando este comando dentro del mismo contenedor kafka1:
### ⚙️ Crear el tópico pedidos (si no existe)

```bash
podman exec -it kafka1 kafka-topics \
  --bootstrap-server localhost:29092 \
  --create \
  --topic pedidos \
  --partitions 2 \
  --replication-factor 2
```

### 🔎 3️⃣ Confirmar la creación del tópico

Para validar que se creó correctamente:

```bash
podman exec -it kafka1 kafka-topics \
  --bootstrap-server localhost:29092 \
  --describe \
  --topic pedidos
```

## 🚀 Operación
### Levantar Kafka en Podman
```bash
podman-compose -f podman-compose-kafka-3brokers.yml up -d
```

### Verificar los contenedores:
```bash
podman ps
```

### Ejecutar la aplicación
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=pedido
```

### Enviar un pedido de prueba
Se puede usar Postman o curl:
```bash
curl -X POST http://localhost:8083/pedidos \
-H "Content-Type: application/json" \
-d '{
      "id":5,
      "producto": "Computador",
      "cantidad":1,
      "precio":2500000,
      "usuario":{
          "nombre": "Juan",
          "apellido":"Lopez",
          "tipoDocumento":"CC",
          "numeroDocumento":"123456789",
          "correo":"jlopez@hotmail.com"
      }
  }'
```

### Visualizar la recepción en el servicio REST
En el log de la aplicación se verá algo como:
```bash
Request recibido por servicio REST /pedidos/registrar: RequestPedido(id=...
```
### Visualizar la transformación de la entidad recibida en el servicio REST
En el log de la aplicación se verá algo como:
```bash
Request transformado en pedido: Pedido(id=...
```

### Visualizar el envío del Pedido a Kafka
En el log de la aplicación se verá algo como:
```bash
Pedido enviado a Kafka: Pedido(id=...
```

### Visualizar el consumo del Pedido de Kafka
En el log de la aplicación se verá algo como:
```bash
Pedido consumido en FACTURACIÓN: Pedido(id=
```
## 🖼️ Resultado en Imágenes
### Verificación de los contenedores
![Toma de pantalla de la ejecución del comando ```podman ps``` mostrando los contenedores reqeridos funcionando.](docs/images/pruebas-kafka/podmanps.png)

### Inicialización
![Toma de pantalla de la inicialización del programa desde VS Code.](docs/images/pruebas-kafka/inicializacion.png)

### Solicitud desde Postman
![Toma de pantalla de la solicitud al servicio REST de un objeto JSON.](docs/images/pruebas-kafka/postman.png)

### Ejecución
![Toma de pantalla de las principales salidas del programa.](docs/images/pruebas-kafka/ejecucion.png)

📚 Créditos
Proyecto desarrollado por Armando Perea
Dirección de Impuestos y Aduanas Nacionales (DIAN)
Para la Universidad Santo Tomás — Capacitación Kafka 2025.