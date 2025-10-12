import {Component, Inject, PLATFORM_ID, signal} from '@angular/core';
import {CommonModule, isPlatformBrowser, NgOptimizedImage} from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';

@Component({
  selector: 'app-login-component',
  imports: [CommonModule, FormsModule, RouterModule, NgOptimizedImage],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  responseMessage = signal<string>('');

  constructor(
    private router: Router,
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  onSubmit(): void {
    const loginData = {
      email: this.email,
      password: this.password
    }

    this.authService.login(loginData).subscribe({
      next: (response: any) => {
        if (isPlatformBrowser(this.platformId)) {
          console.log('Login bem-sucedido:', response);
          this.responseMessage.set("Login bem-sucedido!");

          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('id', response.userId);
          sessionStorage.setItem('name', response.name);
          sessionStorage.setItem('email', response.email);

          this.router.navigate(['/menu']);
        }
      },
      error: (error) => {
        console.error('Erro no login:', error);
        this.responseMessage.set(error.error.message);
      }
    });

    setTimeout(() => {
      this.responseMessage.set('');
      this.email = '';
      this.password = '';
    }, 2000);
  }
}
