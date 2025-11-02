import {Component, computed, Signal, signal, WritableSignal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {PageDTO, WalletResponse, WalletService} from '../../service/wallet-service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Component({
  selector: 'app-view-wallets-component',
  imports: [
    FormsModule, CommonModule
  ],
  templateUrl: './view-wallets-component.html',
  styleUrl: './view-wallets-component.css'
})
export class ViewWalletsComponent {

  vaults: WritableSignal<WalletResponse[]> = signal([]);
  isLoading: WritableSignal<boolean> = signal(false);
  currentPage: WritableSignal<number> = signal(0);
  isLastPage: WritableSignal<boolean> = signal(false);
  totalElements: WritableSignal<number> = signal(0);
  searchTerm: WritableSignal<string> = signal('');

  filteredVaults: Signal<WalletResponse[]> = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) {
      return this.vaults();
    }
    return this.vaults().filter(vault =>
      vault.name?.toLowerCase().includes(term)
    );
  });

  constructor(
    private walletService: WalletService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.fetchWallets();
  }

  fetchWallets(): void {
    if (this.isLoading() || this.isLastPage()) {
      return;
    }
    this.isLoading.set(true);
    const pageToFetch = this.currentPage();
    const pageSize = 10;

    this.walletService.getWallets(pageToFetch, pageSize).subscribe({
      next: (page: PageDTO<WalletResponse>) => {
        this.vaults.update(currentVaults => [...currentVaults, ...page.data]);
        this.currentPage.set(page.page);
        this.totalElements.set(page.totalElements);
        this.isLastPage.set(page.page >= page.totalPages - 1);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar cofres:', err);
        this.toastr.error('Falha ao carregar seus cofres. Tente novamente.', 'Erro');
        this.isLoading.set(false);
      }
    });
  }

  loadMore(): void {
    if (!this.isLoading() && !this.isLastPage()) {
      this.currentPage.update(page => page + 1);
      this.fetchWallets();
    }
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  navigateToVault(vaultId: number): void {
    this.router.navigate(['/wallet/', vaultId]);
  }

  onDeleteVault(vaultId: number, event: MouseEvent): void {
    event.stopPropagation();

    this.walletService.deleteWallet(vaultId).subscribe({
      next: () => {
        this.vaults.update(currentVaults =>
          currentVaults.filter(vault => vault.id !== vaultId)
        );
        this.totalElements.update(total => total - 1);
        this.toastr.success('Cofre excluído com sucesso!', 'Sucesso');
      },
      error: (err) => {
        console.error('Erro ao excluir cofre:', err);
        this.toastr.error('Falha ao excluir o cofre. Tente novamente.', 'Erro');
      }
    });
  }
}
