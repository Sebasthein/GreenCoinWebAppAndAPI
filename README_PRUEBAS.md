# Documentación Técnica: Estrategia de Pruebas

Este documento detalla los conceptos fundamentales sobre el aseguramiento de la calidad del software (QA) aplicado al desarrollo, así como la justificación y las herramientas empleadas explícitamente en el proyecto **GreenCoinWebAppAndAPI**.

---

## 1. Conceptos Fundamentales de Pruebas

### Pruebas Unitarias
Son aquellas que aíslan una parte específica y diminuta del código (típicamente una función, método o clase) y comprueban que funcione correctamente de forma cien por ciento independiente, verificando que para un *input* determinado devuelva el *output* esperado, desconectado de cualquier servicio externo.

### Pruebas de Integración
Verifican que los diferentes módulos, sistemas o dependencias de una aplicación funcionen bien en conjunto. Se encargan de detectar errores en la forma en la que los componentes (como la base de datos, las APIs y los servicios de red) se comunican e intercambian datos entre sí.

### Pruebas de Regresión
Son pruebas automatizadas que se ejecutan cada vez que el código existente es alterado (ya sea por una nueva feature o una corrección de bugs) para asegurar que los cambios recientes no hayan roto funcionalidades o componentes que operaban sin problemas en el pasado.

---

## 2. Herramientas Recomendadas por Tipo

### Para Pruebas Unitarias:
1. **JUnit (Ecosistema Java):** Framework estándar para crear, estructurar y ejecutar pruebas aserción por aserción.
2. **Mockito:** Librería que permite crear objetos simulados (*mocks*) de dependencias complejas (como conexiones a red o base de datos) permitiendo aislar la lógica original que se quiere probar.

### Para Pruebas de Integración:
1. **Spring Boot Test (MockMvc):** Permite arrancar y simular el ciclo de vida completo de un servidor web, interceptando llamadas RESTful (`GET, POST, PUT, DELETE`) sin necesidad de desplegar el código en un tomcat real.
2. **Testcontainers:** Utiliza contenedores de Docker desechables para ofrecer instancias operativas reales (bases de datos PostgreSQL, colas de RabbitMQ, etc.) que se encienden y destruyen junto a las pruebas.

### Para Pruebas de Regresión:
1. **Selenium:** Autómata de navegadores líder en la industria, que permite simular clics y tecleos reales de usuario persistiendo los "casos felices" a través del tiempo.
2. **Postman / Newman:** Permite guardar y documentar colecciones corporativas enteras de llamadas a la API que luego se ejecutan secuencialmente en un pipeline para detectar cambios de contratos en JSON.

---

## 3. Escenarios del Mundo Real

* **Escenario de Prueba Unitaria:** Al desarrollar un algoritmo en una aplicación bancaria para calcular la tasa de interés compuesto de un préstamo. Es vital aplicar múltiples combinaciones matemáticas complejas a esa función aislada para garantizar resultados sin afectar el servidor transaccional del banco (evitando tocar la base de datos real).
* **Escenario de Prueba de Integración:** Al implementar un sistema *e-commerce*. Al momento del Checkout de compras, es mandatorio comprobar que el microservicio de órdenes se logre contactar exitosamente vía API con la pasarela de pagos externa (PayPal / Stripe / Culqi) y que un rechazo de pago aborte la transacción local.
* **Escenario de Prueba de Regresión:** Un equipo decide actualizar la aplicación central de Android de un SDK viejo a uno nuevo, migrando cientos de dependencias en el camino. Al finalizar la migración, corren automáticamente todas las pruebas de regresión previas sobre "El registro de cuentas nuevas". Como se espera que el flujo de registro siga siendo exactamente igual, si este falla es que la migración rompió componentes antiguos.

---

## 4. Importancia en las Metodologías Ágiles
En metodologías ágiles (como *Scrum*), el software se construye y entrega en iteraciones de semanas cortas (*Sprints*). Implementar la validación y automatización del código es **crítico** porque provee retroalimentación inmediata (Continuous Integration/Continuous Deployment - CI/CD). Los desarrolladores pueden codificar nuevas features sabiendo que si su código nuevo estropea funciones creadas la semana anterior por otro compañero, las pruebas automatizadas alertaran de este hecho inmediatamente bloqueando el despliegue a producción. Disminuye la incertidumbre y erradica el extenso ciclo tradicional QA manual por humanos.

---

## 5. Implementación en la Aplicación (GreenCoinWebAppAndAPI)

Hemos decidido implementar las siguientes validaciones automatizadas al código del servidor Backend:

* **3 Pruebas Unitarias** centradas y aisladas en el Motor Matemático y de Reglas Generales (`UsuarioServicioTest.java` y `ReciclajeServicioTest.java`).
* **2 Pruebas de Integración** centradas explícitamente sobre el enrutamiento HTTP de los Controladores a través de Bases de datos H2 en la memoria RAM (`UsuarioControllerIntegrationTest.java` y `ReciclajeControllerIntegrationTest.java`).
* **1 Prueba de Regresión** enfocada en evitar bugs de lógica secuencial impidiendo de por vida que un reciclaje ya validado sea corrompido de nuevo (`ReciclajeServicioRegressionTest.java`).

### Justificación en el Contexto del Proyecto
1. **Unitarias:** Eran indispensables para probar que el método `agregarPuntos` y `getPuntosAcumulados` sumaban las matemáticas correctas. Las dinámicas de **Gamificación** de acumular "Green Coins" y subir en Ranking del usuario son el *core business* de nuestra aplicación; un fallo de lógica en los puntos echaría a perder el esfuerzo de reciclaje entero.
2. **Integración:** Las llamadas entrantes `/api/usuarios/api/registro` (crear cuenta) y `/api/reciclajes/registrar` (nuevo reciclaje de material) representan las puertas críticas en el servidor; el backend no solo tenía que ejecutar la lógica, sino además rechazar *logins* falsos y serializar correctamente las filas en base de datos junto a los Roles relacionales correctos.
3. **Regresión:** Un caso crítico del dominio es evitar que un organizador asigne dobles puntos de un material a la vez ("Double Spending") reciclándolo. La prueba de excepción nos blinda para que ningún desarrollador despistado o refactorización nueva levante esta regla dorada jamás.

### Stack de Herramientas Implementado
- **JUnit 5 Jupiter:** Framework nativo base de todos nuestros nuevos tests, encargado de montar el entorno y aserciones (`@Test`, `assertEquals`, `assertThrows`).
- **Mockito (`@Mock`, `@InjectMocks`):** Permitió falsear (MOCKiar) las llamadas a la interfase con la capa de Persistencia (JpaRepository) durante los Unit Tests, permitiéndonos chequear los puramente los algoritmos sin tocar Base de Datos.
- **Spring Boot Test (`@SpringBootTest`, `MockMvc`):** Proporcionó un contenedor de Spring Web encapsulado, permitiéndole a los Tests de Integración interceptar rutas REST completas desde un cliente web ficticio para registrar usuarios y verificar Respuestas `HttpStatus.CREATED (201)`.
- **H2 Database engine:** Para los tests de integración, en lugar de usar PostgreSQL forzamos condicionalmente al *application context* de testeo a abrir una base de datos efímera SQL puramente escrita en RAM que se destruye finalizado cada test, asegurando que las pruebas de integración sean veloces y estén totalmente aisladas.
- **Spring Security Test (`@WithMockUser`):** Nos permitió eludir los Filtros de Sesión de JWT de manera simulada para los test del contexto de MVC integrados, facilitando actuar bajo la identidad de la cuenta con validación previa de seguridad sin emitir un token manualmente.
