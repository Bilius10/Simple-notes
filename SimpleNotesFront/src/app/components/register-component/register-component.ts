import {Component, signal} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import {ToastrService} from 'ngx-toastr';

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

  constructor(private router: Router,
              private authService: AuthService,
              private toastr: ToastrService
  ) {}

  async onSubmit(): Promise<void> {

      const registerData = {
        name: this.name,
        email: this.email,
        password: this.password
      };

      this.authService.register(registerData).subscribe({
        next: (response) => {
          console.log('Registro bem-sucedido:', response);
          this.toastr.success('Você será redirecionado para o login.', 'Registro bem-sucedido!');
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          console.error('Erro no registro:', error);

          const backendError = error.error;
          let displayMessage = 'Não foi possível completar o registro. Tente novamente.';

          if (backendError) {
            if (backendError.errors) {
              displayMessage = Object.values(backendError.errors).join('<br>');
            } else if (backendError.message) {
              displayMessage = backendError.message;
            }
          }

          this.toastr.error(displayMessage, 'Erro ao registrar:', {
            enableHtml: true,
            timeOut: 5000
          });
        }
      });

      setTimeout(() => {
        this.name = '';
        this.email = '';
        this.password = '';
      }, 2000);
  }
}
