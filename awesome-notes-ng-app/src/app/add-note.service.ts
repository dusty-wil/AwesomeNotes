import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { NoteRequest } from './add-note/noteRequest';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddNoteService {

    private url = 'http://localhost:8080/api/notes';

    constructor(private httpClient: HttpClient) {

    }

    addNote(noteRequest: NoteRequest): Observable<any> {
       return this.httpClient.post(this.url, noteRequest);
    }

    editNote(noteRequest: NoteRequest): Observable<any> {
        return this.httpClient.put(this.url + '/' + noteRequest.id, noteRequest);
    }

    deleteNote(noteId): Observable<any> {
        return this.httpClient.delete(this.url + '/' + noteId);
    }

    getAllNotes(): Observable<Array<NoteRequest>> {
       return this.httpClient.get<Array<NoteRequest>>(this.url + '/all');
    }

    getNote(noteId): Observable<any> {
        return this.httpClient.get(this.url + '/' + noteId);
    }
}
