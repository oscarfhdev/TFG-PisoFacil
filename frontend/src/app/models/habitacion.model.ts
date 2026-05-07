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
  admiteMascotas?: boolean;
  admiteFumadores?: boolean;
  lgtbiFriendly?: boolean;
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

  // Mapeos de Propietario
  idUsuarioPropietario?: number;
  nombrePropietario?: string;
  fotoPerfilUrlPropietario?: string;
  instagramUrlPropietario?: string;

  // Fotos
  fotosHabitacion?: string[];
  fotosPiso?: string[];
}
