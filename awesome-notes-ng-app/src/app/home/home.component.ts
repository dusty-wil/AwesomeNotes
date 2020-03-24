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

  isAuthenticated: any;
  notes: Observable<Array<NoteRequest>>;

  constructor(
    private authService: AuthService,
    private noteService: AddNoteService
  ) {
    this.isAuthenticated = false;
   }

  ngOnInit(): void {
    this.isAuthenticated = this.authService.isAuthenticated();
    if (this.isAuthenticated) {
        this.notes = this.noteService.getAllNotes();
    }  

  }

  delete(noteId): void {
    this.noteService.deleteNote(noteId).subscribe(data => {
        console.log(data)
      }, error => {
        console.log(error)
      })
  }

}
