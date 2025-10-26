import {Component, signal} from '@angular/core';
import {Route, Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import { ActivatedRoute } from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-confirm-email-component',
  imports: [RouterModule],
  templateUrl: './confirm-email-component.html',
  styleUrl: './confirm-email-component.css'
})
export class ConfirmEmailComponent {

    status = signal<String>('loading');

    constructor(private endpoint: ActivatedRoute,
                private router: Router,
                private authService: AuthService,
                private toastr: ToastrService) {}

    onSubmit(): void {

      const token = this.endpoint.snapshot.paramMap.get('token');

      if (!token) {
        this.status.set('error');
        this.toastr.error('Nenhum token de redefinição de senha foi fornecido.', 'Token invalido!');
        return;
      }

      this.confirmToken(token);
    }

    private confirmToken(token: string): void {

      this.authService.confirmEmail(token).subscribe({
        next: (response: any) => {
          console.log('Email confirmado com sucesso:', response);
          this.status.set('success');
          this.toastr.error('Você já pode fazer login.', 'Email confirmado com sucesso!');
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          console.error('Erro ao confirmar email:', error);

          this.status.set('error');

          const backendError = error.error;
          let displayMessage = 'Erro desconhecido. Tente novamente.';

          if (backendError) {
            if (backendError.errors) {
              displayMessage = Object.values(backendError.errors).join('<br>');
            } else if (backendError.message) {
              displayMessage = backendError.message;
            }
          }

          this.toastr.error(displayMessage, 'Erro ao confirmar email!', {
            enableHtml: true,
            timeOut: 5000
          });
        }
      });
    }
}
