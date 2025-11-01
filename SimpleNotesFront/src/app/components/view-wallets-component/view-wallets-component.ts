import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {WalletResponse, WalletService} from '../../service/wallet-service';
import {ToastrService} from 'ngx-toastr';
import {PageDTO} from '../../service/user-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-view-wallets-component',
  imports: [
    FormsModule, CommonModule
  ],
  templateUrl: './view-wallets-component.html',
  styleUrl: './view-wallets-component.css'
})
export class ViewWalletsComponent implements OnInit {

  vaults: WalletResponse[] = [];
  isLoading: boolean = false;

  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  constructor(
    private walletService: WalletService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadWallets(false);
  }

  loadWallets(isLoadMore: boolean = false): void {
    if (this.isLoading) return;

    this.isLoading = true;
    const pageToFetch = isLoadMore ? this.currentPage + 1 : 0;

    if (!isLoadMore) {
      this.vaults = [];
    }

    this.walletService.getUsers(pageToFetch, this.pageSize).subscribe({
      next: (page: PageDTO<WalletResponse>) => {
        this.vaults = [...this.vaults, ...page.data];
        this.currentPage = page.page;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar cofres:', err);
        this.toastr.error('Falha ao carregar seus cofres. Tente novamente.', 'Erro');
        this.isLoading = false;
      }
    });
  }

  onLoadMore(): void {
    this.loadWallets(true);
  }

  hasNextPage(): boolean {
    return this.currentPage < (this.totalPages - 1);
  }

  navigateToVault(vaultId: number): void {
    this.router.navigate(['/', vaultId]);
  }

  onDeleteVault(vaultId: number, event: MouseEvent): void {
    this.walletService.deleteWallet(vaultId).subscribe({
      next: () => {
          this.vaults = this.vaults.filter(vault => vault.id !== vaultId);
          this.toastr.success('Cofre excluído com sucesso!', 'Sucesso');
        },
        error: (err) => {
          console.error('Erro ao excluir cofre:', err);
          this.toastr.error('Falha ao excluir o cofre. Tente novamente.', 'Erro');
        }
      });
    }
}
