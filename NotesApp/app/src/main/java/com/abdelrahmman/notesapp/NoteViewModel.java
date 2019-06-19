package com.abdelrahmman.notesapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.abdelrahmman.notesapp.models.Note;
import com.abdelrahmman.notesapp.persistence.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;
    private LiveData<List<Note>> mAllNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.retrieveNotesTask();
    }

    public void insert(Note note) {
        mRepository.insertNoteTask(note);
    }

    public void update(Note note) {
        mRepository.updateNote(note);
    }

    public void delete(Note note) {
        mRepository.deleteNote(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

}
