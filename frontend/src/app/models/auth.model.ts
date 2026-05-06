export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  idUsuario: number;
  nombre: string;
  email: string;
  role: string;
}

export interface RegisterRequest {
  nombre: string;
  apellidos?: string;
  email: string;
  password: string;
  fechaNacimiento?: string;
  estudios?: string;
  biografia?: string;
  esFumador?: boolean;
  tieneMascota?: boolean;
  tienePareja?: boolean;
  perfilLgtbi?: boolean;
}

export interface RegisterResponse {
  idUsuario: number;
  nombre: string;
  email: string;
  message: string;
}
