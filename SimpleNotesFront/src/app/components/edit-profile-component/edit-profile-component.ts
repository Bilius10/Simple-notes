import {Component, Inject, PLATFORM_ID, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import {UserService} from '../../service/user-service';
import {isPlatformBrowser} from '@angular/common';

@Component({
  selector: 'app-edit-profile-component',
  imports: [FormsModule, RouterLink],
  templateUrl: './edit-profile-component.html',
  styleUrl: './edit-profile-component.css'
})
export class EditProfileComponent {

  name = signal<string>('');
  email = signal<string>('');
  responseMessage = signal<string>('');

  constructor(
    private userService: UserService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.fetchUsers()
  }

  ngOnInit(): void {

    this.fetchUsers();
  }

  onSave(): void {
    const updateData = {
      name: this.name(),
      email: this.email(),
    };

    if (isPlatformBrowser(this.platformId)) {
      this.userService.updateUser(sessionStorage.getItem('id'), updateData).subscribe({
        next: (user) => {
          this.responseMessage.set("Dados atualizados");
        },
        error: (err) => {
          this.responseMessage.set("Erro ao atualizar dados");
          console.error('Falha ao buscar usuário:', err);
        }
      });
    }

    setTimeout(() => {
      this.responseMessage.set('');
    }, 2000);
  }

  fetchUsers(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.userService.getUserById(sessionStorage.getItem('id')).subscribe({
        next: (user) => {
          this.name.set(user.name);
          this.email.set(user.email);
        },
        error: (err) => {
          console.error('Falha ao buscar usuário:', err);
        }
      });
    }
  }
}
