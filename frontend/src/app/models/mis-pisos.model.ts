import { HabitacionResponse } from './habitacion.model';

export interface MisPisosResponse {
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
  habitaciones: HabitacionResponse[];
}
