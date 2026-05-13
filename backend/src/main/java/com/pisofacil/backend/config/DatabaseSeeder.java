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

            // Instagram solo para ~60% de los usuarios (diversidad)
            String instagram = (i % 5 != 0) ? "@" + nombre.toLowerCase() + "." + apellidos.toLowerCase().split(" ")[0] : null;
            // Teléfono para ~70% de los usuarios
            String telefono = (i % 3 != 0) ? "6" + String.format("%08d", 10000000 + i * 3214567) : null;

            Usuario u = Usuario.builder()
                    .nombre(nombre)
                    .apellidos(apellidos)
                    .email(nombreCompleto.toLowerCase().replace(" ", ".").replace("á", "a")
                            .replace("é", "e").replace("í", "i").replace("ó", "o")
                            .replace("ú", "u") + i + "@correo.com")
                    .telefono(telefono)
                    .instagramUrl(instagram)
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
                "Piso compartido muy bien ubicado en la zona de Argüelles, a menos de diez minutos andando de la Complutense. Somos tres personas jóvenes entre estudiantes y trabajadores, el ambiente es muy tranquilo y nos llevamos genial. La cocina está totalmente equipada con microondas, horno, lavavajillas y una nevera grande. Tenemos un salón bastante amplio con sofá de esquina donde solemos ver alguna peli los viernes por la noche. Los turnos de limpieza se respetan siempre y hay un cuadrante en la puerta de la cocina. Los gastos de comunidad están incluidos en el alquiler, pero la luz, el agua y el gas se pagan aparte divididos entre todos, suele salir por unos 35-40 euros al mes cada uno dependiendo de la época del año.",
                "Buscamos compañero/a para nuestro piso en el Eixample barcelonés. Es un piso amplio de techos altos con suelos hidráulicos originales y mucha personalidad. No somos de fiestas en casa entre semana, preferimos un ambiente tranquilo para estudiar o trabajar desde casa. La cocina tiene vitrocerámica, horno y una mesa donde solemos desayunar juntos. Tenemos dos baños completos, lo cual es bastante cómodo. El WiFi de fibra óptica está incluido en el precio, pero la luz y el agua van aparte según factura real que nos llega cada dos meses. La zona está muy bien comunicada con metro y autobuses, y tenemos un Mercadona a dos calles.",
                "Piso ideal para universitarios en pleno centro de Granada, justo al lado de la famosa calle Pedro Antonio de Alarcón donde está todo el ambiente estudiantil. El piso fue reformado hace dos años con muebles nuevos y un estilo moderno que mola bastante. Las habitaciones son todas exteriores, así que entra mucha luz natural. Tenemos WiFi de alta velocidad incluido en el precio porque sabemos que es fundamental para estudiar. La cocina es bastante amplia y tiene todo lo necesario. El salón es acogedor, con una Smart TV donde vemos partidos o series. Los gastos de luz y agua se dividen entre todos de forma justa.",
                "¿Te apetece vivir justo enfrente de la Universidad de Salamanca? Nuestro piso está en un edificio antiguo rehabilitado que tiene un encanto especial, con su portal de piedra y todo. La vivienda está muy bien cuidada y cuenta con calefacción central, que aquí en Salamanca es fundamental porque los inviernos son bastante duros. La cocina es de concepto abierto y solemos hacer cenas de compañeros de vez en cuando. Tenemos un salón con buena luz y un sofá grande. Los gastos de calefacción están incluidos en la comunidad, pero la luz va aparte. Buscamos a alguien para todo el curso que sea responsable y buen compañero.",
                "Vivienda en la Avenida Blasco Ibáñez, perfecta para estudiantes de la UPV o de la UV porque ambos campus quedan muy cerca. El piso es bastante grande con casi 100 metros cuadrados y tiene una terraza donde da el sol prácticamente todo el día, que es genial para tender la ropa o tomar algo por las tardes. Estamos a un paso de la zona de ocio pero el piso es tranquilo por dentro. Tenemos dos baños para compartir entre los que vivimos, así que por las mañanas no hay agobios. La cocina tiene dos frigoríficos porque somos varios y así cada uno tiene su espacio. Luz y agua aparte.",
                "Piso señorial en pleno centro de Sevilla, a pocos minutos de la Puerta de Jerez y de las facultades del centro. Es una casa con mucho carácter, suelos de mármol y techos altos con molduras. El ambiente en casa es muy tranquilo y silencioso, perfecto si estás opositando, preparando un máster o simplemente necesitas concentrarte. Tenemos aire acondicionado centralizado que en Sevilla es absolutamente imprescindible de mayo a octubre. Los gastos de electricidad van aparte porque el aire consume bastante y cada uno gestiona su uso. Buscamos a gente responsable y madura.",
                "Compartimos piso en el centro de Bilbao, estamos muy cerca de la parada de Moyua y del Casco Viejo. El piso es moderno porque se reformó hace poco y tiene un estilo nórdico con muebles de IKEA que queda muy bien. Tenemos zona de lavandería independiente con lavadora y tendedero interior para los días de lluvia, que aquí en Bilbao ya sabéis que son muchos. La cocina tiene lavavajillas, que es un lujo en un piso compartido. Los suministros de luz y gas se pagan aparte divididos entre todos. Buscamos a alguien joven que trabaje o estudie y que sea limpio en las zonas comunes.",
                "Piso acogedor en la zona nueva de Santiago de Compostela, una zona con mucha vida estudiantil, supermercados cerca y buenas comunicaciones con el Campus Sur de la USC. El piso es muy cálido gracias a la calefacción de gas y las ventanas de doble cristal. La cocina es el corazón de la casa, tiene una mesa grande de madera donde solemos desayunar juntos y compartir conversaciones. Los gastos de comunidad y agua están incluidos en el precio del alquiler, la luz y el gas van aparte. Buscamos a alguien tranquilo que quiera un hogar de verdad para todo el curso.",
                "Vivimos en la zona norte de Madrid, cerca de Paseo de la Castellana, y nos sobra una habitación que queremos llenar con alguien majo. El piso es moderno con calefacción central incluida y un salón bastante grande con ventanales de suelo a techo que dan mucha luminosidad. La cocina está bien equipada con electrodomésticos buenos. Es un barrio tranquilo con buen aparcamiento si tienes coche y hay un supermercado abajo. Los gastos de luz van aparte. Buscamos a alguien trabajador que quiera un sitio tranquilo para descansar después de la jornada laboral.",
                "Piso reformado en la calle Balmes de Barcelona, una de las calles principales del Eixample. Somos tres compañeros de entre 22 y 26 años que nos llevamos muy bien. Buscamos a alguien de edad similar para que el ambiente sea fluido y natural. La limpieza de las zonas comunes es fundamental para nosotros, tenemos un cuadrante de turnos semanales que se respeta a rajatabla. La cocina es funcional y tiene todo lo necesario. WiFi de fibra incluido. Los gastos de luz y agua van aparte según la factura que llega cada mes.",
                "Piso con mucho encanto en el centro de Córdoba, en la calle Cruz Conde que es la zona más comercial. Lo mejor que tiene es un patio cordobés privado donde solemos cenar en las noches de verano, es una maravilla. Estamos cerca de todas las facultades del centro y del casco histórico. Las habitaciones son amplias y exteriores. Tenemos aire acondicionado en todas las estancias, que en Córdoba es totalmente necesario. Los suministros de electricidad y agua van aparte del alquiler. Buscamos a alguien sociable y buen compañero.",
                "Buscamos compañero/a para nuestro piso en el centro de Burgos. Es una zona residencial muy tranquila y el piso es súper calentito gracias a la calefacción individual de gas que funciona de maravilla. Tenemos ventanas de doble acristalamiento que aíslan fenomenal del frío burgalés. El salón es amplio y cómodo, perfecto para ver pelis o estudiar en el sofá. La cocina tiene horno, microondas y espacio de sobra. Los gastos de calefacción y luz van aparte y los dividimos entre todos. Buscamos a alguien para larga estancia que quiera sentirse como en casa.",
                "¿Te imaginas vivir al lado de la Puerta del Sol en Madrid? Nuestro piso es súper céntrico pero da a un patio interior, así que no se oyen ruidos de la calle para nada. Es bastante sorprendente lo silencioso que es para estar donde está. Somos estudiantes de máster y opositores, así que el ambiente es de estudio y trabajo. La cocina está equipada con horno, microondas y cafetera de cápsulas que compartimos. Tenemos WiFi rápido incluido. Los gastos de luz van aparte y se dividen de forma justa.",
                "Ático increíble cerca de la Sagrada Familia en Barcelona. Lo mejor de todo es la terraza privada de unos 20 metros cuadrados con vistas directas al templo, que de noche iluminado es una pasada. Solemos subir a estudiar, tomar algo o simplemente charlar al atardecer. El ambiente del piso es artístico y relajado, nos consideramos un piso LGTBI-friendly y abierto a todo tipo de perfiles. La cocina es pequeña pero funcional. Estamos bien conectados con metro. Los suministros van aparte del alquiler.",
                "Piso de estudiantes en Granada, zona Recogidas, que es una de las calles más céntricas y bien comunicadas. El piso está muy bien, con muebles nuevos que compramos este año y mucha luz natural en todas las habitaciones. Tenemos aire acondicionado individual en cada habitación, que en Granada se agradece mucho en verano. La cocina tiene una isla central muy práctica y está completamente equipada. Los gastos de luz y agua no están incluidos en el precio y se dividen entre todos. Buscamos gente con ganas de disfrutar del ambiente granadino respetando el descanso.",
                "Compartimos piso en la Avenida de la Constitución de Sevilla, con vistas espectaculares a la Catedral desde el salón que te dejan sin palabras. Es un piso muy grande y señorial con habitaciones amplias y bien ventiladas. El ambiente es multicultural porque hemos tenido compañeros de varios países y nos gusta esa diversidad. Buscamos un perfil de inquilino ordenado y responsable, no se permiten fiestas en casa pero sí visitas normales. Los gastos de luz y aire acondicionado van aparte del alquiler.",
                "Piso espacioso en la calle Urzaiz de Vigo, con vistas a la Ría que son bastante bonitas especialmente al atardecer. Tenemos un salón-comedor muy bien montado con sofá grande y tele, donde organizamos noches de cine entre compañeros de vez en cuando. El edificio tiene conserje y servicio de recogida de basuras, lo cual es muy cómodo. Estamos en plena zona comercial con todos los servicios a mano. Los suministros se pagan aparte. Buscamos a alguien que trabaje o estudie por la zona.",
                "Ubicación inmejorable en la calle Portales de Logroño, que es el centro absoluto de la ciudad. El piso es muy luminoso gracias a los grandes ventanales y cuenta con una reforma de estilo moderno con algunos toques industriales que le dan personalidad. Estamos a poca distancia de las facultades de la Universidad de La Rioja y de la famosa zona de tapeo de la calle Laurel. El ambiente del piso es dinámico y joven. Los gastos de internet están incluidos en el precio, la luz va aparte.",
                "Piso céntrico en la calle Alfonso I de Zaragoza, que es el corazón histórico de la ciudad. Es una casa antigua que se reformó con muy buen gusto, conservando los techos con molduras y el suelo original pero con una cocina moderna que contrasta genial. El ambiente es muy tranquilo porque todos estudiamos o trabajamos y nos respetamos las horas de estudio. Tenemos calefacción individual que cada uno regula. Los suministros de luz y gas van aparte del alquiler divididos entre todos.",
                "Piso funcional y muy bien ubicado en la calle Marqués de Larios de Málaga, la calle más famosa de la ciudad. Vivir aquí es una pasada por la cercanía absoluta a todo: playas, centro comercial, puerto y zonas de ocio. Tenemos aire acondicionado en todas las habitaciones que es totalmente necesario para sobrevivir al verano malagueño. El mobiliario es nuevo y moderno. Los gastos de electricidad van aparte del alquiler. Buscamos a alguien joven para completar el piso, se pide fianza de un mes.",
                "Vivienda con sabor canario en el barrio de Vegueta de Las Palmas. Los techos son altísimos de madera y las ventanas enormes dejan entrar una luz natural espectacular durante todo el día. Es un piso con un ambiente zen y tranquilo, ideal para estudiantes que teletrabajen o necesiten mucha concentración. Estamos cerca de la catedral y de la zona de museos. La cocina es amplia y funcional. Los gastos de agua y luz van aparte, aunque aquí no se gasta mucho en calefacción por el clima.",
                "Piso espacioso y señorial en la calle Real de A Coruña, con vistas laterales al mar y mucha luz natural especialmente por las tardes. La cocina es enorme y está totalmente equipada, nos gusta cocinar juntos los fines de semana y compartir cenas de grupo. El edificio tiene ascensor y portal reformado. Buscamos a alguien que sea limpio, participe de la vida del piso y tenga ganas de buen rollo. Los suministros se dividen entre todos mensualmente.",
                "Piso en la calle Colón de Valencia, una de las zonas más seguras y bien comunicadas de la ciudad. El piso ha sido decorado por un interiorista y tiene un estilo muy cuidado que combina elegancia y funcionalidad. Tenemos portero físico y servicio de recogida de paquetes. Los gastos de suministros se dividen entre todos cada mes de forma transparente. Buscamos un perfil de estudiante serio o joven trabajador que valore la buena convivencia.",
                "Vivir en la Avenida de la Libertad de San Sebastián es un auténtico privilegio. Estamos a escasos metros de la Playa de la Concha y del centro de la ciudad. El piso es sobrio, elegante y sorprendentemente silencioso para estar donde está. Tenemos una gran librería en el salón compartida por todos los inquilinos, lo que le da un toque muy acogedor. El ambiente es de estudio serio. No se permite fumar dentro. Los suministros van aparte.",
                "Piso recién reformado en la calle Toro de Salamanca, a un paseo de la Plaza Mayor y de las facultades del campus centro. Todo el mobiliario, las camas y los electrodomésticos son nuevos de este año. El diseño del piso es funcional y pensado para aprovechar al máximo cada metro cuadrado. Tenemos calefacción central incluida que es importantísima aquí. Los gastos de luz van aparte. Buscamos a alguien joven para el próximo cuatrimestre.",
                "Apartamento moderno y luminoso en la calle Santiago de Valladolid, una zona céntrica y bien comunicada. El piso tiene bastante altura así que entra mucha luz por las ventanas. El salón es amplio con sofá chaiselongue y televisión 4K para uso común. La cocina tiene placa de inducción, horno y espacio para que cocinemos todos sin agobios. Los gastos de suministros van aparte. Buscamos a alguien para compartir los próximos meses, preferiblemente larga estancia.",
                "Piso con mucho carácter en el casco antiguo de Oviedo. Las paredes son de piedra vista original y el interior combina ese estilo rústico con una reforma contemporánea y minimalista que queda muy bonita. El piso mantiene una temperatura ideal durante todo el año gracias a los muros gruesos. El ambiente es muy acogedor y tranquilo. La limpieza de zonas comunes se hace por turnos semanales. Los gastos de suministros van aparte entre todos.",
                "Vivienda práctica y funcional en el centro de Zamora, a pocos minutos de la estación de autobuses y tren. Es ideal para estudiantes que busquen algo con buena relación calidad-precio sin renunciar a vivir cómodos. Las habitaciones son amplias y la cocina tiene mesa de comedor para comer todos juntos. Tenemos WiFi de fibra incluido en el alquiler. Los gastos de luz y agua van aparte y suelen ser bastante económicos.",
                "Piso con mucho encanto en el centro de Logroño, muy cerca de la calle Laurel. Lo más especial es su galería acristalada llena de plantas que aporta una luz natural preciosa a toda la casa y que cuidamos entre todos. El ambiente entre los compañeros es muy familiar y solemos compartir gastos de limpieza y alguna compra común. La cocina es amplia y está bien equipada. Los suministros de luz y agua van aparte.",
                "Ático luminoso en Santa Cruz de Tenerife con balcones que ofrecen vistas despejadas al mar, despertarse aquí es una maravilla cada mañana. El piso es muy fresco gracias a la ventilación cruzada natural y los toldos eléctricos para los días de sol intenso. La decoración es fresca y veraniega, con plantas naturales por toda la casa. El ambiente es relajado y sociable. Los gastos de agua y luz van aparte del alquiler, aunque aquí no se gasta prácticamente nada en calefacción."
        };

        String[] centrosInteresSeed = {
                "Universidad Complutense de Madrid", "Universidad de Barcelona", "Universidad de Granada (Fuentenueva)",
                "Universidad de Salamanca", "Universidad Politécnica de Valencia", "Universidad de Sevilla",
                "Universidad de Deusto", "Universidad de Santiago de Compostela", "IE University",
                "ESADE Business School", "Universidad de Córdoba", "Universidad de Burgos",
                "Universidad Autónoma de Madrid", "Universidad Pompeu Fabra", "Universidad de Granada (Cartuja)",
                "Universidad de Sevilla (Campus Viapol)", "Universidad de Vigo", "Universidad de La Rioja",
                "Universidad Zaragoza", "Universidad Málaga", "Universidad Las Palmas",
                "Universidad A Coruña", "Universidad Valencia", "Universidad País Vasco",
                "Univ. Salamanca", "Univ. Valladolid", "Univ. Oviedo", "Univ. Zamora", "Univ. Rioja", "Univ. Laguna"
        };

        String[] titulosHabitacion = {
                "Habitación luminosa con escritorio grande",
                "Dormitorio exterior cerca de la universidad",
                "Habitación cómoda en piso compartido",
                "Dormitorio con mucha luz y buen ambiente",
                "Habitación tranquila para estudiantes",
                "Dormitorio con baño propio y armario",
                "Habitación con cama doble en el centro",
                "Dormitorio luminoso con vistas a la calle",
                "Habitación económica con todo incluido",
                "Dormitorio moderno en piso reformado",
                "Habitación con balcón privado",
                "Habitación para parejas - Muy amplia",
                "Dormitorio acogedor en zona tranquila",
                "Habitación con cerradura y WiFi",
                "Dormitorio con estanterías y luz",
                "Habitación grande con vistas al centro",
                "Dormitorio compartido - Ambiente joven",
                "Habitación para entrar ya - Todo nuevo",
                "Dormitorio con aire acondicionado",
                "Habitación amplia cerca del metro",
                "Habitación para parejas - Bien equipada",
                "Dormitorio interior pero muy silencioso",
                "Habitación con vestidor y escritorio",
                "Dormitorio clásico en piso señorial",
                "Habitación con nevera pequeña privada",
                "Dormitorio moderno - Estudiantes máster",
                "Habitación soleada con buena ventilación",
                "Dormitorio en piso de trabajadores jóvenes",
                "Habitación con vistas al mar y terraza",
                "Habitación segura con llave propia"
        };

        String[] descripcionesHabitacion = {
                "Hola! Alquilamos esta habitación que es bastante luminosa y da a la calle principal. Tiene una cama de 90cm con un colchón que compramos hace poco y un escritorio grande de madera con mucho espacio para el portátil, apuntes y lo que necesites. El armario es de dos puertas y no está nada mal de tamaño, cabe ropa de invierno y verano sin problemas. Los gastos de luz y agua no están incluidos en el alquiler, solemos pagar unos 35€ al mes cada uno aproximadamente dependiendo de la temporada. Buscamos a alguien que sea limpio, ordenado y buen compañero de piso. Si te interesa no dudes en escribirnos por la plataforma, podemos quedar para que veas el piso en persona sin compromiso.",
                "Dormitorio exterior que da a una calle tranquila y peatonal. Tiene mucha luz por las mañanas así que prácticamente no necesitas encender la luz artificial hasta que se pone el sol por la tarde. El armario es empotrado de tres cuerpos y cabe muchísima ropa, además tiene cajones interiores para organizar bien tus cosas. La luz y el gas se pagan aparte cada dos meses según la factura real que nos llega, suele salir por poco la verdad porque somos cuidadosos con el consumo. Buscamos a alguien que estudie o trabaje y que quiera un ambiente sano y de respeto en casa. Nos da igual chico o chica.",
                "Habitación bastante amplia con balcón propio hacia una calle peatonal donde se puede desayunar tranquilamente cuando hace buen tiempo. Tiene cortinas opacas por si quieres dormir a oscuras los fines de semana. El precio del alquiler no incluye los suministros: la luz va aparte según lo que gastemos entre todos dividido de forma equitativa, y el agua está incluida en la comunidad. Somos gente abierta, maja y sociable, buscamos a alguien similar para completar el piso este curso. Lo ideal sería alguien que se quede como mínimo hasta junio.",
                "Dormitorio interior pero con una ventana grande que da a un patio luminoso lleno de plantas del vecino de abajo. Lo mejor de esta habitación es el silencio absoluto que hay, no se oye absolutamente nada de la calle y eso se nota mucho a la hora de estudiar o dormir. Tenemos WiFi de fibra óptica simétrica de 600Mb que va volando, eso sí está incluido en el precio del alquiler. La luz se paga aparte según consumo real. Buscamos a alguien que respete las horas de sueño del resto de compañeros y que sea responsable con la limpieza.",
                "Habitación de buen tamaño con un vestidor independiente separado por una cortina donde cabe toda tu ropa perfectamente organizada. Tiene una cama de 105cm con cabecero tapizado y un escritorio funcional junto a la ventana donde entra buena luz para estudiar. Los gastos de agua y luz se dividen equitativamente cada mes entre todos los que vivimos aquí. El ambiente es de estudio y trabajo durante la semana, nada de fiestas ruidosas. Los fines de semana cada uno hace su vida sin problemas, pero a veces nos juntamos para cenar algo.",
                "Esta habitación tiene su propio baño dentro con ducha, lavabo y espejo grande, que es un lujazo absoluto para un piso compartido porque no tienes que compartirlo con nadie más. Tiene una cama doble de 135cm bastante cómoda y un escritorio nuevo de IKEA con cajones. La luz y el agua se pagan aparte del alquiler, pero al tener baño propio el reparto de gastos se hace de forma diferenciada y justa. Buscamos a alguien para larga estancia, mínimo un curso académico completo porque no queremos estar cambiando de compañero cada dos meses.",
                "Dormitorio orientado al norte, que en esta ciudad es perfecto porque no hace mucho calor en verano y te ahorras el aire acondicionado. Tiene un armario grande de dos puertas con barra y estantes, además de una estantería auxiliar para libros y apuntes de la carrera. La calefacción es eléctrica con radiadores individuales y la pagamos aparte en los meses de invierno, pero suele ser poco porque el piso conserva bien el calor. Somos dos chicos estudiantes y buscamos a un tercer compañero o compañera que encaje con nuestro estilo de vida tranquilo.",
                "Habitación recién pintada en blanco que queda muy limpia y con muebles nuevos que compramos este verano pasado. La cama tiene un colchón de firmeza media bastante cómodo y el escritorio es amplio con espacio para dos pantallas si haces teletrabajo. Los gastos de suministros como luz y agua suelen ser unos 40€ al mes por persona y no están incluidos en la renta mensual. Buscamos a alguien joven de entre 20 y 30 años que tenga ganas de buen rollo y que sea cuidadoso con las zonas comunes del piso.",
                "Esta es la habitación principal del piso y tiene dos ventanales enormes que dan a la calle principal. Entra un sol increíble especialmente por las tardes y la sensación de amplitud es muy agradable. Además del armario tiene una cómoda de cajones y un perchero de pie que queda muy bien. La luz va aparte del alquiler y la dividimos entre todos. Nos gusta hacer vida en el salón compartido pero respetamos muchísimo la intimidad y el espacio de cada uno cuando está en su habitación con la puerta cerrada.",
                "Dormitorio con aire acondicionado individual tipo split con mando a distancia, que en esta ciudad se agradece muchísimo sobre todo en los meses de julio y agosto. También tiene una televisión pequeña de 32 pulgadas por si quieres ver algo cómodamente antes de dormir sin molestar a nadie. Los gastos de luz no están incluidos en el alquiler mensual ya que cada uno controla su propio consumo y así es más justo para todos. Buscamos a alguien que sea ordenado sobre todo en la cocina y en el baño que compartimos.",
                "Habitación perfecta para alguien que necesite mucha concentración para estudiar o que esté opositando. Es súper silenciosa porque da al patio interior del edificio y no se oye nada de ruido. Tiene una lámpara de escritorio profesional con luz regulable muy buena para leer y un escritorio amplio donde cabe de todo. Los suministros se pagan aparte cada mes divididos entre los compañeros. Tenemos un cuadrante de limpieza semanal para que las zonas comunes estén siempre impecables. Solo buscamos gente realmente responsable.",
                "HABITACIÓN PARA PAREJAS. Es bastante grande, de unos 18 metros cuadrados, y tiene una cama de 150cm con colchón nuevo. Si sois dos personas el gasto de suministros sube un poco porque lógicamente se consume más agua y luz, pero se habla tranquilamente entre todos y se llega a un acuerdo justo sin problemas. Tenemos un salón amplio donde hacemos vida. Buscamos a gente sociable y limpia que se integre bien en la dinámica del piso. Venid a conocerlo cuando queráis, sin compromiso.",
                "Habitación con la zona de estudio y la de descanso claramente separadas gracias a una estantería grande que hace de separador visual. Es casi como tener tu propio mini-apartamento dentro del piso compartido, que mola bastante. Tiene cama de 105cm con cabecero tapizado y una mesa de estudio con flexo. La luz se paga aparte del alquiler mensual. Buscamos a alguien que trabaje o esté haciendo un máster porque somos gente muy tranquila durante la semana y necesitamos ese ambiente para rendir.",
                "Habitación bastante acogedora con cama doble de 135cm y mesitas de noche a ambos lados con sus lámparas de lectura individuales. Los gastos de suministros se pagan aparte cada mes divididos entre todos los compañeros de piso de forma transparente. El ambiente del piso es dinámico y respetuoso a la vez, nos gusta hacer cenas de grupo de vez en cuando y compartir momentos en el salón. Buscamos a alguien que pueda entrar a vivir ya o en las próximas semanas.",
                "Dormitorio muy cálido y acogedor con suelo de madera natural que le da un toque especial. Tiene un armario grande de dos puertas con mucho espacio interior y un escritorio moderno con cajones para organizar tus cosas de estudio. La calefacción central está incluida en los gastos de comunidad así que no pagas extra por eso, pero la luz eléctrica sí va aparte y se divide entre todos. Buscamos a alguien que quiera quedarse todo el curso escolar como mínimo para tener estabilidad.",
                "Habitación luminosa que da al patio de manzanas del edificio, así que es tremendamente tranquila para dormir y no hay absolutamente ningún ruido molesto de tráfico. Los gastos de luz y agua se pagan aparte mensualmente divididos entre todos los inquilinos del piso. Nos gusta hacer planes juntos de vez en cuando como ir de cañas después de clase o cenar fuera los viernes. El buen rollo en el piso está garantizado, llevamos viviendo juntos más de un año y nos va genial.",
                "Estancia minimalista y funcional con lo justo y necesario para vivir cómodo: cama individual de 90cm, armario amplio de dos cuerpos y mesa de estudio con flexo LED. Los suministros de luz y agua se pagan aparte del alquiler cada mes. Somos todos estudiantes de entre 20 y 25 años y buscamos a alguien de nuestra edad más o menos para que haya buen feeling y la convivencia sea natural. No nos gustan las fiestas en casa pero sí salir juntos de vez en cuando.",
                "Habitación con muebles de madera muy bonitos estilo nórdico y una estantería enorme de pared a pared con mucho espacio para tus libros, fotos y recuerdos. La luz y el agua no están incluidos en el precio del alquiler pero suele salir bastante económico porque somos pocos viviendo y no gastamos demasiado. Buscamos a alguien que sea educado, tranquilo y que no le importe compartir un rato de charla en la cocina mientras desayunamos por las mañanas.",
                "Dormitorio con luz natural prácticamente todo el día porque estamos en un quinto piso sin edificios altos enfrente que tapen el sol. No se oyen ruidos de la calle para nada gracias a las ventanas de doble acristalamiento que pusieron en la última reforma. Los gastos de la luz van aparte del alquiler y se dividen entre todos. El WiFi de fibra óptica está incluido en el precio. Buscamos a alguien que sea limpio y ordenado, es lo más importante para nosotros a la hora de elegir compañero.",
                "Habitación muy amplia de unos 20 metros cuadrados donde cabe perfectamente un sillón o una butaca de lectura además de la cama y el escritorio. Es la habitación más grande de toda la casa y tiene mucha ventilación natural. La luz se paga aparte del alquiler. Somos trabajadores jóvenes de entre 24 y 28 años con horarios normales de oficina y buscamos a alguien en una situación similar. Muy buen rollo garantizado, nos gusta hacer barbacoas en verano.",
                "HABITACIÓN IDEAL PARA PAREJAS. Tiene muchísimo espacio con una cama de 150cm con canapé abatible para guardar maletas y ropa de temporada debajo. Los gastos de suministros como luz y agua se dividen entre los que vivamos en el piso en ese momento, si sois pareja contáis como dos a la hora de repartir. Buscamos a una pareja joven que sea sociable, limpia y que respete las normas básicas de convivencia del piso que tenemos colgadas en la cocina.",
                "Dormitorio interior muy recogido y silencioso, perfecto para personas que necesitan dormir bien y descansar de verdad después de un día largo de trabajo o estudio. La iluminación artificial está muy bien pensada con luces cálidas LED que crean un ambiente acogedor por las noches. La luz va aparte del alquiler y se paga cada mes. Tenemos un ambiente de confianza total entre los compañeros y nos gusta sentirnos cómodos en casa como si fuera la de toda la vida.",
                "Habitación para alguien que busque algo en buen estado y con calidad. El mobiliario está prácticamente nuevo y el colchón es de este año, a estrenar. Los suministros de luz y agua se facturan aparte del alquiler mensual y se reparten de forma justa entre todos. Buscamos preferiblemente a un perfil de postgrado, investigador o joven profesional que valore la tranquilidad. Imprescindible ser una persona ordenada y respetuosa con los horarios de descanso.",
                "Habitación tecnológica pensada para gente que trabaja o estudia con ordenador. Tiene WiFi de alta velocidad con router en el pasillo que da señal perfecta y varias tomas de corriente junto al escritorio para montar tu setup completo sin problemas. La luz se paga aparte según el consumo que marque la factura bimensual. Ideal si eres gamer con cascos, programador o trabajas desde casa en remoto. Buscamos a alguien respetuoso con los horarios nocturnos porque el resto madrugamos bastante.",
                "Esta habitación tiene su propia nevera pequeña tipo minibar dentro para que tengas tus bebidas y snacks siempre a mano sin ocupar espacio en la nevera grande de la cocina que compartimos entre todos. Es muy cómoda y práctica para el día a día. La luz se paga aparte del alquiler dividida entre los compañeros. Somos gente abierta y con buen rollo, buscamos a alguien que aporte buena energía al piso. Se pide un mes de fianza al entrar que se devuelve íntegra al irse.",
                "Dormitorio moderno y bien organizado con un estilo tipo estudio independiente dentro del piso compartido. Tienes mucha autonomía y espacio propio con escritorio grande, armario empotrado y una zona de relax con un puf. Los suministros de luz y agua no están incluidos en el precio del alquiler mensual y se dividen entre todos. Buscamos a alguien joven y responsable que quiera una estancia larga y estable con nosotros, preferiblemente de varios meses como mínimo.",
                "Habitación con un estilo industrial bastante original que le da mucha personalidad: paredes blancas, muebles en tonos oscuros de hierro y madera, y una lámpara de filamento en la mesita de noche que queda muy bien. La luz va aparte del alquiler y se paga cada mes. Tenemos una cocina muy bien equipada con lavavajillas, vitrocerámica y horno, y nos gusta mantenerla siempre impecable. Buscamos a alguien que comparta ese mismo hábito de limpieza y orden en las zonas comunes.",
                "Habitación en un piso que cuenta con portero físico y mucha seguridad en el portal, es un barrio residencial muy tranquilo donde se puede pasear a cualquier hora sin problemas. Los gastos de suministros se reparten mensualmente entre todos de forma justa y transparente con una hoja de cálculo compartida. Buscamos a alguien educado y maduro que valore la buena convivencia, el silencio por las noches entre semana y la limpieza de las zonas comunes.",
                "Habitación con vistas al mar si te asomas al balcón, que es una maravilla despertarse así cada mañana con la brisa marina y la luz del amanecer. La habitación es fresca y luminosa durante todo el día. La luz y el agua se pagan aparte del alquiler pero aquí no se gasta mucho la verdad porque el clima es suave y no necesitamos calefacción prácticamente nunca. Buscamos a alguien con buena onda que quiera disfrutar de la vida compartida en la isla.",
                "Habitación segura con cerradura propia y llave independiente para tu total tranquilidad y privacidad. Está recién reformada con pintura nueva, enchufes nuevos y todo está listo para entrar a vivir mañana mismo sin necesidad de comprar nada. Los gastos de luz y agua no están incluidos en el alquiler y se dividen entre todos los compañeros. Buscamos a alguien que busque estabilidad, larga estancia y un buen ambiente entre compañeros de piso."
        };

        int totalHabitacionesCreadas = 0;
        for (int i = 0; i < 19; i++) {
            Usuario dueno = usuarios.get(random.nextInt(usuarios.size()));

            int numBanos = 1 + random.nextInt(2); 
            int planta = random.nextInt(6);       
            double superficie = 55 + random.nextInt(71);
            // Variedad de 1 a 4 habitaciones para que todos los filtros devuelvan resultados
            int numHabsTotal = (i % 4) + 1;

            Piso piso = Piso.builder()
                    .usuario(dueno)
                    .direccion(pisoData[i][0])
                    .ciudad(pisoData[i][1])
                    .codigoPostal(pisoData[i][2])
                    .numHabitacionesTotal(numHabsTotal) 
                    .numBanos(numBanos)
                    .planta(planta)
                    .superficieTotalM2(superficie)
                    .tieneWifi(true)
                    .tieneAscensor(planta > 1 || random.nextBoolean())
                    .descripcionGlobal(descripcionesPiso[i])
                    .admiteFumadores(random.nextInt(4) == 0)   
                    .admiteMascotas(random.nextInt(3) == 0)     
                    .admiteParejas(random.nextBoolean())
                    .lgtbiFriendly(random.nextInt(3) != 0)      
                    .centroInteres(centrosInteresSeed[i % centrosInteresSeed.length])
                    .build();

            Piso pisoGuardado = pisoRepository.save(piso);

            int pisoNum = i + 1;
            for (int f = 1; f <= 3; f++) {
                Foto fotoPiso = Foto.builder()
                        .piso(pisoGuardado)
                        .habitacion(null)
                        .urlAlmacenamiento("/api/uploads/fotos/piso-" + pisoNum + "/" + f + ".jpg")
                        .esPrincipal(f == 1) 
                        .build();
                fotoRepository.save(fotoPiso);
            }

            // Crear al menos 1 habitación, y hasta numHabsTotal (máx 2 para el seeder)
            int numHabs = Math.min(numHabsTotal, 2);

            for (int h = 0; h < numHabs; h++) {
                totalHabitacionesCreadas++; 
                
                int tituloIdx = (totalHabitacionesCreadas - 1) % titulosHabitacion.length;
                int descIdx = (totalHabitacionesCreadas - 1) % descripcionesHabitacion.length;
                BigDecimal precio = BigDecimal.valueOf(250 + random.nextInt(251)); 

                boolean aceptaParejas = (totalHabitacionesCreadas % 5 == 0); 

                Habitacion hab = Habitacion.builder()
                        .piso(pisoGuardado)
                        .tituloAnuncio(titulosHabitacion[tituloIdx])
                        .precioMensual(precio)
                        .descripcionEspecifica(descripcionesHabitacion[descIdx])
                        .estaDisponible(random.nextInt(10) != 0) 
                        .superficieM2(10.0 + random.nextInt(15)) 
                        .tieneBanoPrivado(totalHabitacionesCreadas % 4 == 0) 
                        .amueblada(true)
                        .exterior(random.nextBoolean())
                        .tieneCalefaccion(true)
                        .tieneAireAcondicionado(random.nextBoolean())
                        .build();

                if (aceptaParejas) {
                    pisoGuardado.setAdmiteParejas(true);
                    pisoRepository.save(pisoGuardado);
                }

                Habitacion habGuardada = habitacionRepository.save(hab);

                int carpetaHab = ((totalHabitacionesCreadas - 1) % 30) + 1; // Ciclar entre hab-1 y hab-30
                for (int fh = 1; fh <= 2; fh++) {
                    Foto fotoHab = Foto.builder()
                            .piso(pisoGuardado)
                            .habitacion(habGuardada)
                            .urlAlmacenamiento("/api/uploads/fotos/hab-" + carpetaHab + "/" + fh + ".jpg")
                            .esPrincipal(fh == 1) // Primera foto es la principal

                            .build();
                    fotoRepository.save(fotoHab);
                }
            }
        }

        // ═══════════════════════════════════════════════════════════════
        // 3. PISO PROPIO DEL USUARIO DE PRUEBA (user@pisofacil.com)
        // ═══════════════════════════════════════════════════════════════
        Piso pisoUserTest = Piso.builder()
                .usuario(userTest)
                .direccion("Calle Avenida de Ansite, 10")
                .ciudad("Santa Cruz de Tenerife")
                .codigoPostal("38001")
                .numHabitacionesTotal(1)
                .numBanos(1)
                .planta(3)
                .superficieTotalM2(65.0)
                .tieneWifi(true)
                .tieneAscensor(true)
                .descripcionGlobal("Piso luminoso en Santa Cruz de Tenerife con balcones que ofrecen vistas despejadas al mar. El piso es muy fresco gracias a la ventilación cruzada natural y los toldos eléctricos para los días de sol intenso. La decoración es fresca y veraniega, con plantas naturales por toda la casa. El ambiente es relajado y sociable. Los gastos de agua y luz van aparte del alquiler, aunque aquí no se gasta prácticamente nada en calefacción.")
                .admiteFumadores(false)
                .admiteMascotas(true)
                .admiteParejas(false)
                .lgtbiFriendly(true)
                .centroInteres("Universidad de La Laguna")
                .build();
        Piso pisoUserTestGuardado = pisoRepository.save(pisoUserTest);

        // Fotos del piso (carpeta piso-20)
        for (int f = 1; f <= 3; f++) {
            fotoRepository.save(Foto.builder()
                    .piso(pisoUserTestGuardado)
                    .habitacion(null)
                    .urlAlmacenamiento("/api/uploads/fotos/piso-20/" + f + ".jpg")
                    .esPrincipal(f == 1)
                    .build());
        }

        // Habitación del piso de userTest (carpeta hab-30)
        totalHabitacionesCreadas++; // = 30
        Habitacion habUserTest = Habitacion.builder()
                .piso(pisoUserTestGuardado)
                .tituloAnuncio(titulosHabitacion[29])
                .precioMensual(BigDecimal.valueOf(380))
                .descripcionEspecifica(descripcionesHabitacion[29])
                .estaDisponible(true)
                .superficieM2(16.0)
                .tieneBanoPrivado(false)
                .amueblada(true)
                .exterior(true)
                .tieneCalefaccion(false)
                .tieneAireAcondicionado(true)
                .build();
        Habitacion habUserTestGuardada = habitacionRepository.save(habUserTest);

        // Fotos de la habitación (carpeta hab-30)
        for (int fh = 1; fh <= 2; fh++) {
            fotoRepository.save(Foto.builder()
                    .piso(pisoUserTestGuardado)
                    .habitacion(habUserTestGuardada)
                    .urlAlmacenamiento("/api/uploads/fotos/hab-30/" + fh + ".jpg")
                    .esPrincipal(fh == 1)
                    .build());
        }

        System.out.println("✅ DataLoader: 20 pisos con 30 anuncios REALISTAS creados.");
        System.out.println("👤 DataLoader: Piso de user@pisofacil.com creado con hab-30.");
    }

    private String getRandomColor(Random random) {
        String[] colores = {"6366f1", "ec4899", "f59e0b", "10b981", "3b82f6", "8b5cf6", "ef4444", "14b8a6", "f97316", "06b6d4", "a855f7", "84cc16", "e11d48", "0ea5e9", "d946ef"};
        return colores[random.nextInt(colores.length)];
    }
}
