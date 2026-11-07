import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { catchError, map, switchMap, throwError } from 'rxjs';
import { AuthService, UserProfile } from './auth.service';

type AuthMode = 'login' | 'signup';
type ApiStatus = 'checking' | 'online' | 'offline';

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  readonly mode = signal<AuthMode>('login');
  readonly apiStatus = signal<ApiStatus>('checking');
  readonly isSubmitting = signal(false);
  readonly showPassword = signal(false);
  readonly formError = signal('');
  readonly successMessage = signal('');
  readonly profile = signal<UserProfile | null>(null);

  email = '';
  password = '';
  confirmPassword = '';
  private accessToken = '';
  private refreshToken = '';

  constructor(private readonly authService: AuthService) {}

  ngOnInit(): void {
    this.authService.healthCheck().subscribe({
      next: () => this.apiStatus.set('online'),
      error: () => this.apiStatus.set('offline'),
    });
  }

  setMode(mode: AuthMode): void {
    this.mode.set(mode);
    this.formError.set('');
    this.successMessage.set('');
    this.confirmPassword = '';
  }

  togglePasswordVisibility(): void {
    this.showPassword.update((visible) => !visible);
  }

  submit(form: NgForm): void {
    this.formError.set('');
    this.successMessage.set('');

    if (form.invalid || this.isSubmitting()) {
      form.control.markAllAsTouched();
      return;
    }

    if (this.mode() === 'signup' && this.password !== this.confirmPassword) {
      this.formError.set('Your passwords do not match.');
      return;
    }

    this.isSubmitting.set(true);
    const credentials = { email: this.email.trim(), password: this.password };

    if (this.mode() === 'signup') {
      this.authService.signup(credentials).subscribe({
        next: (response) => {
          this.isSubmitting.set(false);
          this.mode.set('login');
          this.password = '';
          this.confirmPassword = '';
          form.resetForm({ email: credentials.email });
          this.successMessage.set(response.message || 'Account created. Sign in to continue.');
        },
        error: (error: HttpErrorResponse) => {
          this.isSubmitting.set(false);
          this.formError.set(this.getErrorMessage(error));
        },
      });
      return;
    }

    this.authService
      .login(credentials)
      .pipe(
        switchMap((response) =>
          this.authService
            .getCurrentUser(response.accessToken)
            .pipe(map((profile) => ({ response, profile }))),
        ),
      )
      .subscribe({
        next: ({ response, profile }) => {
          this.isSubmitting.set(false);
          this.accessToken = response.accessToken;
          this.refreshToken = response.refreshToken;
          this.profile.set(profile);
        },
        error: (error: HttpErrorResponse) => {
          this.isSubmitting.set(false);
          this.formError.set(this.getErrorMessage(error));
        },
      });
  }

  signOut(): void {
    const refreshAndLogout = () =>
      this.authService.refresh(this.refreshToken).pipe(
        switchMap((tokens) => {
          this.accessToken = tokens.accessToken;
          this.refreshToken = tokens.refreshToken;
          return this.authService.logout(tokens.accessToken);
        }),
      );

    this.authService
      .logout(this.accessToken)
      .pipe(
        catchError((error: HttpErrorResponse) =>
          error.status === 401 && this.refreshToken
            ? refreshAndLogout()
            : throwError(() => error),
        ),
      )
      .subscribe({
        next: () => this.clearSession(),
        error: (error: HttpErrorResponse) => {
          this.clearSession();
          this.formError.set(
            `Signed out on this device, but the server could not revoke the session: ${this.getErrorMessage(error)}`,
          );
        },
      });
  }

  private clearSession(): void {
    this.accessToken = '';
    this.refreshToken = '';
    this.profile.set(null);
    this.password = '';
    this.formError.set('');
    this.successMessage.set('You have been signed out.');
  }

  private getErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Could not reach the WordyNerd API. Check that the backend is running and the database is available.';
    }

    const message = error.error?.message;
    if (typeof message === 'string' && message.trim()) {
      return message;
    }

    return `The request failed (${error.status}). Please try again.`;
  }
}
