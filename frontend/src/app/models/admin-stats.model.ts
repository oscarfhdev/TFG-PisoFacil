export interface UsuarioConPisos {
  idUsuario: number;
  nombre: string;
  apellidos?: string;
  email: string;
  fotoPerfilUrl?: string;
  cantidadPisos: number;
}

export interface AdminStats {
  // Totales globales
  totalUsuarios: number;
  totalPisos: number;
  totalHabitaciones: number;
  totalReportes: number;

  // Habitaciones por disponibilidad
  habitacionesDisponibles: number;
  habitacionesOcupadas: number;

  // Reportes por estado
  reportesPendientes: number;
  reportesResueltos: number;
  reportesRechazados: number;

  // Usuarios activos / suspendidos
  usuariosActivos: number;
  usuariosSuspendidos: number;

  // Distribución geográfica
  pisosPorCiudad: Record<string, number>;

  // Distribución por rango de precio
  habitacionesPorRangoPrecio: Record<string, number>;

  // Top publicadores
  topPublicadores: UsuarioConPisos[];
}
