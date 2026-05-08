# PISOFÁCIL — Plataforma de búsqueda de alojamiento compartido

## DOCUMENTO ANEXO B — Plan y Ejecución de Pruebas

---

## Índice

1. [Introducción](#1-introducción)
2. [Pruebas Unitarias: Lógica de Negocio (HabitacionServiceTest)](#2-pruebas-unitarias-lógica-de-negocio-habitacionservicetest)
   - 2.1 [Caminos Felices (Happy Path) — CRUD](#21-caminos-felices-happy-path--crud)
   - 2.2 [Excepciones y Casos Límite](#22-excepciones-y-casos-límite)
   - 2.3 [Toggle de Disponibilidad y Control de Permisos](#23-toggle-de-disponibilidad-y-control-de-permisos)
   - 2.4 [Motor de Compatibilidad — calcularAfinidad](#24-motor-de-compatibilidad--calcularafinidad)
3. [Pruebas de Integración Web: Controlador REST (HabitacionControllerTest)](#3-pruebas-de-integración-web-controlador-rest-habitacioncontrollertest)
   - 3.1 [Caminos Felices (Happy Paths)](#31-caminos-felices-happy-paths)
   - 3.2 [Caminos Tristes (Unhappy Paths) y Validación](#32-caminos-tristes-unhappy-paths-y-validación)
4. [Resumen de Cobertura y Conclusiones](#4-resumen-de-cobertura-y-conclusiones)

---

## 1. Introducción

En el ciclo de vida del desarrollo de software profesional, la implementación de una arquitectura robusta debe ir inherentemente acompañada de una estrategia de validación exhaustiva. Este documento anexo complementa la memoria técnica principal del proyecto **PisoFácil**, con el objetivo de detallar el plan de **Aseguramiento de la Calidad (QA)** y la ejecución sistemática de las pruebas automatizadas.

Para certificar la estabilidad, seguridad y fiabilidad del sistema antes de su paso a producción, se ha diseñado una suite de pruebas dividida en múltiples capas, utilizando estándares de la industria:

- **JUnit 5 (Jupiter):** Framework de testing de nueva generación para la plataforma Java. Se han empleado las anotaciones `@Nested` para agrupar tests por contexto semántico, `@DisplayName` para documentación legible y `@ExtendWith(MockitoExtension.class)` para la integración con el motor de mocks.
- **Mockito:** Biblioteca de simulación (mocking) que permite aislar completamente la unidad bajo prueba, reemplazando dependencias reales (repositorios, mappers) por dobles de test controlados. Se han utilizado las anotaciones `@Mock`, `@InjectMocks` y, en el contexto de Spring Boot 3.5, la nueva `@MockitoBean` que sustituye a la depreciada `@MockBean`.
- **Spring MockMvc:** Herramienta de testing de la capa web que permite ejecutar peticiones HTTP simuladas contra los controladores REST sin necesidad de levantar un servidor embebido ni una base de datos real, evaluando códigos de estado, cuerpos de respuesta y headers.
- **AssertJ:** Biblioteca de aserciones fluidas que proporciona una API legible y expresiva para validar resultados, superiores a las aserciones nativas de JUnit.

### Alcance del documento

El alcance de este plan de pruebas abarca la entidad **Habitación** como caso representativo del dominio de negocio, al ser el recurso central de la plataforma y el que concentra la mayor complejidad algorítmica (motor de compatibilidad). Se han diseñado dos baterías complementarias:

| Capa | Clase de Test | Nº Tests | Enfoque |
|------|--------------|----------|---------|
| **Servicio** (Lógica de negocio) | `HabitacionServiceTest` | **17** | Aislamiento total con Mockito. Validación de reglas de dominio, permisos y algoritmos. |
| **Controlador** (API REST) | `HabitacionControllerTest` | **10** | Integración web con MockMvc. Verificación de contratos HTTP, validaciones `@Valid` y manejo de errores. |
| | **TOTAL** | **27** | |

### Stack tecnológico de pruebas

| Componente | Versión | Propósito |
|-----------|---------|-----------|
| JUnit Jupiter | 5.11.x | Motor de ejecución de tests |
| Mockito | 5.x | Simulación de dependencias |
| Spring Boot Test | 3.5.x | Autoconfiguración de contexto de prueba |
| MockMvc | — | Simulación de peticiones HTTP |
| AssertJ | 3.26.x | Aserciones fluidas |
| H2 Database | 2.x | Base de datos en memoria (perfil test) |
| `@MockitoBean` | Spring 3.5+ | Reemplazo de `@MockBean` (depreciado) |

---

## 2. Pruebas Unitarias: Lógica de Negocio (HabitacionServiceTest)

Se ha implementado la clase `HabitacionServiceTest` bajo la extensión `@ExtendWith(MockitoExtension.class)`, que inyecta automáticamente los mocks definidos con `@Mock` en la instancia del servicio anotada con `@InjectMocks`. Este enfoque garantiza un **aislamiento completo** de la capa de servicio respecto a la infraestructura (base de datos, framework web, seguridad).

Los tests se organizan en cuatro bloques funcionales mediante la anotación `@Nested`, que proporciona agrupación semántica y mejora la legibilidad del informe de ejecución.

### Fixtures reutilizables

Antes de cada test (`@BeforeEach`), se inicializan los siguientes objetos de dominio mediante el patrón Builder de Lombok:

| Fixture | Tipo | Descripción |
|---------|------|-------------|
| `propietario` | `Usuario` | Usuario con `idUsuario=1`, perfil con mascota, pareja, no fumador, no LGTBI. |
| `piso` | `Piso` | Piso en Madrid (Gran Vía 1), admite mascotas y parejas, no fumadores, LGTBI-friendly. |
| `habitacion` | `Habitacion` | Habitación vinculada al piso (`id=100`), precio 450€, disponible. |
| `responseDTO` | `HabitacionResponseDTO` | DTO de respuesta con los campos mapeados. |
| `requestDTO` | `HabitacionRequestDTO` | DTO de petición para operaciones de creación. |

---

### 2.1 Caminos Felices (Happy Path) — CRUD

Se verifican las operaciones fundamentales del ciclo de vida CRUD (Create, Read, Update, Delete), asegurando que el flujo normal de ejecución devuelva los resultados esperados y que las interacciones con los repositorios se realicen correctamente mediante `verify()`.

**Resumen de Resultados (6/6 tests pasados):**

| # | Test | Método bajo prueba | Aspecto Clave | Resultado |
|---|------|-------------------|---------------|-----------|
| 1 | `findAll_devuelveListaDTOs` | `findAll()` | Retorna lista con 1 elemento; verifica invocación al repositorio. | ✅ Éxito |
| 2 | `findById_existente_devuelveDTO` | `findById(100L)` | Retorna DTO con `idHabitacion=100` y `ciudad=Madrid`. | ✅ Éxito |
| 3 | `findDisponibles_devuelveSoloDisponibles` | `findDisponibles()` | Filtra exclusivamente habitaciones con `estaDisponible=true`. | ✅ Éxito |
| 4 | `create_pisoExistente_devuelveDTO` | `create(requestDTO)` | Persiste vía `save()` y retorna DTO con título correcto. | ✅ Éxito |
| 5 | `delete_existente_eliminaSinError` | `delete(100L)` | No lanza excepciones; verifica `deleteById()`. | ✅ Éxito |
| 6 | `update_existente_actualizaCampos` | `update(100L, updateDTO)` | Actualiza título a "Título actualizado" y precio a 500€. | ✅ Éxito |

---

### 2.2 Excepciones y Casos Límite

Se valida el comportamiento defensivo del servicio ante datos inexistentes o entradas límite. Se comprueba que el sistema responda con excepciones controladas (`ResourceNotFoundException`) en lugar de errores no gestionados, protegiendo la integridad de la API.

**Resumen de Resultados (4/4 tests pasados):**

| # | Test | Método bajo prueba | Excepción esperada | Aspecto Clave | Resultado |
|---|------|-------------------|-------------------|---------------|-----------|
| 7 | `findById_noExiste_lanzaExcepcion` | `findById(999L)` | `ResourceNotFoundException` | El mensaje contiene el ID "999". | ✅ Éxito |
| 8 | `create_pisoNoExiste_lanzaExcepcion` | `create(requestDTO)` | `ResourceNotFoundException` | Mensaje: "Piso no encontrado". Frena la inserción si la FK es inválida. | ✅ Éxito |
| 9 | `delete_noExiste_lanzaExcepcion` | `delete(999L)` | `ResourceNotFoundException` | Comprueba `existsById()` antes de eliminar. | ✅ Éxito |
| 10 | `findAll_sinDatos_devuelveListaVacia` | `findAll()` | — (sin excepción) | Retorna `[]` vacío sin lanzar error; comportamiento seguro ante tabla vacía. | ✅ Éxito |

---

### 2.3 Toggle de Disponibilidad y Control de Permisos

Se audita el mecanismo de cambio de estado de disponibilidad de una habitación (`toggleDisponibilidad`), que implementa un **control de acceso basado en la identidad del solicitante**. Esta función es crítica para la seguridad del dominio, ya que solo el propietario del piso o un administrador deben poder modificar el estado de publicación.

**Resumen de Resultados (3/3 tests pasados):**

| # | Test | Actor | Resultado esperado | Aspecto Clave | Resultado |
|---|------|-------|--------------------|---------------|-----------|
| 11 | `toggle_propietario_cambia` | Propietario (`carlos@test.com`) | Sin excepción | El propietario legítimo del piso puede alternar la disponibilidad. | ✅ Éxito |
| 12 | `toggle_sinPermisos_lanzaExcepcion` | Intruso (`intruso@test.com`) | `AccessDeniedException` | Un usuario sin relación con el piso recibe un rechazo explícito (403 implícito). | ✅ Éxito |
| 13 | `toggle_admin_cambia` | Admin (`admin@test.com`) | Sin excepción | Un administrador puede gestionar cualquier habitación, independientemente de la propiedad. | ✅ Éxito |

> [!IMPORTANT]
> El test #12 verifica el patrón de seguridad **Fail-Closed**: cualquier usuario que no sea el propietario ni administrador es rechazado por defecto, previniendo ataques de tipo IDOR (Insecure Direct Object Reference).

---

### 2.4 Motor de Compatibilidad — calcularAfinidad

Se evalúa el **algoritmo propietario de afinidad** de PisoFácil, que calcula un porcentaje de compatibilidad entre el perfil del usuario y las políticas del piso. Este motor analiza hasta cuatro dimensiones del perfil:

| Dimensión | Campo Usuario | Campo Piso | Criterio |
|-----------|--------------|------------|----------|
| Mascotas | `tieneMascota` | `admiteMascotas` | Si el usuario tiene mascota y el piso no admite → penalización. |
| Fumador | `esFumador` | `admiteFumadores` | Si el usuario es fumador y el piso no admite → penalización. |
| Pareja | `tienePareja` | `admiteParejas` | Si el usuario tiene pareja y el piso no admite → penalización. |
| LGTBI | `perfilLgtbi` | `lgtbiFriendly` | Si el usuario tiene perfil LGTBI y el piso no es friendly → penalización. |

**Fórmula:** `porcentajeCompatibilidad = (dimensiones_compatibles / dimensiones_activas) × 100`

Si ninguna dimensión está activa (usuario sin preferencias relevantes), el resultado es `null` y no se muestra badge en la UI.

**Resumen de Resultados (4/4 tests pasados):**

| # | Test | Perfil Usuario | Políticas Piso | % Esperado | Resultado |
|---|------|---------------|----------------|------------|-----------|
| 14 | `recomendadas_compatible100` | mascota ✓, pareja ✓ | admite mascotas ✓, admite parejas ✓ | **100%** (2/2) | ✅ Éxito |
| 15 | `recomendadas_incompatibleMascotas` | mascota ✓, pareja ✓ | admite mascotas ✗, admite parejas ✓ | **50%** (1/2) | ✅ Éxito |
| 16 | `recomendadas_incompatibleFumador` | fumador ✓ | admite fumadores ✗ | **0%** (0/1) | ✅ Éxito |
| 17 | `recomendadas_sinDimensiones_devuelveNull` | sin preferencias activas | — | **null** (sin badge) | ✅ Éxito |

> [!NOTE]
> El test #17 es particularmente relevante para la experiencia de usuario: un usuario recién registrado que no ha completado su perfil no debe recibir un porcentaje de compatibilidad falso (ej. 100% por vacuidad), sino que el sistema omite el indicador.

---

## 3. Pruebas de Integración Web: Controlador REST (HabitacionControllerTest)

Se ha implementado la clase `HabitacionControllerTest` utilizando la anotación `@WebMvcTest(HabitacionController.class)`, que levanta **exclusivamente la capa web** de Spring (controladores, serializadores Jackson, validaciones `@Valid`) sin cargar repositorios ni base de datos.

### Configuración del entorno de test

| Anotación / Configuración | Propósito |
|---------------------------|-----------|
| `@WebMvcTest(HabitacionController.class)` | Carga solo el controlador bajo prueba y su infraestructura MVC. |
| `@AutoConfigureMockMvc(addFilters = false)` | **Desactiva los filtros de seguridad** (JWT) para aislar las pruebas del controlador de la autenticación. |
| `@Import(GlobalExceptionHandler.class)` | Importa el manejador global de excepciones para validar que los errores se serializan correctamente en el cuerpo de respuesta. |
| `@MockitoBean` | Inyecta mocks de `HabitacionService`, `UsuarioRepository`, `JwtAuthenticationFilter`, `JwtUtil` y `PisoFacilUserDetailsService`. |

### Contrato HTTP verificado

La siguiente tabla resume los endpoints evaluados, organizados por método HTTP y código de respuesta esperado.

---

### 3.1 Caminos Felices (Happy Paths)

**Resumen de Resultados (6/6 tests pasados):**

| # | Test | Método | Endpoint | HTTP Code | ¿Qué valida? | Resultado |
|---|------|--------|----------|-----------|--------------|-----------|
| 1 | `findAll_devuelve200` | `GET` | `/habitaciones` | **200 OK** | Retorna array JSON con 1 elemento; verifica campo `tituloAnuncio`. | ✅ Éxito |
| 2 | `findAll_listaVacia_devuelve200` | `GET` | `/habitaciones` | **200 OK** | Retorna array vacío `[]`; no lanza error ante tabla sin datos. | ✅ Éxito |
| 3 | `findById_existente_devuelve200` | `GET` | `/habitaciones/1` | **200 OK** | Retorna objeto con `idHabitacion=1` y `ciudad=Madrid`. | ✅ Éxito |
| 4 | `create_bodyValido_devuelve201` | `POST` | `/habitaciones` | **201 Created** | Body JSON válido con `idPiso`, `tituloAnuncio` y `precioMensual` → recurso creado. | ✅ Éxito |
| 5 | `delete_existente_devuelve204` | `DELETE` | `/habitaciones/1` | **204 No Content** | Eliminación exitosa sin cuerpo de respuesta. | ✅ Éxito |
| 6 | `findDisponibles_devuelve200` | `GET` | `/habitaciones/disponibles` | **200 OK** | Filtra solo disponibles; verifica `estaDisponible=true`. | ✅ Éxito |

---

### 3.2 Caminos Tristes (Unhappy Paths) y Validación

**Resumen de Resultados (4/4 tests pasados):**

| # | Test | Método | Endpoint | HTTP Code | ¿Qué valida? | Resultado |
|---|------|--------|----------|-----------|--------------|-----------|
| 7 | `findById_noExiste_devuelve404` | `GET` | `/habitaciones/999` | **404 Not Found** | `ResourceNotFoundException` → body contiene `error` con "999". Protege contra IDs fantasma. | ✅ Éxito |
| 8 | `create_sinTitulo_devuelve400` | `POST` | `/habitaciones` | **400 Bad Request** | Body vacío sin campos obligatorios → Jakarta `@Valid` rechaza la petición antes de llegar al servicio. | ✅ Éxito |
| 9 | `delete_noExiste_devuelve404` | `DELETE` | `/habitaciones/999` | **404 Not Found** | Intento de eliminación de recurso inexistente → error controlado en body. | ✅ Éxito |
| 10 | `buscarAvanzado_conCiudad_devuelve200` | `GET` | `/habitaciones/buscar?ciudad=Madrid` | **200 OK** | Endpoint de búsqueda avanzada con filtro `ciudad`; retorna resultados filtrados con `ciudad=Madrid`. | ✅ Éxito |

> [!IMPORTANT]
> El test #8 verifica que la **validación de Jakarta Bean Validation** (`@Valid` + `@NotBlank`, `@NotNull`) actúa como primera línea de defensa, rechazando peticiones malformadas con un HTTP 400 antes de que el servicio procese datos inválidos. Este patrón es fundamental para prevenir inyecciones y estados inconsistentes en la base de datos.

---

## 4. Resumen de Cobertura y Conclusiones

### Matriz de cobertura por tipo de prueba

| Categoría | Nº Tests | % del Total | Estado |
|-----------|----------|-------------|--------|
| Happy Path — CRUD (Service) | 6 | 22.2% | ✅ 6/6 |
| Excepciones y Casos Límite (Service) | 4 | 14.8% | ✅ 4/4 |
| Toggle Disponibilidad + Permisos (Service) | 3 | 11.1% | ✅ 3/3 |
| Motor de Compatibilidad (Service) | 4 | 14.8% | ✅ 4/4 |
| Happy Paths — Controller (Integration) | 6 | 22.2% | ✅ 6/6 |
| Unhappy Paths — Controller (Integration) | 4 | 14.8% | ✅ 4/4 |
| **TOTAL** | **27** | **100%** | **✅ 27/27** |

### Códigos HTTP validados

| Código | Significado | Tests que lo cubren |
|--------|-------------|-------------------|
| `200 OK` | Consulta exitosa | #1, #2, #3, #6, #10 |
| `201 Created` | Recurso creado correctamente | #4 |
| `204 No Content` | Eliminación exitosa | #5 |
| `400 Bad Request` | Validación de entrada fallida | #8 |
| `403 Forbidden` | Acceso denegado (RBAC) | Service #12 (implícito) |
| `404 Not Found` | Recurso no encontrado | #7, #9 |

### Conclusiones

La batería de pruebas implementada certifica los siguientes aspectos críticos de la plataforma PisoFácil:

1. **Integridad del ciclo CRUD:** Todas las operaciones de lectura, creación, actualización y eliminación de habitaciones funcionan correctamente en el camino feliz y gestionan adecuadamente los errores.

2. **Seguridad a nivel de dominio:** El mecanismo de toggle de disponibilidad implementa un control de acceso basado en identidad (propietario/admin) que previene manipulaciones no autorizadas, siguiendo el principio de Fail-Closed.

3. **Robustez del Motor de Compatibilidad:** El algoritmo de afinidad calcula correctamente los porcentajes en escenarios de compatibilidad total (100%), parcial (50%), nula (0%) y ausencia de datos (`null`), garantizando una experiencia de usuario precisa.

4. **Contrato HTTP estable:** La capa de controladores respeta los estándares REST, devolviendo los códigos de estado apropiados (200, 201, 204, 400, 404) y serializando los errores de forma consistente a través del `GlobalExceptionHandler`.

5. **Validación en múltiples capas:** La combinación de Jakarta Bean Validation (`@Valid`) en el controlador y excepciones controladas en el servicio establece una arquitectura de defensa en profundidad contra datos inválidos.

---

> *Documentación técnica del proyecto PisoFácil — TFG DAM 2025/2026*
> *Batería de pruebas ejecutada sobre Spring Boot 3.5.x, JUnit 5, Mockito 5.x*
