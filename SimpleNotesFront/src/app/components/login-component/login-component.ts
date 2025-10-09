import {Component, signal} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
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

  constructor(private router: Router, private authService: AuthService) {}

  onSubmit(): void {
    const loginData = {
      email: this.email,
      password: this.password
    }

    this.authService.login(loginData).subscribe({
      next: (response: any) => {
        console.log('Login bem-sucedido:', response);
        this.responseMessage.set("Login bem-sucedido!");
        localStorage.setItem('token', response.token);
        localStorage.setItem('id', response.userId);
        localStorage.setItem('userName', response.userName);
        this.router.navigate(['/']);
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
