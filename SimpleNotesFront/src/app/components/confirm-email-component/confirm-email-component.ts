import {Component, signal} from '@angular/core';
import {Route, Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-confirm-email-component',
  imports: [RouterModule],
  templateUrl: './confirm-email-component.html',
  styleUrl: './confirm-email-component.css'
})
export class ConfirmEmailComponent {

    status = signal<String>('loading');
    message = signal<String>('');

    constructor(private endpoint: ActivatedRoute, private router: Router,private authService: AuthService) {}

    onSubmit(): void {

      const token = this.endpoint.snapshot.paramMap.get('token');

      if (!token) {
        this.status.set('error');
        this.message.set('Nenhum token de confirmação foi fornecido. O link pode estar quebrado.');
        return;
      }

      this.confirmToken(token);
    }

    private confirmToken(token: string): void {

      this.authService.confirmEmail(token).subscribe({
        next: (response: any) => {
          console.log('Email confirmado com sucesso:', response);
          this.status.set('success');
          this.message.set('Email confirmado com sucesso! Você já pode fazer login.');
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          console.error('Erro na confirmação do email:', error);
          this.status.set('error');
          this.message.set(error.error.message || 'Ocorreu um erro ao confirmar o email. O token pode ser inválido ou expirado.');
        }
      });
    }
}
