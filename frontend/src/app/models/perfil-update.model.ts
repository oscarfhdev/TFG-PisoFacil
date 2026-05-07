export interface PerfilUpdateRequest {
  nombre: string;
  apellidos?: string;
  email: string;
  fechaNacimiento?: string;
  genero?: string;
  estudios?: string;
  biografia?: string;
  instagramUrl?: string;
  esFumador?: boolean;
  tieneMascota?: boolean;
  tienePareja?: boolean;
  perfilLgtbi?: boolean;
}

export interface CambiarPasswordRequest {
  passwordActual: string;
  passwordNueva: string;
}
