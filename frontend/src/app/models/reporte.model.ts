export interface ReporteRequest {
  idUsuarioEmisor: number;
  categoria: string;
  titulo: string;
  mensaje: string;
}

export interface ReporteResponse {
  idReporte: number;
  idUsuarioEmisor: number;
  nombreEmisor: string;
  categoria: string;
  titulo: string;
  mensaje: string;
  fechaCreacion: string;
  estado: string;
}
