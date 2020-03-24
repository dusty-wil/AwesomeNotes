import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { NoteRequest } from './noteRequest';
import { AddNoteService } from '../add-note.service';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-add-note',
  templateUrl: './add-note.component.html',
  styleUrls: ['./add-note.component.css']
})
export class AddNoteComponent implements OnInit {

  addNoteForm: FormGroup;
  noteRequest: NoteRequest;
  addNoteError: String;

  constructor(
      private addNoteService: AddNoteService,
      private router: Router,
      private activatedRoute: ActivatedRoute
  ) {
    this.addNoteForm = new FormGroup({
        title: new FormControl(),
        content: new FormControl()
    });


    this.noteRequest = {
        content: '',
        title: '',
        createdDate: null,
        updateDate: null,
        id: null
    }

    this.addNoteError = null;
   }

  ngOnInit(): void {
    let id = this.activatedRoute.snapshot.paramMap.get('id');
    
    if (id) {
        this.addNoteService.getNote(id).subscribe(data => {
            if (data) {
                this.addNoteForm.get('title').setValue(data.title);
                this.addNoteForm.get('content').setValue(data.content);
                this.noteRequest.id = data.id;
            }
        }, error => {
            console.log(error)
        })
    }
    
  }

  onSubmit() {
    this.addNoteError = null;
    this.noteRequest.content = this.addNoteForm.get('content').value;
    this.noteRequest.title = this.addNoteForm.get('title').value;
    
    if (this.noteRequest.id) {
        this.addNoteService.editNote(this.noteRequest).subscribe(data => {
            this.router.navigateByUrl('/home')
        }, error => {
            this.addNoteError = error.message;
        });
    } else {
        this.addNoteService.addNote(this.noteRequest).subscribe(data => {
            this.router.navigateByUrl('/home')
        }, error => {
            this.addNoteError = error.message;
        });
    }
    
    
  }
}
