package com.dustinmwilliams.AwesomeNotes.controller;

import com.dustinmwilliams.AwesomeNotes.dto.NoteRequest;
import com.dustinmwilliams.AwesomeNotes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController
{
    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity createNote(@RequestBody NoteRequest noteRequest)
    {
        noteService.createNote(noteRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteNote(@PathVariable @RequestBody Long id)
    {
        noteService.deleteNote(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity editNote(@RequestBody NoteRequest noteRequest)
    {
        noteService.editNote(noteRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NoteRequest>> getAllNotes()
    {
        return new ResponseEntity<>(noteService.getAllNotes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteRequest> getNote(@PathVariable @RequestBody Long id)
    {
        return new ResponseEntity<>(noteService.getNote(id), HttpStatus.OK);
    }
}
