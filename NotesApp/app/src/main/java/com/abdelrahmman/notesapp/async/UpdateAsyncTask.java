package com.abdelrahmman.notesapp.async;

import android.os.AsyncTask;
import android.util.Log;

import com.abdelrahmman.notesapp.models.Note;
import com.abdelrahmman.notesapp.persistence.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {

    private static final String TAG = "InsertAsyncTask";

    private NoteDao mNoteDao;

    public UpdateAsyncTask(NoteDao dao){
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackground: thread: " + Thread.currentThread().getName());
        mNoteDao.update(notes[0]);
        return null;
    }
}
