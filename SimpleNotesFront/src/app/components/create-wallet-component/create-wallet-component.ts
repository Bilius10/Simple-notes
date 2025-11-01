import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {WalletService} from '../../service/wallet-service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-create-wallet-component',
  imports: [
    FormsModule, CommonModule
  ],
  templateUrl: './create-wallet-component.html',
  styleUrl: './create-wallet-component.css'
})
export class CreateWalletComponent {

  vaultTitle: string = '';
  vaultDescription: string = '';

  constructor(
    private walletService: WalletService,
    private toastr: ToastrService
  ) {
  }

  saveVault(): void {

    const walletData = {
      name: this.vaultTitle,
      description: this.vaultDescription
    };

    this.walletService.postWallet(walletData).subscribe({
      next: (response) => {
        this.toastr.success('Cofre criado!', 'Sucesso');
      },
      error: (error) => {
        console.error('Erro no login:', error);

        const backendError = error.error;
        let displayMessage = 'Erro desconhecido. Tente novamente.';

        if (backendError) {
          if (backendError.errors) {
            displayMessage = Object.values(backendError.errors).join('<br>');
          } else if (backendError.message) {
            displayMessage = backendError.message;
          }
        }

        this.toastr.error(displayMessage, 'Erro ao criar cofre', {
          enableHtml: true,
          timeOut: 5000
        });
      }
    });
  }
}
