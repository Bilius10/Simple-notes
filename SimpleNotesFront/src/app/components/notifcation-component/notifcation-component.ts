import {Component, signal, OnInit, Inject, PLATFORM_ID} from '@angular/core';
import {CommonModule, isPlatformBrowser} from '@angular/common';
import {RouterLink} from '@angular/router';
import {NotificationService, Notification} from '../../service/notification-service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-notifcation-component',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './notifcation-component.html',
  styleUrl: './notifcation-component.css'
})
export class NotifcationComponent implements OnInit {

  public notifications = signal<Notification[]>([]);
  public isLoading = signal(true);
  public unreadCount = signal(0);

  constructor(
    private notificationService: NotificationService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadNotifications();
    this.loadUnreadCount();
  }

  loadNotifications(): void {
    this.isLoading.set(true);
    this.notificationService.getNotifications().subscribe({
      next: (data) => {
        const formattedData = data.map(n => ({
          ...n,
          createdAt: this.formatDate(n.createdAt)
        }));
        this.notifications.set(formattedData);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.toastr.error(err.error.message, 'Erro ao carregar notificações!');
        console.error("Erro ao carregar notificações:", err);
        this.isLoading.set(false);
      }
    });
  }

  loadUnreadCount(): void {
    this.notificationService.countUnreadNotifications().subscribe({
      next: (count) => this.unreadCount.set(count),
      error: (err) => console.error("Erro ao carregar contador:", err)
    });
  }

  markAsRead(id: number): void {

    this.notifications.update(currentList =>
      currentList.map(n => (n.id === id ? { ...n, isRead: true } : n))
    );
    this.unreadCount.update(c => (c > 0 ? c - 1 : 0));

    this.notificationService.markAsRead(id.toString()).subscribe({
      error: (err) => {
        console.error("Erro ao marcar como lida:", err);
        this.toastr.error(err.error.message, 'Erro ao marcar como lida!');

        this.notifications.update(currentList =>
          currentList.map(n => (n.id === id ? { ...n, isRead: false } : n))
        );
        this.unreadCount.update(c => c + 1);
      }
    });
  }

  markAllAsRead(): void {
    this.notifications.update(list => list.map(n => ({ ...n, isRead: true })));
    this.unreadCount.set(0);

    this.notificationService.markAllAsRead().subscribe({
      error: (err) => {
        console.error("Erro ao marcar todas como lidas:", err);
        this.toastr.error(err.error.message, 'Erro ao marcar todas como lidas!');

        this.loadNotifications();
        this.loadUnreadCount();
      }
    });
  }

  private formatDate(dateString: string): string {

    const date = new Date(dateString);
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
