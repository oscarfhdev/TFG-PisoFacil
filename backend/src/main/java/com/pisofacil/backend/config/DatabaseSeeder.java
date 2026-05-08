package com.pisofacil.backend.config;

import com.pisofacil.backend.model.Foto;
import com.pisofacil.backend.model.Habitacion;
import com.pisofacil.backend.model.Piso;
import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.FotoRepository;
import com.pisofacil.backend.repository.HabitacionRepository;
import com.pisofacil.backend.repository.PisoRepository;
import com.pisofacil.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PisoRepository pisoRepository;
    private final HabitacionRepository habitacionRepository;
    private final FotoRepository fotoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            System.out.println("🏠 DataLoader: La base de datos ya contiene datos. Saltando seed.");
            return;
        }

        Random random = new Random(42); // Seed fija para reproducibilidad

        // ═══════════════════════════════════════════════════════════════
        // 1. USUARIOS (Admin + User de prueba + 30 usuarios normales)
        // ═══════════════════════════════════════════════════════════════

        // --- Usuario Admin ---
        Usuario admin = Usuario.builder()
                .nombre("Administrador")
                .apellidos("Sistema")
                .email("admin@pisofacil.com")
                .password(passwordEncoder.encode("admin1234"))
                .esAdmin(true)
                .fechaNacimiento(LocalDate.of(1990, 1, 15))
                .genero("Masculino")
                .estudios("Ingeniería Informática")
                .biografia("Administrador de la plataforma PisoFácil.")
                .fotoPerfilUrl("/api/uploads/fotos/usuarios/hombre/16.jpg")
                .esFumador(false)
                .tieneMascota(false)
                .tienePareja(false)
                .perfilLgtbi(false)
                .build();
        usuarioRepository.save(admin);

        // --- Usuario Normal de prueba ---
        Usuario userTest = Usuario.builder()
                .nombre("Usuario")
                .apellidos("Normal")
                .email("user@pisofacil.com")
                .password(passwordEncoder.encode("user1234"))
                .esAdmin(false)
                .fechaNacimiento(LocalDate.of(1998, 6, 20))
                .genero("Femenino")
                .estudios("Psicología")
                .biografia("Buscando piso compartido para el próximo curso universitario.")
                .fotoPerfilUrl("/api/uploads/fotos/usuarios/mujer/16.jpg")
                .esFumador(false)
                .tieneMascota(true)
                .tienePareja(false)
                .perfilLgtbi(false)
                .build();
        usuarioRepository.save(userTest);

        // --- 30 Usuarios con datos realistas ---
        String[] nombres = {
                "Lucía García", "Pablo Martínez", "María López", "Carlos Fernández",
                "Ana Rodríguez", "Javier Sánchez", "Laura Pérez", "Daniel Gómez",
                "Sara Ruiz", "Alejandro Díaz", "Elena Moreno", "Hugo Álvarez",
                "Claudia Romero", "Sergio Navarro", "Andrea Torres", "Raúl Domínguez",
                "Paula Vázquez", "Adrián Ramos", "Carmen Gil", "Marcos Molina",
                "Isabel Ortega", "Álvaro Serrano", "Natalia Blanco", "Diego Castro",
                "Sofía Morales", "Óscar Jiménez", "Marina Cortés", "Iván Guerrero",
                "Irene Cano", "Francisco Herrera"
        };

        String[] estudios = {
                "Medicina", "Derecho", "ADE", "Ingeniería Industrial",
                "Enfermería", "Arquitectura", "Filología Inglesa", "Veterinaria",
                "Biología", "Informática", "Magisterio", "Periodismo",
                "Farmacia", "Física", "Historia del Arte", "Economía",
                "Trabajo Social", "Matemáticas", "Química", "Traducción",
                "Fisioterapia", "Psicología", "Bellas Artes", "Criminología",
                "Turismo", "Sociología", "Filosofía", "Óptica",
                "Ciencias Ambientales", "Relaciones Laborales"
        };

        String[] biografias = {
                "Soy una persona tranquila y ordenada. Me gusta estudiar con música de fondo.",
                "Busco compañeros de piso respetuosos. Me encanta cocinar para todos.",
                "Estudiante de último año. Responsable y limpio. Horarios de mañana.",
                "Deportista, madrugador y amante de la vida sana. Busco ambiente tranquilo.",
                "Amante de los animales y la naturaleza. Busco piso pet-friendly.",
                "Me gusta salir los fines de semana pero entre semana soy muy tranquilo/a.",
                "Erasmus incoming. Buscando piso cerca de la universidad para el próximo curso.",
                "Friki de los videojuegos pero silencioso. Respeto total por el espacio común.",
                "Persona sociable. Me encanta organizar cenas de compañeros de piso.",
                "Estudiante trabajador/a. Paso poco tiempo en casa pero mantengo todo limpio.",
                "Me encanta leer y ver series. Busco compañeros con gustos similares.",
                "Músico/a amateur. Practico con auriculares, ¡prometido!",
                "Vegano/a y concienciado/a con el medio ambiente. Reciclo siempre.",
                "Bookworm y amante del café. Las mañanas de domingo son sagradas.",
                "Gimnasio a las 7am y estudiar hasta las 10pm. Disciplina y respeto.",
                "Persona creativa, artista y algo bohemia. Busco un espacio inspirador.",
                "Extrovertido/a pero respetuoso/a con las horas de descanso.",
                "Cocino fatal pero lavo mis platos a tiempo. Busco alguien que cocine bien 😄",
                "Runner y senderista. Los domingos los paso fuera de casa explorando.",
                "Cinéfilo/a empedernido/a. Noche de pelis los viernes en el salón.",
                "Busco un piso donde reine la armonía y el buen rollo.",
                "Estudiante de doctorado. Necesito silencio absoluto para concentrarme.",
                "Me adapto a todo tipo de convivencia. Lo importante es comunicarse.",
                "Fan de la repostería. Aviso: habrá tartas caseras frecuentemente 🍰",
                "Alérgico al desorden. Me gusta que las zonas comunes estén impecables.",
                "Skater y fotógrafo/a amateur. Busco piso con buena luz natural.",
                "Practica yoga y meditación. Ambiente zen asegurado.",
                "Gamer nocturno pero con auriculares. No molesto a nadie, prometido.",
                "Me encanta viajar. Algunos fines de semana no estaré en el piso.",
                "Persona tranquila que valora su intimidad pero siempre dispuesta a ayudar."
        };

        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String nombreCompleto = nombres[i];
            String[] partes = nombreCompleto.split(" ", 2);
            String nombre = partes[0];
            String apellidos = partes.length > 1 ? partes[1] : "";

            String genero = (i % 2 == 0) ? "Femenino" : "Masculino";
            String carpetaGenero = (i % 2 == 0) ? "mujer" : "hombre";
            int fotoNum = (i / 2) + 1;
            String fotoUrl = "/api/uploads/fotos/usuarios/" + carpetaGenero + "/" + fotoNum + ".jpg";

            Usuario u = Usuario.builder()
                    .nombre(nombre)
                    .apellidos(apellidos)
                    .email(nombreCompleto.toLowerCase().replace(" ", ".").replace("á", "a")
                            .replace("é", "e").replace("í", "i").replace("ó", "o")
                            .replace("ú", "u") + i + "@correo.com")
                    .password(passwordEncoder.encode("password123"))
                    .esAdmin(false)
                    .fechaNacimiento(LocalDate.of(
                            1996 + random.nextInt(7),
                            1 + random.nextInt(12),
                            1 + random.nextInt(28)))
                    .genero(genero)
                    .estudios(estudios[i])
                    .biografia(biografias[i])
                    .fotoPerfilUrl(fotoUrl)
                    .esFumador(random.nextBoolean())
                    .tieneMascota(random.nextBoolean())
                    .tienePareja(random.nextBoolean())
                    .perfilLgtbi(random.nextBoolean())
                    .build();

            usuarios.add(usuarioRepository.save(u));
        }

        System.out.println("✅ DataLoader: " + (usuarios.size() + 2) + " usuarios creados.");

        // ═══════════════════════════════════════════════════════════════
        // 2. PISOS Y HABITACIONES
        // ═══════════════════════════════════════════════════════════════

        String[][] pisoData = {
                {"Calle de la Princesa, 25", "Madrid", "28008"},
                {"Carrer d'Aragó, 150", "Barcelona", "08011"},
                {"Calle Pedro Antonio de Alarcón, 12", "Granada", "18002"},
                {"Calle Libreros, 4", "Salamanca", "37008"},
                {"Avenida de Blasco Ibáñez, 60", "Valencia", "46021"},
                {"Calle San Fernando, 10", "Sevilla", "41004"},
                {"Calle Licenciado Poza, 30", "Bilbao", "48001"},
                {"Rúa Nova de Abaixo, 15", "Santiago de Compostela", "15701"},
                {"Paseo de la Castellana, 100", "Madrid", "28046"},
                {"Carrer de Balmes, 200", "Barcelona", "08006"},
                {"Calle Cruz Conde, 12", "Córdoba", "14001"},
                {"Calle de la Puebla, 5", "Burgos", "09004"},
                {"Calle Mayor, 1", "Madrid", "28013"},
                {"Carrer de Marina, 300", "Barcelona", "08025"},
                {"Calle Recogidas, 15", "Granada", "18005"},
                {"Avenida de la Constitución, 20", "Sevilla", "41001"},
                {"Calle Urzaiz, 50", "Vigo", "36201"},
                {"Calle Portales, 10", "Logroño", "26001"},
                {"Calle Alfonso I, 8", "Zaragoza", "50003"},
                {"Calle Marqués de Larios, 5", "Málaga", "29005"},
                {"Calle Juan de Quesada, 2", "Las Palmas", "35001"},
                {"Calle Real, 40", "A Coruña", "15003"},
                {"Calle de Colón, 15", "Valencia", "46004"},
                {"Avenida de la Libertad, 5", "San Sebastián", "20004"},
                {"Calle Toro, 10", "Salamanca", "37002"},
                {"Calle Santiago, 12", "Valladolid", "47001"},
                {"Calle San Vicente, 20", "Oviedo", "33003"},
                {"Calle de los Herreros, 4", "Zamora", "49001"},
                {"Calle Laurel, 12", "Logroño", "26001"},
                {"Avenida de Ansite, 10", "Santa Cruz de Tenerife", "38001"}
        };

        String[] descripcionesPiso = {
                "Espectacular piso compartido situado en el emblemático barrio de Argüelles, a escasos minutos a pie de la Universidad Complutense. La vivienda cuenta con un salón señorial de techos altos, cocina totalmente equipada con electrodomésticos de alta gama y un ambiente de estudio inmejorable. Ideal para estudiantes de máster que busquen tranquilidad y cercanía al centro.",
                "Amplio y luminoso piso en pleno Eixample barcelonés. Conserva los suelos hidráulicos originales y techos con molduras. Las zonas comunes incluyen una galería acristalada perfecta para relajarse. Excelente conexión con líneas de metro L1 y L2. Buscamos compañeros respetuosos que valoren la arquitectura clásica y el orden.",
                "Vivienda pensada por y para estudiantes en el corazón de Granada. Situado en la calle Pedro Antonio de Alarcón, el piso es el epicentro de la vida universitaria. Reformado recientemente con mobiliario moderno, Smart TV en el salón y conexión WiFi 6 de alta velocidad. ¡El mejor ambiente joven de la ciudad!",
                "Vive la historia de Salamanca desde este piso situado justo frente a la fachada de la Universidad. Edificio histórico rehabilitado con todas las comodidades modernas. Cuenta con calefacción central (fundamental para el invierno salmantino) y una cocina de concepto abierto donde compartir cenas con el resto de compañeros.",
                "Increíble ubicación en la Avenida Blasco Ibáñez. Este piso destaca por su luminosidad y su proximidad tanto a las facultades del Campus de Tarongers como a la zona de ocio nocturno y la playa. Gran terraza común con vistas a la avenida y cocina con dos frigoríficos para mayor comodidad de los inquilinos.",
                "Piso señorial en el centro de Sevilla, a un paso de la Puerta Jerez y la Universidad de Sevilla. Habitaciones amplias, suelos de mármol y aire acondicionado centralizado. El ambiente en la casa es tranquilo, enfocado a estudiantes de último curso o opositores que necesiten un entorno de silencio y respeto.",
                "Moderno apartamento en el ensanche bilbaíno. A 5 minutos de la parada de metro de Moyua y rodeado de servicios. El piso ha sido reformado íntegramente este año, contando con mobiliario de diseño nórdico, lavavajillas y zona de lavandería independiente. Perfecto para jóvenes profesionales o estudiantes de Deusto.",
                "Típico piso compostelano en la zona nueva, con amplias estancias y mucha luz natural. Muy bien comunicado con el Campus Sur de la USC. La cocina es el corazón de la casa, espaciosa y con una gran mesa de madera donde los compañeros suelen compartir el desayuno. Gastos de comunidad y agua incluidos en el precio.",
                "Vivienda exclusiva en pleno Paseo de la Castellana. Urbanización con seguridad 24h y excelentes calidades. El piso cuenta con un salón de 40m2 con ventanales de suelo a techo, cocina de diseño y climatización por suelo radiante. Una opción premium para quienes buscan confort y prestigio en la capital.",
                "Piso totalmente reformado en la calle Balmes, una de las arterias principales de Barcelona. El inmueble dispone de una distribución inteligente que garantiza la privacidad de cada habitación. Zonas comunes minimalistas y funcionales con limpieza semanal incluida en las zonas comunes.",
                "Encantador piso en la calle Cruz Conde, la zona más comercial y vibrante de Córdoba. A un paseo de la Facultad de Filosofía y Letras. Destaca por su patio cordobés privado de uso exclusivo para los inquilinos, ideal para las noches de primavera. Mobiliario rústico renovado y aire acondicionado en todas las estancias.",
                "Piso acogedor y muy cálido en el centro de Burgos. Cuenta con un sistema de calefacción central excepcional y ventanas de climalit de triple vidrio para asegurar el máximo confort térmico. Ambiente familiar y tranquilo, perfecto para estudiantes de la UBU que busquen sentirse como en casa.",
                "Vivir en la calle Mayor es vivir la esencia de Madrid. Este piso ofrece una experiencia urbana total, a un paso de la Puerta del Sol. A pesar de su ubicación céntrica, es sorprendentemente silencioso gracias a su orientación. Cocina equipada con horno, microondas y cafetera de cápsulas.",
                "Magnífico ático cerca de la Sagrada Familia. La joya de la vivienda es su terraza privada de 20m2 con vistas directas al templo. El piso tiene una decoración bohemia y artística, ideal para perfiles creativos que busquen inspiración. Muy bien conectado con las líneas azul y morada de metro.",
                "Piso de lujo en la calle Recogidas, Granada. Reformado con materiales de primera calidad, suelos de tarima y paredes lisas. Todas las estancias son exteriores y cuentan con mucha luz. Cocina con isla central de mármol y todos los pequeños electrodomésticos necesarios (tostadora, batidora, hervidor).",
                "Vivienda reformada en la Avenida de la Constitución, Sevilla. Vistas inmejorables a la Catedral desde el salón. El piso combina elementos clásicos con una decoración contemporánea. Se busca un perfil de inquilino ordenado y responsable. Ambiente internacional y multicultural.",
                "Piso con vistas panorámicas a la Ría de Vigo en la calle Urzaiz. Muy espacioso, con un gran salón-comedor donde se organizan noches de cine entre compañeros. Edificio con conserje y recogida de basuras. Situado en la zona comercial con todos los servicios a la mano.",
                "Ubicación inmejorable en la calle Portales, Logroño. El piso es muy luminoso y cuenta con una reforma de estilo industrial con paredes de ladrillo visto. A poca distancia de la zona de tapeo y de las facultades de la Universidad de La Rioja. Ambiente dinámico y joven.",
                "Elegante piso en la calle Alfonso I, el corazón de Zaragoza. Edificio histórico rehabilitado con un portal impresionante. El piso cuenta con techos decorados con molduras, calefacción individual y una cocina muy moderna que contrasta con el estilo clásico de la vivienda.",
                "Piso moderno y funcional en la calle Marqués de Larios, Málaga. Disfruta de vivir en la calle más famosa de la ciudad, a un paso del puerto y de la playa de la Malagueta. Aire acondicionado en todas las habitaciones, fundamental para el verano malagueño. Mobiliario nuevo a estrenar.",
                "Vivienda con sabor canario en el barrio de Vegueta, Las Palmas. Techos altísimos de madera y grandes ventanales. El piso tiene una atmósfera zen y tranquila, ideal para estudiantes que teletrabajen o necesiten concentración. Muy cerca de la catedral y de las zonas de museos.",
                "Piso espacioso y señorial en la calle Real de A Coruña. Vistas laterales al mar y mucha luz de tarde. La cocina es enorme y está totalmente equipada para los amantes de la gastronomía. Edificio con ascensor y portal reformado sin barreras arquitectónicas.",
                "Exclusivo piso en la calle Colón de Valencia. Una de las zonas más seguras y prestigiosas. El piso ha sido diseñado por un decorador de interiores, buscando el equilibrio entre elegancia y funcionalidad. Servicio de portería y recogida de paquetes incluido.",
                "Vivir en la Avenida de la Libertad de San Sebastián es un privilegio. A escasos metros de la Playa de la Concha. El piso es sobrio, elegante y muy silencioso. Cuenta con una gran librería en el salón compartida para todos los inquilinos. Ambiente de estudio serio.",
                "Piso recién reformado en la calle Toro, Salamanca. Todo el mobiliario, camas y electrodomésticos han sido comprados este año. Diseño funcional pensado para el aprovechamiento máximo del espacio. Muy cerca de la Plaza Mayor y de las facultades del campus centro.",
                "Apartamento moderno en la calle Santiago de Valladolid. Muy luminoso gracias a su altura. Cuenta con un salón amplio con sofá chaiselongue y televisión 4K para uso común. Cocina con placa de inducción y horno pirolítico. Se busca compañero para larga estancia.",
                "Piso con carácter en el casco antiguo de Oviedo. Muros de piedra vista que mantienen la temperatura ideal durante todo el año. El interior ha sido reformado con un estilo contemporáneo y minimalista. Ambiente muy acogedor y tranquilo para el descanso.",
                "Vivienda práctica y funcional en el centro de Zamora. Ideal para estudiantes que busquen una opción económica sin renunciar a la calidad. Habitaciones bien equipadas y cocina con mesa de comedor. Muy cerca de la estación de autobuses y tren.",
                "Piso con mucho encanto en el centro de Logroño. Destaca por su galería acristalada llena de plantas que aporta una luz especial a toda la casa. El ambiente entre los compañeros es muy familiar, suelen compartir gastos de limpieza y compra común.",
                "Ático luminoso en Santa Cruz de Tenerife con balcones que ofrecen vistas despejadas al mar. El piso es muy fresco gracias a la ventilación cruzada y cuenta con toldos eléctricos. Decoración fresca y veraniega, con plantas naturales y mucha luz natural durante todo el día."
        };

        String[] centrosInteresSeed = {
                "Universidad Complutense de Madrid", "Universidad de Barcelona", "Universidad de Granada (Fuentenueva)",
                "Universidad de Salamanca", "Universidad Politécnica de Valencia", "Universidad de Sevilla",
                "Universidad de Deusto", "Universidad de Santiago de Compostela", "IE University",
                "ESADE Business School", "Universidad de Córdoba", "Universidad de Burgos",
                "Universidad Autónoma de Madrid", "Universidad Pompeu Fabra", "Universidad de Granada (Cartuja)",
                "Universidad de Sevilla (Campus Viapol)", "Universidad de Vigo", "Universidad de La Rioja",
                "Universidad de Zaragoza", "Universidad de Málaga", "Universidad de Las Palmas",
                "Universidad de A Coruña", "Universidad de Valencia (Tarongers)", "Universidad del País Vasco",
                "Universidad de Salamanca (Campus Canalejas)", "Universidad de Valladolid", "Universidad de Oviedo",
                "Universidad de Salamanca (Campus Zamora)", "Universidad de La Rioja", "Universidad de La Laguna"
        };

        String[] titulosHabitacion = {
                "Habitación luminosa con escritorio amplio",
                "Dormitorio exterior con armario empotrado",
                "Habitación con balcón y vistas a la calle",
                "Dormitorio acogedor con buena ventilación",
                "Habitación espaciosa con vestidor",
                "Habitación con baño privado incluido",
                "Dormitorio silencioso orientación norte",
                "Habitación reformada con suelo de parquet",
                "Dormitorio principal con dos ventanas",
                "Habitación con aire acondicionado propio",
                "Dormitorio ideal para estudiar en paz",
                "Habitación amueblada lista para entrar a vivir",
                "Habitación con zona de estudio separada",
                "Dormitorio con cama doble y mesitas",
                "Habitación acogedora y muy cálida en invierno",
                "Dormitorio con vistas al patio interior",
                "Habitación minimalista y moderna",
                "Dormitorio con estantería y escritorio de madera",
                "Habitación con luz natural todo el día",
                "Dormitorio amplio con espacio para sofá",
                "Habitación con cama canapé de gran capacidad",
                "Dormitorio interior muy silencioso",
                "Habitación ideal para estudiantes de máster",
                "Dormitorio con televisión inteligente",
                "Habitación con nevera ejecutiva privada",
                "Dormitorio tipo suite con zona de estar",
                "Habitación con decoración industrial",
                "Dormitorio con gran espejo de cuerpo entero",
                "Habitación en piso con terraza compartida",
                "Dormitorio con cerradura independiente"
        };

        String[] descripcionesHabitacion = {
                "Habitación diseñada para el máximo rendimiento académico. Cuenta con un escritorio de 160cm de largo, silla ergonómica de oficina y una estantería de gran capacidad para libros. La ventana es amplia y recibe luz solar directa desde media mañana, ideal para no encender la luz hasta el atardecer.",
                "Dormitorio exterior muy espacioso con un armario empotrado de tres cuerpos totalmente vestido por dentro. La cama cuenta con un canapé para almacenamiento extra. Orientación este, por lo que disfrutarás de unos amaneceres espectaculares. Suelo de madera natural que aporta calidez a la estancia.",
                "Habitación con encanto que dispone de balcón privado hacia la calle peatonal. Incluye mesa y silla de exterior para poder desayunar o estudiar al aire libre. La estancia es muy luminosa y cuenta con cortinas opacas para garantizar el descanso. Techos altos con vigas decorativas.",
                "Dormitorio muy tranquilo situado en la zona interior del piso, lejos del ruido de la calle. Es ideal para personas con sueño ligero o que necesiten silencio absoluto para concentrarse. Equipada con cama individual de alta gama, cómoda de cuatro cajones y perchero auxiliar para abrigos.",
                "Habitación tipo suite de grandes dimensiones que incluye un vestidor independiente con espejo de cuerpo entero. La iluminación LED es regulable en intensidad para crear diferentes ambientes. Incluye alfombra de pelo corto y una butaca de lectura junto a la lámpara de pie. Un lujo en el centro.",
                "Máxima privacidad con tu propio baño integrado en la habitación. El baño ha sido reformado recientemente con plato de ducha de resina y grifería termostática. La habitación cuenta además con un armario de puertas correderas de espejo y cama doble de 135cm con almohada viscoelástica.",
                "Dormitorio orientado al norte, manteniendo una temperatura fresca y agradable incluso en los meses más calurosos. Cuenta con una decoración en tonos blancos y madera clara que potencia la sensación de amplitud. Escritorio junto al radiador para las sesiones de estudio invernales.",
                "Estancia recién pintada con suelo de parquet de roble recién pulido. Dispone de un gran espejo de pared y estanterías flotantes para organización. La cama tiene un colchón de muelles ensacados que garantiza un soporte firme y cómodo. Ventana de doble acristalamiento para aislamiento total.",
                "Dormitorio principal de la vivienda que destaca por tener dos grandes ventanales que inundan la estancia de luz. Cuenta con un rincón de estar con un pequeño sofá y mesa auxiliar. El armario es de estilo vintage restaurado, aportando mucha personalidad a la habitación.",
                "Habitación equipada con split de aire acondicionado de bajo consumo y mando a distancia. Incluye una Smart TV de 32 pulgadas anclada a la pared para disfrutar de tus series con total comodidad desde la cama. Mobiliario moderno en blanco brillante y escritorio con organizador de cables.",
                "El rincón perfecto para un opositor o estudiante de máster. La habitación está insonorizada acústicamente y cuenta con iluminación técnica para el estudio. Dispone de una pizarra blanca de cristal y una cajonera con llave para documentos importantes. Ambiente de paz asegurado.",
                "Habitación totalmente amueblada y equipada con ropa de cama, edredón nórdico y juego de toallas de algodón. El escritorio incluye lámpara flexo y conexión por cable Ethernet además de WiFi. Armario con perchas incluidas y zapatero auxiliar tras la puerta.",
                "Estancia singular con la zona de estudio claramente diferenciada de la zona de descanso mediante una estantería divisoria. Esto ayuda a desconectar después de las horas de trabajo o estudio. Cama de 105cm con cabecero tapizado en gris y mesita de noche con cargador USB.",
                "Habitación espaciosa con cama doble de 150cm y mesitas de noche a ambos lados con lámparas de lectura individuales. Ideal para quienes valoran el espacio al dormir. Incluye una cómoda amplia para ropa doblada y un perchero estilo burro para la ropa de diario.",
                "Dormitorio muy acogedor gracias a su orientación sur y el sistema de calefacción individual. Cuenta con una decoración en tonos cálidos, alfombra de lana y plantas naturales que purifican el aire. Escritorio de madera maciza y silla tapizada muy cómoda.",
                "Habitación luminosa con vistas al patio interior ajardinado del edificio, lo que garantiza ausencia total de ruidos de tráfico. Despertarás con el sonido de los pájaros en pleno centro. Armario de dos puertas y escritorio de cristal templado muy moderno.",
                "Estancia de diseño minimalista con muebles de líneas nórdicas y funcionales. Dispone de una gran pizarra de corcho para notas y fotos. La cama cuenta con un protector de colchón de alta calidad y edredón de temporada. Lámpara de techo regulable en temperatura de color.",
                "Habitación con mobiliario de madera natural que incluye una estantería de pared a pared, ideal para amantes de la lectura o coleccionistas. El escritorio es muy robusto y cuenta con mucha profundidad. Incluye puf de relax en una esquina de la habitación.",
                "Dormitorio con luz natural radiante durante todo el día gracias a su gran ventanal sin obstáculos enfrente. Cuenta con persianas eléctricas para un control total de la luz. Mesa de estudio amplia con silla de ruedas profesional y cajonera metálica.",
                "Estancia muy amplia que permite tener una pequeña zona de salón propia con una butaca y mesita de centro. Es casi como un pequeño estudio dentro del piso compartido. Armario empotrado de gran altura que aprovecha hasta el último centímetro de almacenaje.",
                "Habitación práctica con cama tipo canapé de gran capacidad, ideal para guardar maletas o ropa de otra temporada. El colchón es nuevo y de firmeza media-alta. Escritorio plegable que permite ganar espacio cuando no se está estudiando. Armario de puertas blancas.",
                "Dormitorio interior muy silencioso y recogido, perfecto para personas que buscan un ambiente de recogimiento. La iluminación artificial ha sido cuidada para que sea cálida y acogedora. Cuenta con un gran espejo vestidor y un armario modular muy bien organizado.",
                "Estancia pensada para perfiles senior o de postgrado que busquen un entorno serio y profesional. Mobiliario de alta calidad, colores neutros y conexión a internet por cable de alta velocidad. Se requiere un perfil responsable para esta habitación.",
                "Habitación tecnológica que incluye televisión inteligente con aplicaciones de streaming instaladas, altavoz inteligente y regleta con protección de picos para el ordenador. Escritorio con iluminación LED ambiental personalizable en colores.",
                "Dormitorio con el lujo de tener una pequeña nevera ejecutiva propia para guardar bebidas y snacks. Ideal para mayor comodidad y privacidad. Incluye también un pequeño microondas personal. Cama individual con canapé y escritorio de esquina.",
                "Habitación tipo suite con una zona de estar integrada que incluye un sillón orejero y una mesita para el café. Es la habitación más grande del piso y ofrece una independencia total. Armario de cuatro puertas y grandes ventanales con visillos.",
                "Estancia con una decoración de estilo industrial muy marcada: paredes de ladrillo visto, lámparas de filamento y muebles de hierro y madera. Muy moderna y con mucha personalidad. Escritorio amplio y cama con base tapizada negra.",
                "Dormitorio que destaca por su gran espejo de cuerpo entero y una zona de tocador o escritorio auxiliar. Muy buena iluminación cenital y lámpara de mesa para ambiente. Armario con puertas correderas de cristal opaco muy elegante.",
                "Habitación con acceso directo y exclusivo a una parte de la terraza compartida, permitiendo tener tu propio rincón exterior. Cama doble, armario de gran capacidad y mesa de estudio orientada hacia la terraza para disfrutar de las vistas mientras trabajas.",
                "Máxima seguridad y privacidad para tu tranquilidad: la habitación dispone de puerta con cerradura independiente y llave propia. Estancia totalmente reformada con acabados de primera, mobiliario nuevo y una distribución muy aprovechada."
        };

        int totalHabitacionesCreadas = 0;
        for (int i = 0; i < 20; i++) {
            // Asignar un usuario dueño aleatorio de los 30 creados
            Usuario dueno = usuarios.get(random.nextInt(usuarios.size()));

            int numBanos = 1 + random.nextInt(2); // 1 o 2
            int planta = random.nextInt(6);       // 0 a 5
            double superficie = 55 + random.nextInt(71); // 55-125 m²

            Piso piso = Piso.builder()
                    .usuario(dueno)
                    .direccion(pisoData[i][0])
                    .ciudad(pisoData[i][1])
                    .codigoPostal(pisoData[i][2])
                    .numHabitacionesTotal((i < 10) ? 2 : 1) // Sincronizado con las habitaciones reales
                    .numBanos(numBanos)
                    .planta(planta)
                    .superficieTotalM2(superficie)
                    .tieneWifi(true)
                    .tieneAscensor(planta > 1 || random.nextBoolean())
                    .descripcionGlobal(descripcionesPiso[i])
                    .admiteFumadores(random.nextInt(4) == 0)   // ~25%
                    .admiteMascotas(random.nextInt(3) == 0)     // ~33%
                    .admiteParejas(random.nextBoolean())
                    .lgtbiFriendly(random.nextInt(3) != 0)      // ~66%
                    .centroInteres(centrosInteresSeed[i % centrosInteresSeed.length])
                    .build();

            Piso pisoGuardado = pisoRepository.save(piso);

            // ───────────────────────────────────────────────────
            // 2.1 FOTOS DEL PISO (3 fotos por piso)
            // ───────────────────────────────────────────────────
            int pisoNum = i + 1;
            for (int f = 1; f <= 3; f++) {
                Foto fotoPiso = Foto.builder()
                        .piso(pisoGuardado)
                        .habitacion(null)
                        .urlAlmacenamiento("/api/uploads/fotos/piso-" + pisoNum + "/" + f + ".jpg")
                        .esPrincipal(f == 1) // La primera foto es la portada del piso
                        .build();
                fotoRepository.save(fotoPiso);
            }

            // ───────────────────────────────────────────────────
            // 2.2 HABITACIONES (Total 30 entre los 20 pisos)
            // ───────────────────────────────────────────────────
            int numHabs = (i < 10) ? 2 : 1; // 10 pisos con 2 habs y 10 con 1 hab = 30 total
            
            for (int h = 0; h < numHabs; h++) {
                totalHabitacionesCreadas++; // Contador global para las carpetas 1-30
                
                int tituloIdx = random.nextInt(titulosHabitacion.length);
                int descIdx = random.nextInt(descripcionesHabitacion.length);
                BigDecimal precio = BigDecimal.valueOf(180 + random.nextInt(221)); // 180-400 €

                Habitacion hab = Habitacion.builder()
                        .piso(pisoGuardado)
                        .tituloAnuncio(titulosHabitacion[tituloIdx])
                        .precioMensual(precio)
                        .descripcionEspecifica(descripcionesHabitacion[descIdx])
                        .estaDisponible(random.nextInt(5) != 0) // 80% disponibles
                        .superficieM2(8.0 + random.nextInt(13))  // 8-20 m²
                        .tieneBanoPrivado(random.nextInt(4) == 0) // ~25%
                        .amueblada(random.nextInt(3) != 0)        // ~66%
                        .exterior(random.nextBoolean())
                        .tieneCalefaccion(random.nextBoolean())
                        .tieneAireAcondicionado(random.nextBoolean())
                        .build();

                Habitacion habGuardada = habitacionRepository.save(hab);

                // ─────────────────────────────────────────────
                // 2.3 FOTOS DE HABITACIÓN (Secuencial 1-30)
                // ─────────────────────────────────────────────
                int carpetaHab = totalHabitacionesCreadas; 
                for (int fh = 1; fh <= 2; fh++) {
                    Foto fotoHab = Foto.builder()
                            .piso(pisoGuardado)
                            .habitacion(habGuardada)
                            .urlAlmacenamiento("/api/uploads/fotos/hab-" + carpetaHab + "/" + fh + ".jpg")
                            .esPrincipal(false) // Solo los pisos tienen foto principal en el E-R
                            .build();
                    fotoRepository.save(fotoHab);
                }
            }
        }

        System.out.println("✅ DataLoader: 20 pisos con habitaciones y fotos creados correctamente en toda España.");
        System.out.println("🏠 DataLoader: Seed completado. Base de datos lista.");
    }

    /**
     * Devuelve un color hex aleatorio (sin #) para los avatares de UI Avatars.
     */
    private String getRandomColor(Random random) {
        String[] colores = {
                "6366f1", "ec4899", "f59e0b", "10b981", "3b82f6",
                "8b5cf6", "ef4444", "14b8a6", "f97316", "06b6d4",
                "a855f7", "84cc16", "e11d48", "0ea5e9", "d946ef"
        };
        return colores[random.nextInt(colores.length)];
    }
}
