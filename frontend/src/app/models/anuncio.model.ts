import { PisoResponse } from './piso.model';
import { HabitacionResponse } from './habitacion.model';

export interface PublicarAnuncioRequest {
  // Campos Piso
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
  
  // Campos Habitación
  tituloAnuncioHabitacion: string;
  precioMensualHabitacion: number;
  descripcionEspecificaHabitacion?: string;
  superficieM2Habitacion?: number;
  tieneBanoPrivadoHabitacion?: boolean;
  amuebladaHabitacion?: boolean;
  exteriorHabitacion?: boolean;
  tieneCalefaccionHabitacion?: boolean;
  tieneAireAcondicionadoHabitacion?: boolean;
}

export interface PublicarAnuncioResponse {
  mensaje: string;
  piso: PisoResponse;
  habitacion: HabitacionResponse;
}
