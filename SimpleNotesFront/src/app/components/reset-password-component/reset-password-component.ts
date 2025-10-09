import {Component, signal} from '@angular/core';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-reset-password-component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reset-password-component.html',
  styleUrl: './reset-password-component.css'
})
export class ResetPasswordComponent {

  newPassword = '';
  isPasswordVisible = false;
  responseMessage = signal<String>('');

  constructor(private endpoint: ActivatedRoute, private router: Router,private authService: AuthService) {}

  onSubmit(): void {

    const token = this.endpoint.snapshot.paramMap.get('token');

    if (!token) {
      this.responseMessage.set("Nenhum token de redefinição de senha foi fornecido. O link pode estar quebrado.");
      return;
    }

    if (!this.newPassword) {
      this.responseMessage.set("Por favor, insira uma nova senha.");
      return;
    }

    const resetPasswordData = {
      newPassword: this.newPassword
    };


    this.authService.resetPassword(token, resetPasswordData).subscribe({
      next: (response: any) => {
        this.responseMessage.set("Senha redefinida com sucesso! Você já pode fazer login com sua nova senha.");
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        this.responseMessage.set("" + (error.error.message || 'Ocorreu um erro ao tentar redefinir a senha. O token pode ser inválido ou expirado.'));
      }
    })

    setTimeout(() => {
      this.responseMessage.set('');
      this.newPassword = '';
    }, 2000);
  }

  togglePasswordVisibility(): void {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

}
