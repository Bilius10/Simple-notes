import {Component, signal, WritableSignal} from '@angular/core';
import {WalletResponse, WalletService} from '../../service/wallet-service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {AddNoteComponent} from '../add-note-component/add-note-component';
import {NoteResponse, NoteService} from '../../service/note-service';
import {CommonModule} from '@angular/common';
import {ViewNoteComponent} from '../view-note-component/view-note-component';

@Component({
  selector: 'app-view-wallet-component',
  imports: [AddNoteComponent, CommonModule, ViewNoteComponent],
  templateUrl: './view-wallet-component.html',
  styleUrl: './view-wallet-component.css'
})
export class ViewWalletComponent {
  wallet: WritableSignal<WalletResponse | null> = signal(null);
  isModalOpen: WritableSignal<boolean> = signal(false); // Add note modal
  public notes = signal<NoteResponse[]>([]);
  selectedNote: WritableSignal<NoteResponse | null> = signal(null); // Document modal
  walletId: string | null = null;

  constructor(
    private noteService: NoteService,
    private walletService: WalletService,
    private toastr: ToastrService,
    private endpoint: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.walletId = this.endpoint.snapshot.paramMap.get('id');

    if (!this.walletId) {
      this.toastr.error('Não foi possível encontrar o ID do cofre na URL.', 'ID inválido!');
      return;
    }

    this.fetchWallet(Number(this.walletId));
    this.loadNotes(Number(this.walletId));
  }

  fetchWallet(id: number): void {
    this.walletService.getWallet(id).subscribe({
      next: (data: WalletResponse) => {
        this.wallet.set(data);
      },
      error: (err) => {
        console.error('Erro ao carregar cofre:', err);
      }
    });
  }

  loadNotes(id: number): void {
    const page = 0;
    const size = 20;

    this.noteService.get(page, size, id).subscribe({
      next: (pageDto) => {
        const formattedData = pageDto.data.map(n => ({ ...n }));
        this.notes.set(formattedData);
      },
      error: (err) => {
        this.toastr.error(err.error?.message || 'Erro ao carregar notas.', 'Erro ao carregar notas!');
        console.error('Erro ao carregar notas:', err);
      }
    });
  }

  openModal(): void {
    this.isModalOpen.set(true);
  }

  closeModal(): void {
    this.isModalOpen.set(false);
  }

  openNoteModal(note: NoteResponse): void {
    this.selectedNote.set(note);
  }

  closeDocumentModal(): void {
    this.selectedNote.set(null);
  }

  deleteDocument(noteId: number): void {
    this.noteService.delete(noteId, Number(this.walletId)).subscribe({
      next: () => {
        this.toastr.success('Nota deletada com sucesso!', 'Sucesso');
        const walletId = this.walletId;
        this.loadNotes(Number(this.walletId));
      },
      error: (err) => {
        this.toastr.error(err.error?.message || 'Erro ao deletar nota.', 'Erro ao deletar nota!');
        console.error('Erro ao deletar nota:', err);
      }
    });
  }
}
