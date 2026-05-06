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
}
