import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NoteResponse } from '../../service/note-service';

@Component({
  selector: 'app-view-note-component',
  imports: [CommonModule],
  templateUrl: './view-note-component.html',
  styleUrl: './view-note-component.css'
})
export class ViewNoteComponent {
  @Input() note: NoteResponse | null = null;
  @Output() closed = new EventEmitter<void>();

  close() {
    this.closed.emit();
  }
}
