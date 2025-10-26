import {Component, signal} from '@angular/core';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-reset-password-component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reset-password-component.html',
  styleUrl: './reset-password-component.css'
})
export class ResetPasswordComponent {

  newPassword = '';
  isPasswordVisible = false;

  constructor(private endpoint: ActivatedRoute,
              private router: Router,
              private authService: AuthService,
              private toastr: ToastrService) {}

  onSubmit(): void {

    const token = this.endpoint.snapshot.paramMap.get('token');

    if (!token) {
      this.toastr.error('Nenhum token de redefinição de senha foi fornecido.', 'Token invalido!');
      return;
    }

    if (!this.newPassword) {
      this.toastr.error("Por favor, insira uma nova senha.", 'Senha invalida!');
      return;
    }

    const resetPasswordData = {
      newPassword: this.newPassword
    };


    this.authService.resetPassword(token, resetPasswordData).subscribe({
      next: (response: any) => {
        this.toastr.success('Você já pode fazer login com sua nova senha.', "Senha redefinida com sucesso!");
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        console.error('Erro ao redefinir senha:', error);

        const backendError = error.error;
        let displayMessage = 'Erro desconhecido. Tente novamente.';

        if (backendError) {
          if (backendError.errors) {
            displayMessage = Object.values(backendError.errors).join('<br>');
          } else if (backendError.message) {
            displayMessage = backendError.message;
          }
        }

        this.toastr.error(displayMessage, 'Erro ao redefinir senha!', {
          enableHtml: true,
          timeOut: 5000
        });
      }
    })

    setTimeout(() => {
      this.newPassword = '';
    }, 2000);
  }

  togglePasswordVisibility(): void {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

}
