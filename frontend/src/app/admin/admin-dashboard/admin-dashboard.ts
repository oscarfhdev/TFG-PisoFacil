import { Component, inject, OnInit, OnDestroy, ElementRef, ViewChild, AfterViewInit, signal, computed } from '@angular/core';
import { DatePipe, DecimalPipe, UpperCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { AdminService } from '../../services/admin.service';
import { catchError, of } from 'rxjs';
import { Chart, ChartConfiguration, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [DatePipe, DecimalPipe, UpperCasePipe, RouterLink],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard implements OnInit, AfterViewInit, OnDestroy {
  private adminService = inject(AdminService);

  @ViewChild('ciudadesChart') ciudadesChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('reportesChart') reportesChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('preciosChart') preciosChartRef!: ElementRef<HTMLCanvasElement>;

  private ciudadesChartInstance?: Chart;
  private reportesChartInstance?: Chart;
  private preciosChartInstance?: Chart;

  stats = toSignal(
    this.adminService.getStats().pipe(catchError(() => of(null))),
    { initialValue: null }
  );

  isLoading = computed(() => this.stats() === null);

  // Colores de marca PisoFácil
  readonly PRIMARY = '#64A7C5';
  readonly ACCENT = '#F39C12';
  readonly SECONDARY = '#1C4B68';

  private isDark(): boolean {
    return document.documentElement.classList.contains('dark');
  }

  private get gridColor(): string {
    return this.isDark() ? 'rgba(148,163,184,0.1)' : 'rgba(0,0,0,0.06)';
  }

  private get tickColor(): string {
    return this.isDark() ? '#94a3b8' : '#6b7280';
  }

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    // Construir gráficos cuando los datos estén disponibles
    const checkAndBuild = () => {
      const s = this.stats();
      if (s) {
        this.buildCiudadesChart(s.pisosPorCiudad);
        this.buildReportesChart(s.reportesPendientes, s.reportesResueltos, s.reportesRechazados);
        this.buildPreciosChart(s.habitacionesPorRangoPrecio);
      } else {
        setTimeout(checkAndBuild, 200);
      }
    };
    setTimeout(checkAndBuild, 300);
  }

  ngOnDestroy(): void {
    this.ciudadesChartInstance?.destroy();
    this.reportesChartInstance?.destroy();
    this.preciosChartInstance?.destroy();
  }

  private buildCiudadesChart(pisosPorCiudad: Record<string, number>): void {
    if (!this.ciudadesChartRef?.nativeElement || !pisosPorCiudad) return;
    const labels = Object.keys(pisosPorCiudad);
    const data = Object.values(pisosPorCiudad);

    const config: ChartConfiguration = {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Pisos',
          data,
          backgroundColor: `${this.PRIMARY}CC`,
          borderColor: this.PRIMARY,
          borderWidth: 2,
          borderRadius: 8,
          borderSkipped: false,
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: this.isDark() ? '#1e293b' : '#fff',
            titleColor: this.isDark() ? '#f1f5f9' : '#111827',
            bodyColor: this.isDark() ? '#94a3b8' : '#6b7280',
            borderColor: this.isDark() ? '#334155' : '#e5e7eb',
            borderWidth: 1,
            padding: 12,
          }
        },
        scales: {
          x: {
            grid: { color: this.gridColor },
            ticks: { color: this.tickColor, font: { family: 'Lato' } },
          },
          y: {
            beginAtZero: true,
            grid: { color: this.gridColor },
            ticks: { color: this.tickColor, font: { family: 'Lato' }, stepSize: 1 },
          }
        }
      }
    };

    this.ciudadesChartInstance = new Chart(this.ciudadesChartRef.nativeElement, config);
  }

  private buildReportesChart(pendientes: number, resueltos: number, rechazados: number): void {
    if (!this.reportesChartRef?.nativeElement) return;

    const config: ChartConfiguration<'doughnut'> = {
      type: 'doughnut',
      data: {
        labels: ['Pendientes', 'Resueltos', 'Rechazados'],
        datasets: [{
          data: [pendientes, resueltos, rechazados],
          backgroundColor: ['#F39C12CC', '#10B981CC', '#EF4444CC'],
          borderColor: ['#F39C12', '#10B981', '#EF4444'],
          borderWidth: 2,
          hoverOffset: 8,
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '70%',
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              color: this.tickColor,
              font: { family: 'Lato', size: 12 },
              padding: 16,
              usePointStyle: true,
              pointStyleWidth: 10,
            }
          },
          tooltip: {
            backgroundColor: this.isDark() ? '#1e293b' : '#fff',
            titleColor: this.isDark() ? '#f1f5f9' : '#111827',
            bodyColor: this.isDark() ? '#94a3b8' : '#6b7280',
            borderColor: this.isDark() ? '#334155' : '#e5e7eb',
            borderWidth: 1,
            padding: 12,
          }
        }
      }
    };

    this.reportesChartInstance = new Chart(this.reportesChartRef.nativeElement, config);
  }

  private buildPreciosChart(habitacionesPorRangoPrecio: Record<string, number>): void {
    if (!this.preciosChartRef?.nativeElement || !habitacionesPorRangoPrecio) return;
    const labels = Object.keys(habitacionesPorRangoPrecio);
    const data = Object.values(habitacionesPorRangoPrecio);

    const config: ChartConfiguration = {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Habitaciones',
          data,
          backgroundColor: [`${this.ACCENT}CC`, `${this.PRIMARY}CC`, `${this.SECONDARY}CC`, '#8B5CF6CC'],
          borderColor: [this.ACCENT, this.PRIMARY, this.SECONDARY, '#8B5CF6'],
          borderWidth: 2,
          borderRadius: 8,
          borderSkipped: false,
        }]
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: this.isDark() ? '#1e293b' : '#fff',
            titleColor: this.isDark() ? '#f1f5f9' : '#111827',
            bodyColor: this.isDark() ? '#94a3b8' : '#6b7280',
            borderColor: this.isDark() ? '#334155' : '#e5e7eb',
            borderWidth: 1,
            padding: 12,
          }
        },
        scales: {
          x: {
            beginAtZero: true,
            grid: { color: this.gridColor },
            ticks: { color: this.tickColor, font: { family: 'Lato' }, stepSize: 1 },
          },
          y: {
            grid: { display: false },
            ticks: { color: this.tickColor, font: { family: 'Lato', size: 12 } },
          }
        }
      }
    };

    this.preciosChartInstance = new Chart(this.preciosChartRef.nativeElement, config);
  }
}
