import {Component, EventEmitter, Output} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {NoteService} from '../../service/note-service';

@Component({
  selector: 'app-add-note-component',
  imports: [],
  templateUrl: './add-note-component.html',
  styleUrl: './add-note-component.css'
})
export class AddNoteComponent {
  title: string = '';
  content: string = '';

  @Output() closed = new EventEmitter<void>();
  @Output() saved = new EventEmitter<number | null>();

  constructor(
    private noteService: NoteService,
    private toastr: ToastrService,
    private endpoint: ActivatedRoute
  ) { }

  onClose(): void {
    this.closed.emit();
  }

  onSubmit(): void {
    const id = this.endpoint.snapshot.paramMap.get('id');

    if (!id) {
      this.toastr.error('Não foi possível encontrar o ID do cofre na URL.', 'ID inválido!');
      return;
    }

    const noteData = {
      title: this.title,
      content: this.content,
      walletId: Number(id)
    };

    this.noteService.create(noteData).subscribe({
      next: (data) => {
        this.toastr.success('Nota adicionada com sucesso!', 'Sucesso');
        this.saved.emit(Number(id));
      },
      error: (err) => {
        console.error('Erro ao adicionar nota:', err);
        const errorMsg = err.error?.message || 'Falha ao adicionar nota.';
        this.toastr.error(errorMsg, 'Erro');
      }
    });
  }
}
