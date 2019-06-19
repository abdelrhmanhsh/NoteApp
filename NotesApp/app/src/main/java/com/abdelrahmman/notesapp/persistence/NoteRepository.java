package com.abdelrahmman.notesapp.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.abdelrahmman.notesapp.async.DeleteAsyncTask;
import com.abdelrahmman.notesapp.async.InsertAsyncTask;
import com.abdelrahmman.notesapp.async.UpdateAsyncTask;
import com.abdelrahmman.notesapp.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        mNoteDatabase = NoteDatabase.getInstance(application);
        mNoteDao = mNoteDatabase.getNoteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note){
        new UpdateAsyncTask((mNoteDatabase.getNoteDao())).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {
        return mAllNotes;
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
}