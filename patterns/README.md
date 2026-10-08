# 2. Patrones de diseño

El proyecto contiene diferentes elementos que permiten identificar dos patrones de diseño: **Strategy** y **Decorator**.

---

## 2.a Strategy Pattern

El patrón **Strategy** permite definir diferentes comportamientos bajo una misma abstracción y seleccionar cuál de ellos
utilizar sin modificar el componente que consume dicha abstracción.

En el proyecto, este patrón se puede observar en la interfaz:

```java
public interface Q {
    String greet();
}
```

La interfaz `Q` define el contrato común, mientras que existen diferentes implementaciones:

```text
          Q
        /   \
       /     \
  SpainQ   EnglishQ
```

`SpainQ` implementa el saludo en español:

```java
public class SpainQ implements Q {

    @Override
    public String greet() {
        return "hola!!!!";
    }
}
```

Mientras que `EnglishQ` implementa el saludo en inglés:

```java
public class EnglishQ implements Q {

    @Override
    public String greet() {
        return "hi!!!!";
    }
}
```

El consumidor (`ServiceWithQ`) trabaja con la abstracción `Q` y no necesita conocer directamente la implementación
concreta.

Por ejemplo:

```java
public ServiceWithQ(@Qualifier("spainQ") Q q) {
    this.q = q;
}
```

La implementación utilizada puede cambiarse sin modificar el controller ni el contrato definido por `Q`.

### ¿Para qué sirve?

Permite encapsular diferentes comportamientos que pueden ser seleccionados de acuerdo con la necesidad de la aplicación.

En este caso, el comportamiento puede cambiar entre:

```text
SpainQ   → hola!!!!
EnglishQ → hi!!!!
```

sin cambiar la lógica del componente que utiliza `Q`.

### Beneficios

- Reduce el acoplamiento entre el consumidor y las implementaciones concretas.
- Permite agregar nuevas estrategias sin modificar las existentes.
- Facilita las pruebas unitarias al poder sustituir una implementación por otra.
- Favorece el principio Open/Closed.
- Permite cambiar el comportamiento mediante configuración o inyección de dependencias.

Por ejemplo, sería posible agregar una nueva implementación:

```java
public class FrenchQ implements Q {

    @Override
    public String greet() {
        return "bonjour!!!!";
    }
}
```

sin necesidad de modificar `SpainQ`, `EnglishQ` ni la interfaz `Q`.

---

## 2.b Decorator Pattern

El patrón **Decorator** permite agregar comportamiento a un objeto existente sin modificar su implementación original.

En el proyecto, `UppercaseQ` puede identificarse con este patrón porque implementa la misma interfaz `Q` y además recibe
otra implementación de `Q` mediante su constructor:

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

En este caso, `UppercaseQ` envuelve a otra implementación de `Q` y agrega un comportamiento adicional: convertir el
resultado a mayúsculas.

Por ejemplo:

```text
SpainQ
   ↓
"hola!!!!"
   ↓
UppercaseQ
   ↓
"HOLA!!!!"
```

También puede utilizarse sobre `EnglishQ`:

```text
EnglishQ
   ↓
"hi!!!!"
   ↓
UppercaseQ
   ↓
"HI!!!!"
```

La estructura puede representarse de la siguiente manera:

```text
             Q
             |
      +------+------+
      |             |
    SpainQ     UppercaseQ
                     |
                     |
                     Q
```

`UppercaseQ` mantiene el mismo contrato (`Q`) pero agrega una responsabilidad adicional al comportamiento recibido.

### ¿Para qué sirve?

Permite agregar funcionalidades o responsabilidades a un objeto de forma dinámica, sin tener que modificar la clase
original.

En este caso, `UppercaseQ` no modifica `SpainQ` ni `EnglishQ`; simplemente transforma el resultado que obtiene de ellas.

### Beneficios

- Permite agregar comportamiento sin modificar la implementación original.
- Favorece la composición sobre la herencia.
- Permite combinar diferentes comportamientos.
- Reduce el acoplamiento.
- Favorece el principio Open/Closed.

Por ejemplo, se podría componer:

```text
EnglishQ
   ↓
UppercaseQ
   ↓
OtroDecorator
```

manteniendo siempre el mismo contrato `Q`.

---

## Conclusión

Los dos patrones identificados en el proyecto cumplen diferentes objetivos:

| Patrón    | Implementación en el proyecto | Objetivo                                                               |
|-----------|-------------------------------|------------------------------------------------------------------------|
| Strategy  | `Q`, `SpainQ`, `EnglishQ`     | Permitir diferentes estrategias de saludo bajo una misma abstracción   |
| Decorator | `UppercaseQ`                  | Agregar comportamiento adicional a una implementación existente de `Q` |

La combinación de ambos permite cambiar la estrategia de generación del saludo y, al mismo tiempo, agregar
comportamientos adicionales sin modificar las implementaciones originales.