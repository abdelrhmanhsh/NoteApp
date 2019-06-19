package com.abdelrahmman.notesapp.async;

import android.os.AsyncTask;
import android.util.Log;

import com.abdelrahmman.notesapp.models.Note;
import com.abdelrahmman.notesapp.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private static final String TAG = "InsertAsyncTask";

    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao dao){
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackground: thread: " + Thread.currentThread().getName());
        mNoteDao.insert(notes[0]);
        return null;
    }
}
