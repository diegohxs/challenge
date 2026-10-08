### 1.d Aplicación Console

La aplicación Console se encuentra dentro del paquete:

`com.monnetpayments.challenge.console`

La clase principal es:

```text
ConsoleApplication
```

#### Algoritmo principal

El método `main` ejecuta una validación entre dos algoritmos diferentes que reciben los mismos parámetros:

- `n`: un número entero generado aleatoriamente entre `0` y `999999`.
- `d`: un dígito generado aleatoriamente entre `0` y `9`.

Esto se repite hasta 99 veces:

```java
int i = 0;

while (++i < 100) {
    int n = (int) (Math.random() * 1000000);
    int d = (int) (Math.random() * 10);

    int withAlgorithm1 = calculateWithAlgorithm1(n, d);
    int withAlgorithm2 = calculateWithAlgorithm2(n, d);

    ...
}
```

Para cada caso se ejecutan ambos métodos y se comparan sus resultados.

Si los resultados son diferentes:

```java
if (withAlgorithm1 != withAlgorithm2) {
    System.out.println(
        "Fails for case number: " + i + " -> (" + n + "," + d + ")"
    );
    return;
}
```

la ejecución finaliza indicando el caso en el que ambos algoritmos producen resultados diferentes.

Si los resultados son iguales, se informa que no hubo diferencias en ese caso y se continúa con la siguiente iteración.

Por lo tanto, el objetivo principal del `main` es **comparar dos implementaciones que intentan resolver el mismo
problema y detectar casos en los que sus resultados sean diferentes**.

---

#### ¿Qué hace `calculateWithAlgorithm1`?

El método:

```java
public static int calculateWithAlgorithm1(int n, int d)
```

es una implementación **recursiva**.

Primero valida que `d` sea un dígito válido:

```java
if (d < 0 || d > 9) {
    return n;
}
```

Luego verifica si `n` llegó a `0`:

```java
if (n == 0) {
    return n;
}
```

Después obtiene el último dígito mediante:

```java
n % 10
```

y elimina ese último dígito mediante:

```java
n / 10
```

Si el último dígito es igual a `d`:

```java
if (n % 10 == d) {
    return calculateWithAlgorithm1(n / 10, d);
}
```

se descarta ese dígito.

Si es diferente:

```java
return calculateWithAlgorithm1(n / 10, d) * 10 + (n % 10);
```

se conserva y se reconstruye el número.

En otras palabras, el algoritmo procesa los dígitos de `n` y **elimina aquellos que son iguales a `d`**, manteniendo el
resto de los dígitos.

Por ejemplo:

```text
n = 123242
d = 2

123242
 ↓ ↓ ↓
 1 3 4

Resultado: 134
```

La característica principal de este método es que utiliza **recursión** para procesar el número dígito por dígito.

---

#### ¿Qué hace `calculateWithAlgorithm2`?

El método:

```java
public static int calculateWithAlgorithm2(int n, int d)
```

intenta resolver el mismo problema que `calculateWithAlgorithm1`, pero utilizando un enfoque **iterativo**.

En lugar de realizar llamadas recursivas, utiliza un `for`:

```java
for (int i = 0; n > Math.pow(10, i); i++) {
    ...
}
```

El algoritmo utiliza potencias de 10 para recorrer los diferentes dígitos del número.

Por ejemplo:

```java
int pow10 = (int) Math.pow(10, i);
int nDivPow10 = n / pow10;
```

Con esto obtiene el dígito correspondiente utilizando:

```java
nDivPow10 % 10
```

Si el dígito es diferente de `d`, se agrega al resultado:

```java
result = result + (nDivPow10 % 10) * ...
```

Si el dígito es igual a `d`, se incrementa:

```java
applied++;
```

para ajustar la posición de los siguientes dígitos que serán agregados al resultado.

La principal diferencia entre ambos métodos es, por lo tanto:

```text
Algorithm 1
    ↓
Implementación recursiva

Algorithm 2
    ↓
Implementación iterativa
```

Ambos buscan obtener el mismo resultado: eliminar del número `n` los dígitos que sean iguales a `d`.

---

#### Comparación entre ambos métodos

El método `main` utiliza los dos algoritmos sobre los mismos valores de entrada:

```text
n
d
```

y posteriormente compara:

```java
withAlgorithm1 != withAlgorithm2
```

Esto permite comprobar si ambas implementaciones producen el mismo resultado.

De esta manera, la aplicación Console funciona como una prueba comparativa entre las dos soluciones.

Una característica importante es que `calculateWithAlgorithm1` utiliza una lógica recursiva más directa basada en:

```text
% 10
/ 10
```

mientras que `calculateWithAlgorithm2` utiliza una lógica iterativa basada en:

```text
Math.pow(10, i)
```

y el manejo de posiciones mediante la variable:

```text
applied
```

Por lo tanto, aunque ambos métodos intentan resolver el mismo problema, utilizan estrategias de implementación
diferentes.