package com.dustinmwilliams.AwesomeNotes.exception;

public class NoteNotFoundException extends RuntimeException
{
    public NoteNotFoundException(String message)
    {
        super(message);
    }
}
