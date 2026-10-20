# Tarea 2 (individual): payroll-engine

Java 21 puro, sin Spring. Es el ejercicio clásico de entrevista: una empresa paga a **empleados** y **proveedores** desde una **cuenta**, con herencia, interfaces, polimorfismo, streams y concurrencia.

Esta la resuelves completamente solo. Cuando termines, súbela a un repositorio de Git y me pasas el enlace para revisarla.

## Qué recibes

- El esqueleto en `src/main/java`: clases con sus firmas y `throw new UnsupportedOperationException("TODO")` en cada método. Los comentarios `TODO` explican las reglas.
- 23 tests en `src/test/java`. Son la especificación: cuando todos pasan, la tarea está completa.

```
mvn test
```

Al empezar, todos fallan con `TODO`. Esa es la señal de partida.

## El modelo

| Tipo | Rol | Concepto que practica |
| --- | --- | --- |
| `Payable` | Todo lo que la empresa paga | Interfaz, después `sealed` |
| `Employee` | Estado común y `monthlyPay()` abstracto | Clase abstracta, encapsulación, `equals`/`hashCode` |
| `SalariedEmployee` | Salario fijo | Herencia |
| `HourlyEmployee` | Por hora, horas extra a 1.5x sobre 160 h | Polimorfismo, `BigDecimal` y redondeo |
| `Manager` | Es un asalariado y tiene un equipo | Herencia + composición, `super.monthlyPay()` |
| `Supplier`, `Invoice` | El proveedor no es empleado; se le paga por factura | Records, validación en constructor compacto |
| `PayeeType` | Empleado o proveedor | Pattern matching en `switch` |
| `Account` | Cuenta compartida por muchos hilos | Locks, check-then-act atómico |
| `Transfers` | Mover dinero entre dos cuentas | Orden de locks, sin deadlocks |
| `PayrollProcessor` | Paga todo en paralelo | Virtual threads, `Semaphore`, compensación |
| `PayrollReport`, `PayrollAnalytics` | Reportes | Streams y collectors |

## Orden sugerido

1. `Employee`, `SalariedEmployee`, `HourlyEmployee`, `Manager`, `Supplier`, `Invoice`, `PayeeType`. Corre `EmployeeTest`.
2. `PayrollAnalytics` y `PayrollReport` con streams, sin loops. Corre `PayrollAnalyticsTest`.
3. `Account` y `Transfers`. Corre `AccountConcurrencyTest` **varias veces**: un error de concurrencia puede pasar una vez por suerte.
4. `PayrollProcessor`. Corre `PayrollProcessorTest`.
5. Mejora: convierte `Payable` y `PaymentResult` en `sealed` y quita cualquier `default` de los `switch`.

## Reglas

- Sin librerías extra. Solo JDK 21 y JUnit.
- No modifiques los tests. Si crees que uno está mal, anótalo y lo discutimos.
- Dinero siempre con `BigDecimal`. Compara con `compareTo`, no con `equals`.
- Ningún campo público mutable. Ninguna lista interna expuesta.
- Nada de `Thread.sleep` ni `synchronized` sobre `this` sin una razón que puedas explicar en inglés.

## Entrega por Git

```
git init
git add .
git commit -m "Payroll engine: domain model"
# commits pequeños por etapa: model, analytics, concurrency, processor
git remote add origin <tu-repo>
git push -u origin main
```

Agrega al final de este README una sección **Design notes** en inglés, de 5 a 10 líneas: por qué cada tipo es clase, clase abstracta, interfaz o record; cómo evitaste los deadlocks; y qué cambiarías si la cuenta viviera en una base de datos compartida por varios pods. Es práctica directa para explicarlo en la entrevista.

## Cómo la voy a revisar

| Criterio | Qué miro |
| --- | --- |
| Tests | Los 23 pasan, y siguen pasando al correrlos 10 veces |
| OOP | Responsabilidades claras, sin `instanceof` fuera de `PayeeType`, sin duplicar lógica del padre |
| Concurrencia | Sin condiciones de carrera, sin deadlocks, locks liberados en `finally`, interrupciones bien manejadas |
| Streams | Collectors correctos, sin efectos secundarios dentro del stream |
| Java moderno | Records, sealed, pattern matching donde aportan |
| Explicación | Las design notes en inglés |

## Notes
1. The first that I did was to check it out the employee class and its derivaties, so and then I realize that the problems arround some clases, for example what clases needed to be declared as sealed or final.
2. I try to compile frequently the project to seeing if the code runs and If I go well in my understanding
