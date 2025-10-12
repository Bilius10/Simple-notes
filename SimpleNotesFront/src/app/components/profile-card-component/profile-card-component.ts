import {Component, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';

@Component({
  selector: 'app-profile-card-component',
  imports: [CommonModule, RouterModule],
  templateUrl: './profile-card-component.html',
  styleUrl: './profile-card-component.css'
})
export class ProfileCardComponent {

  userName = signal<string>('Usuário');
  email = signal<string>('usuario@usuariio.com');

  ngOnInit(): void {
    this.userName.set(sessionStorage.getItem('name') || 'Usuário');
    this.email.set(sessionStorage.getItem('email') || 'usuario@usuario.com');
  }

}
