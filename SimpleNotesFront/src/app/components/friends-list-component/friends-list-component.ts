import {Component, computed, Signal, signal, WritableSignal} from '@angular/core';
import {CommonModule} from "@angular/common";
import {RouterLink} from "@angular/router";
import {FriendRequestListResponse, FriendRequestService} from '../../service/friend-request-service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-friends-list-component', imports: [CommonModule, RouterLink],
  templateUrl: './friends-list-component.html',
  styleUrl: './friends-list-component.css'
})
export class FriendsListComponent {
  constructor(private friendRequestService: FriendRequestService,
              private toastr: ToastrService) {}

  friendRequests: WritableSignal<FriendRequestListResponse[]> = signal([]);
  currentPage: WritableSignal<number> = signal(0);
  isLastPage: WritableSignal<boolean> = signal(false);
  isLoading: WritableSignal<boolean> = signal(false);
  searchTerm: WritableSignal<string> = signal('');

  filteredUsers: Signal<FriendRequestListResponse[]> = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) {
      return this.friendRequests();
    }

    return this.friendRequests().filter(friendRequests => friendRequests.userResponse.name.toLowerCase().includes(term));
  });

  ngOnInit(): void {

    this.fetchFriendRequests();
  }

  private fetchFriendRequests(): void {

    if (this.isLoading() || this.isLastPage()) {
      return;
    }
    this.isLoading.set(true);


    this.friendRequestService.findAllFriends(this.currentPage(), 10).subscribe({
      next: (response) => {

        this.friendRequests.update(currentUsers => [...currentUsers, ...response.data]);
        this.isLastPage.set(response.page >= response.totalPages - 1);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.toastr.error(err.error.message, "Falha ao buscar amigos!");
        this.isLoading.set(false);

      }
    });
  }

  loadMore(): void {
    this.currentPage.update(page => page + 1);
    this.fetchFriendRequests();
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  deletedFriend(id: number): void {

    this.friendRequestService.delete(id).subscribe({
      next: (response) => {
        this.toastr.success("Sucesso", "Pedido de amizade excluido!");
      },
      error: (err) => {
        this.toastr.error(err.error.message, "Falha ao excluir amigo!");
      }
    });
  }
}
