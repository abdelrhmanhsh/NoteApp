package com.abdelrahmman.notesapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abdelrahmman.notesapp.models.Note;
import com.abdelrahmman.notesapp.persistence.NoteRepository;
import com.abdelrahmman.notesapp.utils.Utility;

public class NotesActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private static final String TAG = "NotesActivity";

    public static final int EDIT_MODE_ENABLED = 1;
    public static final int EDIT_MODE_DISABLED = 0;

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "EXTRA_CONTENT";
    public static final String EXTRA_TIMESTAMP = "EXTRA_TIMESTAMP";

    //ui components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;

    //vars
    private boolean mIsNewNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private Note mNote;
    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mLinedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.check);
        mBackArrow = findViewById(R.id.back_arrow);
        mNoteViewModel = new NoteViewModel(getApplication());

        if (getIncomingIntent()) {
            //new Note (Edit Mode)
            setNewNote();
            enableEditMode();

        } else {
            //not new Note (View Mode)
            disableContentInteraction();
        }

        setListeners();

    }

    private void setListeners() {
        mLinedEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }

    private boolean getIncomingIntent(){
        if (getIntent().hasExtra(EXTRA_ID)) {
            mNote = new Note();
            mNote.setTitle(getIntent().getStringExtra(EXTRA_TITLE));
            mNote.setContent(getIntent().getStringExtra(EXTRA_CONTENT));
            mNote.setTimestamp(getIntent().getStringExtra(EXTRA_TIMESTAMP));
            mNote.setId(getIntent().getIntExtra(EXTRA_ID, 1));

            mViewTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            mEditTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            mLinedEditText.setText(getIntent().getStringExtra(EXTRA_CONTENT));

            mMode = EDIT_MODE_ENABLED;
            mIsNewNote = false;
            return false;

        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;

    }

    private void saveChanges() {
        if (mIsNewNote) {
            saveNewNote();
        } else {
            updateNote();
        }
    }

    private void updateNote() {
        mNoteViewModel.update(mNote);
        Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
    }

    private void saveNewNote() {
        mNoteViewModel.insert(mNote);
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }

    private void disableContentInteraction() {
        mLinedEditText.setKeyListener(null);
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.clearFocus();
    }

    private void enableContentInteraction() {
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.requestFocus();
    }

    private void enableEditMode() {
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    private void disableEditMode() {
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        disableContentInteraction();

        String temp = mLinedEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if (temp.length() > 0) {
            mNote.setTitle(mEditTitle.getText().toString());
            mNote.setContent(mLinedEditText.getText().toString());
            String timestamp = Utility.getCurrentTimestamp();
            mNote.setTimestamp(timestamp);

            if (!mNote.getContent().equals(getIntent().getStringExtra(EXTRA_CONTENT)) || !mNote.getTitle().equals(getIntent().getStringExtra(EXTRA_TITLE))) {
                saveChanges();
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();

        if (view == null) {
            view = new View(this);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setNewNote() {
        mViewTitle.setText("note title");
        mEditTitle.setText("note title");

        mNote = new Note();
        mNote.setTitle("note title");
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        Log.d(TAG, "onDoubleTap: double tapped.");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.check: {
                disableEditMode();
                hideSoftKeyboard();
                break;
            }

            case R.id.note_text_title: {
                enableEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }

            case R.id.back_arrow: {
                finish();
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED) {
            onClick(mCheck);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if (mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mViewTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
