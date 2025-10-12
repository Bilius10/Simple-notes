import {Component, signal} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-profile-card-component',
  imports: [CommonModule],
  templateUrl: './profile-card-component.html',
  styleUrl: './profile-card-component.css'
})
export class ProfileCardComponent {

  userName = signal<string>('Usuário');
  email = signal<string>('usuario@usuariio.com');

  ngOnInit(): void {
    this.userName.set(localStorage.getItem('userName') || 'Usuário');
    this.email.set(localStorage.getItem('email') || 'usuario@usuario.com');
  }

}
