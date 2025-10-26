import {Component, computed, Signal, signal, WritableSignal} from '@angular/core';
import {CommonModule, NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {FriendRequestListResponse, FriendRequestService} from '../../service/friend-request-service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-pending-friend-requests-component',
    imports: [CommonModule, RouterLink],
  templateUrl: './pending-friend-requests-component.html',
  styleUrl: './pending-friend-requests-component.css'
})
export class PendingFriendRequestsComponent {
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


    this.friendRequestService.findAllPendingFriendRequests(this.currentPage(), 10).subscribe({
      next: (response) => {

        this.friendRequests.update(currentUsers => [...currentUsers, ...response.data]);
        this.isLastPage.set(response.page >= response.totalPages - 1);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.toastr.error(err.error.message, "Falha ao solicitações pendentes!");
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

  addFriend(status: string, id: number): void {
    console.log(status)
    this.friendRequestService.addFriend(id, status).subscribe({
      next: (response) => {

        this.toastr.success("Sucesso", "Solicitação respondida!")
        this.isLoading.set(false);
      },
      error: (err) => {
        this.isLoading.set(false);
        console.error('Erro ao responder solicitação:', err);

        const backendError = err.error;
        let displayMessage = 'Erro desconhecido. Tente novamente.';

        if (backendError) {
          if (backendError.errors) {
            displayMessage = Object.values(backendError.errors).join('<br>');
          } else if (backendError.message) {
            displayMessage = backendError.message;
          }
        }

        this.toastr.error(displayMessage, 'Erro ao responder solicitação', {
          enableHtml: true,
          timeOut: 5000
        });
      }
    });
  }
}
