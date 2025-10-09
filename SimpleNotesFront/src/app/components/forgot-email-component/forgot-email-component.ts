import {Component, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from '../../service/auth-service';

@Component({
  selector: 'app-forgot-email-component',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, FormsModule],
  templateUrl: './forgot-email-component.html',
  styleUrl: './forgot-email-component.css'
})
export class ForgotEmailComponent {

  email: string = '';
  status = signal<'form' | 'success' | 'error'>('form');
  message = signal('');

  constructor(private router: Router, private authService: AuthService) {}

  onSubmit(): void {

    this.authService.forgotPassword(this.email).subscribe({
      next: (response: any) => {
        this.status.set('success');
        this.message.set('Um email de recuperação foi enviado para ' + this.email + '. Por favor, verifique sua caixa de entrada.');
      },
      error: (error) => {
        this.status.set('error');
        this.message.set(error.error.message || 'Ocorreu um erro ao tentar enviar o email de recuperação. Por favor, tente novamente mais tarde.');
      }
    });

    setTimeout(() => {
      this.message.set('');
      this.email = '';
    }, 2000);

  }
}
