export interface HabitacionRequest {
  idPiso: number;
  tituloAnuncio: string;
  precioMensual: number;
  descripcionEspecifica?: string;
  estaDisponible?: boolean;
  superficieM2?: number;
  tieneBanoPrivado?: boolean;
  amueblada?: boolean;
  exterior?: boolean;
  tieneCalefaccion?: boolean;
  tieneAireAcondicionado?: boolean;
}

export interface BusquedaFiltros {
  ciudad?: string;
  precioMin?: number;
  precioMax?: number;
  tieneBanoPrivado?: boolean;
  exterior?: boolean;
  tieneAireAcondicionado?: boolean;
  tieneCalefaccion?: boolean;
  amueblada?: boolean;
  tieneWifi?: boolean;
  tieneAscensor?: boolean;
  numHabitacionesMax?: number;
  centroInteres?: string;
}

export interface HabitacionResponse {
  idHabitacion: number;
  idPiso: number;
  ciudad: string;
  direccion: string;
  tituloAnuncio: string;
  precioMensual: number;
  descripcionEspecifica?: string;
  estaDisponible: boolean;
  fechaPublicacion: string;
  superficieM2?: number;
  tieneBanoPrivado?: boolean;
  amueblada?: boolean;
  exterior?: boolean;
  tieneCalefaccion?: boolean;
  tieneAireAcondicionado?: boolean;
  
  // Mapeos de Piso
  descripcionGlobal?: string;
  numHabitacionesTotal?: number;
  tieneWifi?: boolean;
  tieneAscensor?: boolean;
  admiteMascotas?: boolean;
  admiteFumadores?: boolean;
  lgtbiFriendly?: boolean;
  admiteParejas?: boolean;
  centroInteres?: string;

  // Mapeos de Propietario
  idUsuarioPropietario?: number;
  nombrePropietario?: string;
  apellidosPropietario?: string;
  fotoPerfilUrlPropietario?: string;
  instagramUrlPropietario?: string;

  // Fotos
  fotosHabitacion?: string[];
  fotosPiso?: string[];

  // Motor de Compatibilidad
  porcentajeCompatibilidad?: number;
}
