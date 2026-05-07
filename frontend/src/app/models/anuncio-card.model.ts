export interface AnuncioCardAfinidad {
  aceptaMascotas: boolean;
  permiteFumar: boolean;
  lgtbi: boolean;
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
}
