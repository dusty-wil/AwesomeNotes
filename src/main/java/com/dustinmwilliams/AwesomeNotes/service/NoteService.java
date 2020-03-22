package com.dustinmwilliams.AwesomeNotes.service;

import com.dustinmwilliams.AwesomeNotes.dto.NoteRequest;
import com.dustinmwilliams.AwesomeNotes.exception.NoteNotFoundException;
import com.dustinmwilliams.AwesomeNotes.model.Note;
import com.dustinmwilliams.AwesomeNotes.model.User;
import com.dustinmwilliams.AwesomeNotes.repository.NoteRepository;
import com.dustinmwilliams.AwesomeNotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class NoteService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private AuthService authService;

    public NoteRequest createNote(NoteRequest noteRequest)
    {
        return authService
            .getCurrentUser()
            .map(user -> {
                User noteUser = userRepository.findUserByUserName(user.getUsername()).orElseThrow(() -> {
                    return new UsernameNotFoundException("User doesn't exist");
                });

                Note note = new Note(
                    noteRequest.getTitle(),
                    noteRequest.getContent(),
                    noteUser
                );

                return mapFromNoteToDTO(noteRepository.save(note));
            }).orElseThrow(() -> new IllegalArgumentException("Not logged in."));
    }

    public NoteRequest editNote(NoteRequest noteRequest)
    {
        return authService
            .getCurrentUser()
            .map(user -> {
                User notesUser = userRepository.findUserByUserName(user.getUsername()).orElseThrow(() -> {
                    return new UsernameNotFoundException("Username not found");
                });

                Note note = noteRepository.findNoteByIdAndUserId(noteRequest.getId(), notesUser.getId()).orElseThrow(() -> {
                    return new NoteNotFoundException("No note found for this id.");
                });

                note.setTitle(noteRequest.getTitle());
                note.setContent(noteRequest.getContent());
                note.setUpdateDate(Instant.now());

                return mapFromNoteToDTO(noteRepository.save(note));
            }).orElseThrow(() -> new IllegalArgumentException("Not logged in."));
    }

    public void deleteNote(Long noteId)
    {
        authService
            .getCurrentUser()
            .map(user -> {
                User notesUser = userRepository.findUserByUserName(user.getUsername()).orElseThrow(() -> {
                    return new UsernameNotFoundException("Username not found");
                });

                Note note = noteRepository.findNoteByIdAndUserId(noteId, notesUser.getId()).orElseThrow(() -> {
                    return new NoteNotFoundException("No note found for this id.");
                });

                noteRepository.delete(note);
                return true;
            }).orElseThrow(() -> new IllegalArgumentException("Not logged in."));
    }

    public List<NoteRequest> getAllNotes()
    {
        return authService
            .getCurrentUser()
            .map(user -> {
                User notesUser = userRepository.findUserByUserName(user.getUsername()).orElseThrow(() -> {
                    return new UsernameNotFoundException("Username not found");
                });

                List<Note> notes = noteRepository.findNotesByUserId(notesUser.getId());
                return notes.stream().map(this::mapFromNoteToDTO).collect(toList());
            }).orElseThrow(() -> new IllegalArgumentException("Not logged in."));
    }

    public NoteRequest getNote(Long noteId)
    {
        return authService
            .getCurrentUser()
            .map(user -> {
                User notesUser = userRepository.findUserByUserName(user.getUsername()).orElseThrow(() -> {
                    return new UsernameNotFoundException("Username not found");
                });

                Note note = noteRepository.findNoteByIdAndUserId(noteId, notesUser.getId()).orElseThrow(() -> {
                    return new NoteNotFoundException("No note found for this id.");
                });

                return mapFromNoteToDTO(note);
            }).orElseThrow(() -> new IllegalArgumentException("Not logged in."));
    }

    private NoteRequest mapFromNoteToDTO(Note note)
    {
        NoteRequest noteReq = new NoteRequest();

        noteReq.setId(note.getId());
        noteReq.setTitle(note.getTitle());
        noteReq.setContent(note.getContent());
        noteReq.setCreatedDate(note.getCreatedDate());
        noteReq.setUpdateDate(note.getUpdateDate());

        return noteReq;
    }
}
