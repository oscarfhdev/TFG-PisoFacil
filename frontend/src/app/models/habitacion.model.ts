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
}
