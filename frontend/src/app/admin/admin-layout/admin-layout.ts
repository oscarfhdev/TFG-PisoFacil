import { Component, inject, signal, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { UpperCasePipe, CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, UpperCasePipe, CommonModule, ReactiveFormsModule],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.scss'
})
export class AdminLayout implements OnInit {
  authService = inject(AuthService);
  router = inject(Router);
  private platformId = inject(PLATFORM_ID);
  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);

  sidebarOpen = signal(false);
  isDarkMode = signal(false);
  showPasswordForm = signal(false);
  passwordLoading = signal(false);
  passwordSuccess = signal(false);

  passwordForm!: FormGroup;

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const savedTheme = localStorage.getItem('theme');
      const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
      const shouldBeDark = savedTheme === 'dark' || (!savedTheme && prefersDark);
      this.isDarkMode.set(shouldBeDark);
      if (shouldBeDark) {
        document.documentElement.classList.add('dark');
      } else {
        document.documentElement.classList.remove('dark');
      }
    }

    this.passwordForm = this.fb.group({
      passwordActual: ['', [Validators.required, Validators.minLength(6)]],
      passwordNueva: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  toggleDarkMode() {
    this.isDarkMode.set(!this.isDarkMode());
    if (isPlatformBrowser(this.platformId)) {
      if (this.isDarkMode()) {
        document.documentElement.classList.add('dark');
        localStorage.setItem('theme', 'dark');
      } else {
        document.documentElement.classList.remove('dark');
        localStorage.setItem('theme', 'light');
      }
    }
  }

  toggleSidebar() { this.sidebarOpen.set(!this.sidebarOpen()); }
  closeSidebar() { this.sidebarOpen.set(false); }

  togglePasswordForm() {
    this.showPasswordForm.set(!this.showPasswordForm());
    this.passwordSuccess.set(false);
    if (!this.showPasswordForm()) {
      this.passwordForm.reset();
    }
  }

  onCambiarPassword() {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }
    this.passwordLoading.set(true);
    this.usuarioService.changePassword(this.passwordForm.value).subscribe({
      next: () => {
        this.passwordLoading.set(false);
        this.passwordSuccess.set(true);
        this.passwordForm.reset();
        setTimeout(() => {
          this.showPasswordForm.set(false);
          this.passwordSuccess.set(false);
        }, 2500);
      },
      error: () => {
        this.passwordLoading.set(false);
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
