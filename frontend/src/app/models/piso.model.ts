export interface PisoRequest {
  idUsuario: number;
  direccion: string;
  ciudad: string;
  codigoPostal?: string;
  numHabitacionesTotal?: number;
  numBanos?: number;
  planta?: number;
  superficieTotalM2?: number;
  tieneWifi?: boolean;
  tieneAscensor?: boolean;
  descripcionGlobal?: string;
  admiteFumadores?: boolean;
  admiteMascotas?: boolean;
  admiteParejas?: boolean;
  lgtbiFriendly?: boolean;
}

export interface PisoResponse {
  idPiso: number;
  idUsuario: number;
  nombreUsuario: string;
  direccion: string;
  ciudad: string;
  codigoPostal?: string;
  numHabitacionesTotal?: number;
  numBanos?: number;
  planta?: number;
  superficieTotalM2?: number;
  tieneWifi?: boolean;
  tieneAscensor?: boolean;
  descripcionGlobal?: string;
  admiteFumadores?: boolean;
  admiteMascotas?: boolean;
  admiteParejas?: boolean;
  lgtbiFriendly?: boolean;
  fechaCreacion: string;
}
