import {Component, computed, Signal, signal, WritableSignal} from '@angular/core';
import {PageDTO, WalletResponse, WalletService} from '../../service/wallet-service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-view-wallet-component',
  imports: [],
  templateUrl: './view-wallet-component.html',
  styleUrl: './view-wallet-component.css'
})
export class ViewWalletComponent {
  wallet: WritableSignal<WalletResponse | null> = signal(null);

  constructor(
    private walletService: WalletService,
    private toastr: ToastrService,
    private endpoint: ActivatedRoute
  ) { }

  ngOnInit(): void {
    const id = this.endpoint.snapshot.paramMap.get('id');

    if (!id) {
      this.toastr.error('Não foi possível encontrar o ID do cofre na URL.', 'ID inválido!');
      return;
    }

    this.fetchWallet(Number(id));
  }

  fetchWallet(id: number): void {

    this.walletService.getWallet(id).subscribe({

      next: (data: WalletResponse) => {
        this.wallet.set(data);
      },
      error: (err) => {
        console.error('Erro ao carregar cofre:', err);
        const errorMsg = err.error?.message || 'Falha ao carregar dados do cofre.';
        this.toastr.error(errorMsg, 'Erro');
      }
    });
  }
}
