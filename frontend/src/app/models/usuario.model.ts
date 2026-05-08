export interface UsuarioRequest {
  nombre: string;
  apellidos?: string;
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
  apellidos?: string;
  email: string;
  esAdmin: boolean;
  fechaNacimiento?: string;
  genero?: string;
  estudios?: string;
  biografia?: string;
  fotoPerfilUrl?: string;
  instagramUrl?: string;
  telefono?: string;
  fechaRegistro: string;
  esFumador?: boolean;
  tieneMascota?: boolean;
  tienePareja?: boolean;
  perfilLgtbi?: boolean;
  cuentaActiva: boolean;
}
