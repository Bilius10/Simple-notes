import {Component, Inject, PLATFORM_ID, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../service/auth-service';
import {UserService} from '../../service/user-service';
import {isPlatformBrowser} from '@angular/common';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-edit-profile-component',
  imports: [FormsModule, RouterLink],
  templateUrl: './edit-profile-component.html',
  styleUrl: './edit-profile-component.css'
})
export class EditProfileComponent {

  name = signal<string>('');
  email = signal<string>('');

  constructor(
    private userService: UserService,
    @Inject(PLATFORM_ID) private platformId: Object,
    private toastr: ToastrService
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
          this.toastr.success("Sucesso", "Dados atualizados!");
        },
        error: (err) => {
          this.toastr.error(err.error.message, "Falha ao buscar usuário!");
        }
      });
    }

    setTimeout(() => {
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
          this.toastr.error(err.error.message, "Falha ao buscar dados!");
        }
      });
    }
  }
}
