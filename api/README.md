# Challenge Monnet Payments

## 1. Aplicación API

La aplicación API se encuentra dentro del paquete:

`com.monnetpayments.challenge.api`

El proyecto contenía errores intencionales que fueron corregidos para permitir que la aplicación compile, levante
correctamente y exponga el endpoint solicitado.

---

### 1.a Correcciones para levantar correctamente la aplicación

Para cumplir con el requerimiento, la aplicación debe iniciar correctamente y permitir consultar:

```text
http://localhost:8099/challenge/greet/
```

obteniendo como respuesta:

```text
hola!!!!
```

#### `ApiApplication`

Se corrigió el método `main` para que sea un método válido de entrada de Java:

```java
public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
}
```

El método debe ser `static` para que Java pueda utilizarlo como punto de entrada de la aplicación.

También se corrigió la configuración del escaneo de componentes. La aplicación API estaba configurada para escanear el
paquete correspondiente a la aplicación Console:

```java
@SpringBootApplication(scanBasePackages = "com.monnetpayments.challenge.console.*")
```

Esto era incorrecto, ya que la aplicación que se está levantando pertenece al paquete:

```text
com.monnetpayments.challenge.api
```

Por lo tanto, se utiliza:

```java
@SpringBootApplication
```

permitiendo que Spring realice el component scan a partir del paquete de `ApiApplication`.

#### `application.yml`

Se configuró el puerto solicitado por el challenge:

```yaml
server:
  port: 8099
```

Además, se mantiene `/challenge` como contexto del servlet:

```yaml
spring:
  mvc:
    servlet:
      path: /challenge
```

Por lo tanto, el endpoint definido en `GreetController`:

```text
/greet
```

queda disponible como:

```text
http://localhost:8099/challenge/greet/
```

La respuesta esperada es:

```text
hola!!!!
```

#### Compatibilidad entre Spring Boot y Springfox (Extra)

El proyecto utiliza Spring Boot `2.7.3` y Springfox `2.9.2`.

Durante el arranque se presentó un error relacionado con `documentationPluginsBootstrapper` de Springfox. Para
solucionar la incompatibilidad entre Spring Boot y Springfox se configuró:

```yaml
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
```

Además, se eliminó la configuración:

```yaml
spring:
  web:
    resources:
      add-mappings: false
```

ya que impedía que los recursos estáticos necesarios para Swagger UI fueran servidos correctamente.

Con estos cambios, Springfox puede inicializar correctamente y la aplicación puede exponer tanto la documentación de
Swagger como su interfaz gráfica.

#### `SpainQ`

Existía un error funcional en el método `greet()`:

```java
@Override
public String greet() {
    return "hola!!!!!! " + this.greet();
}
```

La llamada a:

```java
this.greet()
```

provocaba una recursión infinita, ya que el método se llamaba nuevamente a sí mismo sin una condición de salida.

Esto terminaba generando un:

```text
StackOverflowError
```

Se corrigió para devolver directamente el saludo solicitado:

```java
@Override
public String greet() {
    return "hola!!!!";
}
```

#### Selección de la implementación de `Q`

El proyecto contiene tres implementaciones de la interfaz `Q`:

```text
SpainQ
EnglishQ
UppercaseQ
```

Las tres son compatibles con el tipo:

```java
Q
```

Por lo tanto, cuando `ServiceWithQ` recibe una dependencia de tipo `Q`, Spring necesita saber qué implementación
concreta debe inyectar.

Esto puede resolverse mediante `@Qualifier`.

Para el requisito 1.a se selecciona `SpainQ`:

```java
public ServiceWithQ(@Qualifier("spainQ") Q q) {
    this.q = q;
}
```

De esta forma, el flujo para obtener el saludo en español es:

```text
GreetController
      |
      v
ServiceWithQ
      |
      v
SpainQ
      |
      v
"hola!!!!"
```

#### Verificación del endpoint

Con la aplicación levantada correctamente, se puede consultar:

```text
GET http://localhost:8099/challenge/greet/
```

Respuesta:

```text
hola!!!!
```

#### Swagger (Extra)

La aplicación utiliza Springfox para generar la documentación de la API.

El JSON de Swagger queda disponible en:

```text
http://localhost:8099/challenge/v2/api-docs
```

La interfaz de Swagger UI queda disponible en:

```text
http://localhost:8099/challenge/swagger-ui.html
```

Dentro de Swagger se puede visualizar el endpoint:

```text
GET /greet
```

---

### 1.b ¿Qué haría para obtener el resultado en inglés y en mayúscula?

El código proporcionado ya contiene las implementaciones necesarias para resolver ambos casos.

#### Resultado en inglés

El proyecto contiene la implementación `EnglishQ`:

```java
public class EnglishQ implements Q {

    @Override
    public String greet() {
        return "hi!!!!";
    }
}
```

Para utilizar esta implementación se puede seleccionar el Bean correspondiente mediante `@Qualifier`:

```java
public ServiceWithQ(@Qualifier("englishQ") Q q) {
    this.q = q;
}
```

De esta forma, el flujo sería:

```text
GreetController
      |
      v
ServiceWithQ
      |
      v
EnglishQ
      |
      v
"hi!!!!"
```

El resultado sería:

```text
hi!!!!
```

No es necesario modificar el `GreetController`, ya que este depende del servicio y no de una implementación concreta de
`Q`.

#### Resultado en mayúsculas

El proyecto contiene la implementación `UppercaseQ`:

```java
public class UppercaseQ implements Q {

    private final Q q;

    public UppercaseQ(Q q) {
        this.q = q;
    }

    @Override
    public String greet() {
        return q.greet().toUpperCase();
    }
}
```

`UppercaseQ` recibe otra implementación de `Q` y transforma el resultado obtenido a mayúsculas.

Por ejemplo, utilizando `SpainQ`:

```text
SpainQ
   |
   v
"hola!!!!"
   |
   v
UppercaseQ
   |
   v
"HOLA!!!!"
```

También se puede utilizar sobre `EnglishQ`:

```text
EnglishQ
   |
   v
"hi!!!!"
   |
   v
UppercaseQ
   |
   v
"HI!!!!"
```

Por lo tanto, el comportamiento puede modificarse seleccionando la implementación adecuada de `Q`, sin necesidad de
modificar el controller.

---

### 1.c Componentes importantes

#### `ApiApplication`

Es el punto de entrada de la aplicación Spring Boot.

Su responsabilidad es inicializar el contexto de Spring y levantar la aplicación.

Utiliza:

```java
@SpringBootApplication
```

para habilitar la configuración automática de Spring Boot y el escaneo de componentes.

---

#### `GreetController`

Es el componente encargado de exponer el endpoint HTTP.

Utiliza:

```java
@RestController
```

y define el endpoint:

```java
@GetMapping(path = "greet")
```

Considerando el contexto `/challenge`, el endpoint completo es:

```text
GET http://localhost:8099/challenge/greet/
```

El controller recibe la solicitud HTTP y delega la operación al servicio:

```java
service.doAGreet();
```

Su responsabilidad principal es manejar la comunicación HTTP y delegar la lógica de negocio al servicio.

---

#### `Service`

Es la interfaz que define el contrato de la capa de servicio:

```java
public interface Service {
    String doAGreet();
}
```

Permite que el controller dependa de una abstracción en lugar de una implementación concreta.

---

#### `ServiceWithQ`

Es la implementación de `Service`.

Su responsabilidad es utilizar una implementación de `Q` para obtener el saludo que posteriormente será retornado por el
controller.

La implementación concreta de `Q` puede seleccionarse mediante inyección de dependencias.

---

#### `Q`

Es la interfaz que define el contrato para las diferentes implementaciones de saludo:

```java
public interface Q {
    String greet();
}
```

Esto permite que existan diferentes comportamientos manteniendo el mismo contrato.

---

#### `SpainQ`

Implementa la interfaz `Q` y proporciona el saludo en español:

```text
hola!!!!
```

---

#### `EnglishQ`

Implementa la interfaz `Q` y proporciona el saludo en inglés:

```text
hi!!!!
```

---

#### `UppercaseQ`

Implementa la interfaz `Q` y recibe otra implementación de `Q`.

Su responsabilidad es agregar el comportamiento de convertir el resultado del saludo a mayúsculas:

```java
return q.greet().toUpperCase();
```

De esta forma, puede reutilizar el comportamiento de otra implementación y modificar únicamente el formato del
resultado.

---

#### `Config`

Es la clase de configuración de Spring.

En ella se registran como Beans las diferentes implementaciones de `Q` para que Spring pueda administrarlas e
inyectarlas mediante el contenedor de dependencias.

Entre las implementaciones registradas se encuentran:

```text
SpainQ
EnglishQ
UppercaseQ
```

---

#### `SwaggerConfig`

Es la clase encargada de configurar la documentación de la API mediante Springfox/Swagger.

Utiliza un `Docket` para definir los controllers que serán incluidos en la documentación.

La configuración selecciona los controllers ubicados en:

```text
com.monnetpayments.challenge.api.infrastructure.controllers
```

Por esta razón `GreetController` aparece dentro de la documentación de Swagger.

La documentación generada permite visualizar el endpoint:

```text
GET /greet
```

considerando el contexto:

```text
/challenge
```

por lo que el endpoint completo es:

```text
GET http://localhost:8099/challenge/greet/
```

---

### Flujo general de la aplicación

El flujo principal para una solicitud al endpoint es:

```text
Cliente
   |
   | GET /challenge/greet/
   v
GreetController
   |
   | doAGreet()
   v
ServiceWithQ
   |
   | greet()
   v
Q
   |
   v
SpainQ
   |
   v
"hola!!!!"
```

La implementación de `Q` puede cambiarse para obtener diferentes comportamientos:

```text
SpainQ      -> hola!!!!
EnglishQ    -> hi!!!!
UppercaseQ  -> transforma el resultado a mayúsculas
```