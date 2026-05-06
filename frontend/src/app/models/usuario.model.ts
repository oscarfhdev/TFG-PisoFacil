export interface UsuarioRequest {
  nombre: string;
  email: string;
  password: string;
  fechaNacimiento?: string;
  genero?: string;
  estudios?: string;
  biografia?: string;
  fotoPerfilUrl?: string;
  instagramUrl?: string;
  esFumador?: boolean;
  tieneMascota?: boolean;
  tienePareja?: boolean;
  perfilLgtbi?: boolean;
}

export interface UsuarioResponse {
  idUsuario: number;
  nombre: string;
  email: string;
  esAdmin: boolean;
  fechaNacimiento?: string;
  genero?: string;
  estudios?: string;
  biografia?: string;
  fotoPerfilUrl?: string;
  instagramUrl?: string;
  fechaRegistro: string;
  esFumador?: boolean;
  tieneMascota?: boolean;
  tienePareja?: boolean;
  perfilLgtbi?: boolean;
}
