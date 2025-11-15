import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NoteResponse, NoteService} from '../../service/note-service';
import {WalletService} from '../../service/wallet-service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-view-note-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './view-note-component.html',
  styleUrl: './view-note-component.css'
})
export class ViewNoteComponent {
  @Input() note: NoteResponse | null = null;
  @Output() closed = new EventEmitter<void>();

  constructor(
    private noteService: NoteService,
    private toastr: ToastrService,
    private endpoint: ActivatedRoute
  ) { }

  close() {
    this.closed.emit();
  }

  update() {
    const data = {
      title: this.note?.title || '',
      content: this.note?.content || ''
    };

    const walletId = Number(this.endpoint.snapshot.paramMap.get('id'));
    if (!this.note) {
      this.toastr.error('Nota inválida.', 'Erro');
      return;
    }

    this.noteService.update(this.note.id, walletId, data).subscribe({
      next: (updatedNote) => {
        this.toastr.success('Nota atualizada com sucesso.', 'Sucesso');
        this.note = updatedNote;
      },
      error: (err) => {
        console.error('Erro ao atualizar nota:', err);
        const errorMsg = err.error?.message || 'Falha ao atualizar a nota.';
        this.toastr.error(errorMsg, 'Erro');
      }
    });
  }
}
