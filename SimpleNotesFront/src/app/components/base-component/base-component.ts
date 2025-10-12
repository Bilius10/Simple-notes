import {Component, ElementRef, Inject, PLATFORM_ID, Renderer2, signal} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {CommonModule, isPlatformBrowser, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-base-component',
  imports: [RouterModule, NgOptimizedImage, CommonModule],
  templateUrl: './base-component.html',
  styleUrl: './base-component.css'
})
export class BaseComponent {

  userName = signal<string>('Usuário');

  constructor(private router: Router,
              private renderer: Renderer2,
              private el: ElementRef,
              @Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.userName.set(sessionStorage.getItem('name') || 'Usuário');
    }
  }

  toggleSidebar() {
    const sidebar = this.el.nativeElement.querySelector('.sidebar');
    const overlay = this.el.nativeElement.querySelector('.overlay');

    if (sidebar && overlay) {
      sidebar.classList.toggle('is-open');
      overlay.classList.toggle('is-visible');
    }
  }

  closeSidebar() {
    const sidebar = this.el.nativeElement.querySelector('.sidebar');
    const overlay = this.el.nativeElement.querySelector('.overlay');

    if (sidebar && overlay) {
      sidebar.classList.remove('is-open');
      overlay.classList.remove('is-visible');
    }
  }
}
