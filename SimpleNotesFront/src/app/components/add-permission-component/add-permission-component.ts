import {Component, signal} from '@angular/core';
import {WalletResponse, WalletService} from '../../service/wallet-service';
import {FriendRequestListResponse, FriendRequestService} from '../../service/friend-request-service';
import {ListWalletUser, Permission, WalletUserRequest, WalletUserService} from '../../service/wallet-user-service';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-add-permission-component',
  imports: [
    FormsModule, CommonModule
  ],
  templateUrl: './add-permission-component.html',
  styleUrl: './add-permission-component.css'
})
export class AddPermissionComponent {
  wallets = signal<WalletResponse[]>([]);
  availableUsers = signal<FriendRequestListResponse[]>([]);
  existingPermissions = signal<ListWalletUser[]>([]);
  isLoading = signal<boolean>(false);

  selectedWalletId = signal<number | null>(null);
  selectedUserId = signal<number | null>(null);
  newPermissions = signal<Permission>({
    canCreate: false,
    canUpdate: false,
    canDelete: false
  });

  constructor(private http: HttpClient,
              private walletService: WalletService,
              private friendRequestService: FriendRequestService,
              private walletUserService: WalletUserService) {}

  ngOnInit(): void {
    this.loadWallets();
    this.loadFriends();
  }

  loadWallets() {
    this.walletService.getWallets(0, 10).subscribe({
      next: (data) => this.wallets.update(currentUsers => [...currentUsers, ...data.data]),
      error: (err) => console.error('Erro ao carregar wallets', err)
    })
  }

  loadFriends() {
    this.friendRequestService.findAllFriends(0, 10).subscribe({
      next: (data) => this.availableUsers.update(currentUsers => [...currentUsers, ...data.data]),
      error: (err) => console.error('Erro ao carregar usuarios', err)
    })
  }

  onWalletSelect() {
    const wId = this.selectedWalletId();
    if (!wId) return;

    this.isLoading.set(true);
    this.walletUserService.get(0, 10, wId).subscribe({
        next: (res) => {
          this.existingPermissions.set(res.data);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error(err);
          this.isLoading.set(false);
        }
      });
  }

  addPermission() {
    const wId = this.selectedWalletId();
    const uId = this.selectedUserId();

    if (!wId || !uId) return;

    const body: WalletUserRequest = {
      userId: uId,
      walletId: wId,
      permission: this.newPermissions()
    };

    this.walletUserService.create(body).subscribe({
      next: () => {
        alert('Usuário vinculado com sucesso!');
        this.selectedUserId.set(null);
        this.onWalletSelect();
      },
      error: (err) => alert('Erro ao vincular usuário.')
    });
  }

  updatePermission(item: ListWalletUser, walletId: number) {

    const body: WalletUserRequest = {
      userId: 0,
      walletId: walletId,
      permission: item.permission
    };

    this.walletUserService.update(item.id, body).subscribe({
      next: () => console.log('Permissão atualizada'),
      error: (err) => alert('Erro ao atualizar permissão')
    });
  }
}
