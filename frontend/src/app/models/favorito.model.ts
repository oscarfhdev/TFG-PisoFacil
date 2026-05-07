export interface FavoritoRequest {
  idUsuario: number;
  idHabitacion: number;
}

export interface FavoritoResponse {
  idFavorito: number;
  idUsuario: number;
  idHabitacion: number;
  tituloAnuncio: string;
  precioMensual: number;
  ciudad: string;
  direccion?: string;
  fotoPrincipal?: string;
  superficieM2?: number;
  estaDisponible?: boolean;
  admiteMascotas?: boolean;
  admiteFumadores?: boolean;
  lgtbiFriendly?: boolean;
  fotoPerfilUrlPropietario?: string;
}
