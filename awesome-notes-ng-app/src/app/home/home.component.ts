import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { AddNoteService } from '../add-note.service';
import { NoteRequest } from '../add-note/noteRequest';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  isAuthenticated: boolean;
  notes: Observable<Array<NoteRequest>>;

  constructor(
    private authService: AuthService,
    private noteService: AddNoteService
  ) { }

  ngOnInit(): void {
    this.authService.isAuthenticated().subscribe((value) => {
        this.isAuthenticated = value;
    });
    
    if (this.isAuthenticated) {
        this.notes = this.noteService.getAllNotes();
    }  
  }

  delete(noteId): void {
    this.noteService.deleteNote(noteId).subscribe(data => {
        this.notes = this.noteService.getAllNotes();
      }, error => {
        console.log(error)
      })
  }

}
