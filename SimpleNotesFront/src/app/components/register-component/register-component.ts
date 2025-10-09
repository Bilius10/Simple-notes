import {Component, signal} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';

@Component({
  selector: 'app-register-component',
  imports: [CommonModule, FormsModule, RouterModule, NgOptimizedImage],
  templateUrl: './register-component.html',
  styleUrl: './register-component.css'
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  responseMessage = signal<string>('');

  constructor(private router: Router, private authService: AuthService) {}

  async onSubmit(): Promise<void> {

      const registerData = {
        name: this.name,
        email: this.email,
        password: this.password
      };

      this.authService.register(registerData).subscribe({
        next: (response) => {
          console.log('Registro bem-sucedido:', response);
          this.responseMessage.set("Registro bem-sucedido!");
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          console.error('Erro no registro:', error);
          this.responseMessage.set(error.error.message)
        }
      });

      setTimeout(() => {
        this.responseMessage.set('');
        this.name = '';
        this.email = '';
        this.password = '';
      }, 2000);
  }
}
