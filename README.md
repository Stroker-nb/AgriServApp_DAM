AgriServ
Aplicación móvil Android que conecta a productores agrícolas con aplicadores de servicios agrícolas (por ejemplo, fumigación con drones). Es el avance de la Evaluación Parcial 2 de la asignatura DSY1105 – Desarrollo de Aplicaciones Móviles (Duoc UC, Escuela de Informática y Telecomunicaciones).

Integrantes
Equipo 11

Nombre Apellido (usuario de GitHub: usuario)
Nombre Apellido (usuario de GitHub: usuario)
Los commits aparecen bajo los usuarios Stroker-nb, Kriszmaa y SJ- Alumno (cuenta de un computador del laboratorio).

Contexto de la aplicación
El problema
Hoy los agricultores buscan prestadores de servicios mediante contactos, recomendaciones o consultas individuales, lo que dificulta comparar alternativas. Los prestadores, por su parte, tienen poca visibilidad.

La solución propuesta
AgriServ es un MVP que busca facilitar esa vinculación. La idea de negocio es la siguiente:

El productor (rol solicitante) registra una necesidad de servicio indicando producto, superficie, disponibilidad de agua, obstáculos y momento de aplicación.
Los aplicadores (rol ofertante) ven las solicitudes cercanas y envían una oferta con su precio.
El productor revisa y compara las ofertas, y decide con quién contactarse.
La contratación, el pago, la facturación y la ejecución del servicio quedan fuera del alcance del proyecto.

Origen del proyecto
El desafío fue planteado por TECMUNDO (tecnología aplicada a agricultura y silvicultura: drones, robots y agricultura de precisión) junto al CITT Duoc UC, Sede San Joaquín. El diseño de pantallas está en el prototipo Agri Serv v0.1.0 (mockups).

Tipos de usuario
Rol en la app	Nombre en pantalla	Qué hace
SOLICITANTE	Productor	Publica y revisa sus solicitudes
OFERTANTE	Aplicador	Ve solicitudes cercanas y ofrece su servicio; además tiene una calificación
Funcionalidades implementadas
Registro con validación por campo (nombre, apellido, RUT con dígito verificador, correo, contraseña), elección de rol, aceptación de términos y modal de confirmación.
Inicio de sesión con validación y verificación de credenciales contra la base de datos local.
Sesión persistente: la app recuerda al usuario y abre directo su pantalla de inicio.
Pantalla de bienvenida animada: el logo gira y crece al abrir la app.
Navegación según el rol: productor y aplicador tienen su propia pantalla de inicio.
Detalle de solicitud y listas de solicitudes (con datos de ejemplo).
Perfil de usuario: muestra los datos de la cuenta con sesión iniciada y, solo para los aplicadores, su calificación en estrellas.
Recursos nativos: cámara y galería de fotos, con solicitud de permisos en tiempo de ejecución y aviso cuando el usuario los rechaza.
Cierre de sesión.
Lo que todavía no está implementado
Las listas de solicitudes y ofertas usan datos de ejemplo y no se guardan en Room (solo los usuarios se persisten).
La foto tomada o elegida en el perfil todavía no se guarda ni se muestra.
Formulario de nueva solicitud, filtros, envío de ofertas y matching.
La calificación de los aplicadores no tiene un flujo para asignarla; se mantiene en 0.0 hasta que se edite.
La contraseña se guarda en texto plano (suficiente para el MVP académico; debe cifrarse en una versión real).
Arquitectura
El proyecto sigue el patrón MVVM (Modelo – Vista – ViewModel) con un repositorio entre los ViewModels y la base de datos.

Pantalla (ui)  ──►  ViewModel  ──►  Repositorio  ──►  DAO  ──►  Base de datos Room
   ▲                   │
   └──── UiState ◄─────┘
La pantalla no tiene reglas: solo dibuja el UiState y avisa al ViewModel lo que hizo el usuario.
El ViewModel valida, decide y actualiza el UiState.
El repositorio es el único que habla con el DAO, así los ViewModels no conocen Room.
MainActivity crea la base de datos, el repositorio y los ViewModels, y se los entrega a la navegación.
Estructura de carpetas
app/src/main/java/com/duoc/agriserv/
├── MainActivity.kt
├── model/
├── repository/
├── viewModel/
└── ui/theme/
Carpeta / archivo	Qué contiene y para qué sirve
MainActivity.kt	Punto de entrada. Crea la base de datos, el repositorio y los tres ViewModels, y arranca la interfaz con Navegacion.
model/	Los datos de la app. Usuario es la entidad de Room (una tabla para ambos roles) y Rol es el enum SOLICITANTE / OFERTANTE. Solicitudes y Ofertas son modelos base para funcionalidades futuras.
repository/	Acceso a datos. AppDatabase es la base de datos Room, UsuarioDao define las consultas y UsuarioRepository es el intermediario que usan los ViewModels.
viewModel/	La lógica de cada pantalla: LogInScreenViewModel, SignInScreenViewModel y ProfileScreenViewModel, cada uno con su UiState (el estado que la pantalla dibuja). Validaciones concentra las reglas de los formularios.
ui/theme/	Todo lo visual. Las pantallas (LogInScreen, SignInScreen, SolicitanteScreen, OfertanteScreen, DetalleSolicitudScreen, ProfileScreen), la navegación (Navegacion, que incluye la pantalla de bienvenida animada) y el tema (Theme, Color, Type) con los componentes compartidos: encabezado con logo, barra inferior y colores de campos.
res/drawable/	Recursos gráficos, entre ellos el logo logo_agriserv.png.
Flujo de navegación
Bienvenida animada
   ├─ sin sesión ──► Inicio de sesión ◄──► Registro
   │                       │
   └─ con sesión ──────────┤
                           ▼
        Inicio del productor  /  Inicio del aplicador
                           │
                  Detalle de solicitud
                           │
                         Perfil ──► cámara / galería
Tecnologías
Kotlin 2.2 y Jetpack Compose con Material 3
Navigation Compose
Room 2.8 (procesador de anotaciones con el plugin legacy-kapt)
ViewModel y StateFlow para el estado
Activity Result API para cámara, galería y permisos
Android Gradle Plugin 9.2, minSdk 24, compileSdk 37
Cómo ejecutar el proyecto
Clonar el repositorio:
git clone https://github.com/Stroker-nb/AgriServApp_DAM.git
Abrirlo en Android Studio y esperar a que termine la sincronización de Gradle.
Elegir un emulador o un teléfono Android (API 24 o superior) y pulsar Run.
En el primer uso, crear una cuenta desde "Crear una cuenta" e iniciar sesión.
Consejos para probar
Si cambias columnas de una entidad de Room, sube el número de version en AppDatabase; si no, la app se cierra al abrir con datos antiguos.
En el emulador, la galería aparece vacía. Arrastra una imagen .jpg a la ventana del emulador para probar la galería.
Para ver la base de datos, usa App Inspection → Database Inspector (tabla usuarios).
Para ver las estrellas de calificación, edita la columna calificacion de un usuario ofertante, con un valor como 4.5, y vuelve a abrir el perfil.
Datos de prueba
Todos los datos de la aplicación son ficticios. No se utilizan nombres, RUT, teléfonos ni correos reales. Un RUT con formato válido para pruebas es 12.345.678-5.

Herramientas colaborativas
GitHub: control de versiones, con commits distribuidos entre los integrantes.
Trello: planificación y distribución de tareas. Enlace: agregar aquí el enlace del tablero.
