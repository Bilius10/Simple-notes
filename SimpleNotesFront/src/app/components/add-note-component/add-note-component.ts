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
  selectedFile: File | null = null;

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

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      console.log('Arquivo selecionado:', this.selectedFile.name);
    }
  }

  triggerFileInput(): void {
    const fileInput = document.getElementById('upload-pdf') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  onSubmit(): void {
    const id = this.endpoint.snapshot.paramMap.get('id');

    if (!id) {
      this.toastr.error('Não foi possível encontrar o ID do cofre na URL.', 'ID inválido!');
      return;
    }

    if (this.selectedFile) {
      this.uploadFile(Number(id));
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

  uploadFile(walletId: number): void {
    if (!this.selectedFile) {
      this.toastr.error('Nenhum arquivo selecionado.', 'Erro');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);
    formData.append('walletId', walletId.toString());

    this.noteService.uploadFile(formData).subscribe({
      next: () => {
        this.toastr.success('Arquivo enviado com sucesso!', 'Sucesso');
        this.saved.emit(walletId);
      },
      error: (err) => {
        console.error('Erro ao enviar arquivo:', err);
        const errorMsg = err.error?.message || 'Falha ao enviar o arquivo.';
        this.toastr.error(errorMsg, 'Erro');
      }
    });
  }
}

