package com.dustinmwilliams.AwesomeNotes.repository;

import com.dustinmwilliams.AwesomeNotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dustinmwilliams.AwesomeNotes.model.Note;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>
{
    List<Note> findNotesByUserId(Long userId);
    Optional<Note> findNoteByIdAndUserId(Long id, Long userId);
}
