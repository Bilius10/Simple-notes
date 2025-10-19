import {Component, Inject, PLATFORM_ID, signal} from '@angular/core';
import {CommonModule, isPlatformBrowser} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {AuthService} from '../../service/auth-service';

@Component({
  selector: 'app-profile-card-component',
  imports: [CommonModule, RouterModule],
  templateUrl: './profile-card-component.html',
  styleUrl: './profile-card-component.css'
})
export class ProfileCardComponent {

  userName = signal<string>('Usuário');
  email = signal<string>('usuario@usuariio.com');

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.userName.set(sessionStorage.getItem('name') || 'Usuário');
      this.email.set(sessionStorage.getItem('email') || 'usuario@usuario.com');
    }
  }

}
