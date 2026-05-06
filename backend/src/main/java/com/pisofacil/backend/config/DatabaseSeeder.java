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
import java.time.LocalDateTime;
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
                .fotoPerfilUrl("https://ui-avatars.com/api/?name=Administrador&background=6366f1&color=fff&size=200")
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
                .fotoPerfilUrl("https://ui-avatars.com/api/?name=Usuario+Normal&background=ec4899&color=fff&size=200")
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

        String[] generos = {"Masculino", "Femenino", "No binario"};

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
                "Práctica yoga y meditación. Ambiente zen asegurado.",
                "Gamer nocturno pero con auriculares. No molesto a nadie, prometido.",
                "Me encanta viajar. Algunos fines de semana no estaré en el piso.",
                "Persona tranquila que valora su intimidad pero siempre dispuesta a ayudar."
        };

        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String nombre = nombres[i];
            String avatarUrl = "https://ui-avatars.com/api/?name=" + nombre.replace(" ", "+")
                    + "&background=" + getRandomColor(random) + "&color=fff&size=200";

            String[] partes = nombre.split(" ", 2);
            String soloNombre = partes[0];
            String apellidos = partes.length > 1 ? partes[1] : "";

            Usuario u = Usuario.builder()
                    .nombre(soloNombre)
                    .apellidos(apellidos)
                    .email(nombre.toLowerCase().replace(" ", ".").replace("á", "a")
                            .replace("é", "e").replace("í", "i").replace("ó", "o")
                            .replace("ú", "u") + "@correo.com")
                    .password(passwordEncoder.encode("password123"))
                    .esAdmin(false)
                    .fechaNacimiento(LocalDate.of(
                            1996 + random.nextInt(7),  // Nacidos entre 1996-2002
                            1 + random.nextInt(12),
                            1 + random.nextInt(28)))
                    .genero(generos[random.nextInt(generos.length)])
                    .estudios(estudios[i])
                    .biografia(biografias[i])
                    .fotoPerfilUrl(avatarUrl)
                    .esFumador(random.nextBoolean())
                    .tieneMascota(random.nextBoolean())
                    .tienePareja(random.nextBoolean())
                    .perfilLgtbi(random.nextBoolean())
                    .build();

            usuarios.add(usuarioRepository.save(u));
        }

        System.out.println("✅ DataLoader: " + (usuarios.size() + 2) + " usuarios creados.");

        // ═══════════════════════════════════════════════════════════════
        // 2. PISOS (30) — Varias ciudades de España (Madrid, Barcelona, Granada, Salamanca, etc.)
        // ═══════════════════════════════════════════════════════════════

        String[][] pisoData = {
                // {direccion, ciudad, codigoPostal}
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
                "Piso compartido en el barrio de Argüelles, Madrid. Muy cerca de la Complutense.",
                "Habitaciones en el Eixample de Barcelona. Piso amplio con mucha luz y techos altos.",
                "Ideal para estudiantes en Granada. Zona Pedro Antonio, ambiente universitario garantizado.",
                "Piso histórico frente a la Universidad de Salamanca. Ubicación inmejorable.",
                "Avenida Blasco Ibáñez en Valencia. A un paso de todas las facultades y de la playa.",
                "Vivir en el centro de Sevilla. Cerca de la Universidad de Sevilla (Antigua Fábrica de Tabacos).",
                "Piso en el centro de Bilbao. Cerca de la Universidad de Deusto y bien comunicado.",
                "Santiago de Compostela. Zona nueva, ideal para estudiantes de la USC.",
                "Zona Norte de Madrid. Piso moderno cerca de las torres y bien comunicado por metro.",
                "Barcelona centro. Conexión directa con todas las líneas de metro y tren.",
                "Piso luminoso en el centro de Córdoba. Ideal para estudiantes de Filosofía o Derecho.",
                "Burgos centro. Piso acogedor con calefacción central, imprescindible aquí.",
                "En el corazón de Madrid. Cerca de Sol y de todas las zonas de ocio.",
                "Cerca de la Sagrada Familia. Piso con terraza y vistas increíbles.",
                "Granada centro. Piso reformado con aire acondicionado y calefacción.",
                "Sevilla histórica. Cerca de la Catedral y de las zonas de estudio.",
                "Centro de Vigo. Piso amplio con vistas a la ría y muy soleado.",
                "Logroño centro. Muy cerca de la zona universitaria y de la calle Laurel.",
                "Zaragoza centro. Calle peatonal muy tranquila, ideal para el estudio.",
                "Málaga centro. A 10 minutos de la playa y cerca del campus de El Ejido.",
                "Vegueta en Las Palmas. Piso con encanto canario cerca de la universidad.",
                "A Coruña centro. Cerca del Orzán y de las facultades de Riazor.",
                "Valencia comercial. Piso de lujo reformado para compartir entre estudiantes.",
                "Donostia centro. Cerca de la playa de la Concha y del campus universitario.",
                "Zona comercial de Salamanca. Edificio rehabilitado con todas las comodidades.",
                "Valladolid centro. Muy cerca de la Plaza Mayor y de las facultades.",
                "Oviedo antiguo. Piso con historia pero totalmente reformado por dentro.",
                "Zamora centro. Piso económico y funcional para estudiantes.",
                "Ambiente riojano en el centro de Logroño. Piso recién pintado.",
                "Santa Cruz de Tenerife. Piso luminoso con balcones y vistas al mar."
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
                "Habitación perfecta para concentrarse. Escritorio grande junto a la ventana con mucha luz natural.",
                "Armario empotrado de doble puerta. Orientación exterior que garantiza ventilación y luminosidad.",
                "Balcón privado ideal para desayunar por las mañanas. Ambiente muy agradable.",
                "Habitación tranquila en la parte interior del piso. Ideal para quienes necesitan silencio.",
                "Amplio vestidor con organización completa. No necesitarás más espacio para tu ropa.",
                "Lujo de tener baño privado. Ducha, lavabo y espejo grande. No compartirás con nadie.",
                "Orientación norte que mantiene la habitación fresca en verano. Muy importante en el sur.",
                "Suelo de parquet auténtico recién pulido. Elegancia y calidez bajo tus pies.",
                "Dos ventanas que proporcionan ventilación cruzada. Muy agradable en primavera.",
                "Split de aire acondicionado propio. Tú controlas tu temperatura en todo momento.",
                "Rincón de estudio perfecto. Silencio garantizado y buena iluminación para largas sesiones.",
                "Todo incluido: cama, escritorio, silla ergonómica, lámpara y cortinas. Solo trae tu ropa.",
                "Zona de estudio separada del área de descanso. Productividad y relax en un solo espacio.",
                "Cama doble de 150cm con colchón viscoelástico. Dormirás como nunca.",
                "Calefacción individual y alfombra mullida. Los inviernos aquí son súper cómodos.",
                "Vistas al patio interior con plantas. Despertarse aquí es un placer.",
                "Diseño minimalista con muebles de líneas puras. Para quienes aprecian la sencillez.",
                "Muebles de madera maciza de roble. Estilo nórdico que invita a quedarse.",
                "Ventana grande orientación sur. Luz natural desde primera hora hasta el atardecer.",
                "Espacio suficiente para incluir un pequeño sofá o sillón de lectura junto a la cama.",
                "Cama con canapé para almacenar maletas y ropa de cama sin ocupar espacio en la habitación.",
                "Da a un patio interior sin ruidos de la calle. Perfecta si tienes sueño ligero.",
                "Ambiente de estudio y tranquilidad. Se busca compañero/a responsable y enfocado/a.",
                "Incluye Smart TV en la habitación para que disfrutes de tus series con privacidad.",
                "Pequeña nevera en la habitación para tus snacks y bebidas frías. Máxima comodidad.",
                "Espacio muy amplio que incluye una pequeña zona de estar con butaca y mesita.",
                "Paredes de ladrillo visto y muebles de metal/madera. Un estilo moderno y urbano.",
                "Espejo grande y buena iluminación. Ideal para arreglarse cómodamente.",
                "La habitación tiene acceso directo a una terraza compartida con el resto de compañeros.",
                "Puerta con cerradura propia para garantizar tu privacidad y seguridad en todo momento."
        };

        for (int i = 0; i < 20; i++) {
            // Asignar un usuario dueño aleatorio de los 30 creados
            Usuario dueno = usuarios.get(random.nextInt(usuarios.size()));

            int numHabs = 2 + random.nextInt(3); // entre 2 y 4
            int numBanos = 1 + random.nextInt(2); // 1 o 2
            int planta = random.nextInt(6);       // 0 a 5
            double superficie = 55 + random.nextInt(71); // 55-125 m²

            Piso piso = Piso.builder()
                    .usuario(dueno)
                    .direccion(pisoData[i][0])
                    .ciudad(pisoData[i][1])
                    .codigoPostal(pisoData[i][2])
                    .numHabitacionesTotal(numHabs)
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
                        .urlAlmacenamiento("http://localhost:8080/uploads/fotos/piso-" + pisoNum + "/" + f + ".jpg")
                        .build();
                fotoRepository.save(fotoPiso);
            }

            // ───────────────────────────────────────────────────
            // 2.2 HABITACIONES (2-4 por piso)
            // ───────────────────────────────────────────────────
            for (int h = 0; h < numHabs; h++) {
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
                // 2.3 FOTOS DE HABITACIÓN (2 fotos, carpeta aleatoria 1-30)
                // ─────────────────────────────────────────────
                int carpetaHab = 1 + random.nextInt(30); // K entre 1 y 30
                for (int fh = 1; fh <= 2; fh++) {
                    Foto fotoHab = Foto.builder()
                            .piso(pisoGuardado)
                            .habitacion(habGuardada)
                            .urlAlmacenamiento("http://localhost:8080/uploads/fotos/hab-" + carpetaHab + "/" + fh + ".jpg")
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
