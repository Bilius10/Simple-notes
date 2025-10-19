import { Component, signal, WritableSignal, computed, Signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {User, UserService} from '../../service/user-service';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-add-friend-component',
  imports: [CommonModule, RouterLink],
  templateUrl: './add-friend-component.html',
  styleUrl: './add-friend-component.css'
})
export class AddFriendComponent {

  constructor(private userService: UserService) {}

  users: WritableSignal<User[]> = signal([]);
  currentPage: WritableSignal<number> = signal(0);
  isLastPage: WritableSignal<boolean> = signal(false);
  isLoading: WritableSignal<boolean> = signal(false);
  searchTerm: WritableSignal<string> = signal('');

  filteredUsers: Signal<User[]> = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) {
      return this.users();
    }

    return this.users().filter(user => user.name.toLowerCase().includes(term));
  });

  ngOnInit(): void {

    this.fetchUsers();
  }

  private fetchUsers(): void {

    if (this.isLoading() || this.isLastPage()) {
      return;
    }
    this.isLoading.set(true);


    this.userService.getUsers(this.currentPage(), 10).subscribe({
      next: (response) => {

        this.users.update(currentUsers => [...currentUsers, ...response.data]);
        this.isLastPage.set(response.page >= response.totalPages - 1);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Falha ao buscar usuários:', err);
        this.isLoading.set(false);

      }
    });
  }

  loadMore(): void {
    this.currentPage.update(page => page + 1);
    this.fetchUsers();
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  addFriend(userId: number): void {
    alert(`Funcionalidade futura: Enviar pedido de amizade para o usuário com ID: ${userId}`);
  }

}
