import {Component, Inject, PLATFORM_ID, signal} from '@angular/core';
import {CommonModule, isPlatformBrowser, NgOptimizedImage} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-login-component',
  imports: [CommonModule, FormsModule, RouterModule, NgOptimizedImage, ReactiveFormsModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css'
})
export class LoginComponent {
  isLoading = signal<boolean>(false);
  loginForm: FormGroup;

  constructor(
    private router: Router,
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
    private toastr: ToastrService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid || this.isLoading()) {
      return;
    }

    this.isLoading.set(true);

    const loginData = this.loginForm.value;

    this.authService.login(loginData).subscribe({
      next: (response: any) => {
        this.isLoading.set(false);

        if (isPlatformBrowser(this.platformId)) {
          console.log('Login bem-sucedido:', response);
          this.toastr.success('Login bem-sucedido!', 'Sucesso');

          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('id', response.id);
          sessionStorage.setItem('name', response.name);
          sessionStorage.setItem('email', response.email);

          this.loginForm.reset();
          this.router.navigate(['/menu']);
        }
      },
      error: (httpError) => {
        this.isLoading.set(false);
        console.error('Erro no login:', httpError);

        const backendError = httpError.error;
        let displayMessage = 'Erro desconhecido. Tente novamente.';

        if (backendError) {
          if (backendError.errors) {
            displayMessage = Object.values(backendError.errors).join('<br>');
          } else if (backendError.message) {
            displayMessage = backendError.message;
          }
        }

        this.toastr.error(displayMessage, 'Erro no login', {
          enableHtml: true,
          timeOut: 5000
        });
      }
    });
  }
}
