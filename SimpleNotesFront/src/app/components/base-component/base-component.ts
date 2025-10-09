import {Component, signal} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-base-component',
  imports: [RouterModule, NgOptimizedImage],
  templateUrl: './base-component.html',
  styleUrl: './base-component.css'
})
export class BaseComponent {

  userName = signal<string>('Usuário');

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.userName.set(localStorage.getItem('userName') || 'Usuário');
  }

}
