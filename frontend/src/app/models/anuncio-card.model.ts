export interface AnuncioCardAfinidad {
  aceptaMascotas: boolean;
  permiteFumar: boolean;
  lgtbi: boolean;
  admiteParejas: boolean;
}

export interface AnuncioCardData {
  id: number;
  tipo: 'piso' | 'habitacion';
  titulo: string;
  precio: number;
  ciudad: string;
  direccion: string;
  imagenUrl: string;
  numHabitaciones?: number;
  numBanos?: number;
  superficieM2?: number;
  estaDisponible: boolean;
  disponibleYa: boolean;
  gastosIncluidos: boolean;
  avatarPropietario: string;
  afinidad: AnuncioCardAfinidad;
  esFavorito?: boolean;
  
  // Nuevos campos para iconos extra
  amueblada?: boolean;
  exterior?: boolean;
  tieneCalefaccion?: boolean;
  tieneAireAcondicionado?: boolean;
  tieneWifi?: boolean;
  tieneAscensor?: boolean;
  tieneBanoPrivado?: boolean;

  // Motor de Compatibilidad
  porcentajeCompatibilidad?: number;
}

