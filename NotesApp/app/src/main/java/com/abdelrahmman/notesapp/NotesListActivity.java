package com.abdelrahmman.notesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import android.widget.Toast;

import com.abdelrahmman.notesapp.adapters.NotesRecyclerAdapter;
import com.abdelrahmman.notesapp.models.Note;
import com.abdelrahmman.notesapp.utils.VerticalItemSpacingDecorator;

import java.util.List;

public class NotesListActivity extends AppCompatActivity implements View.OnClickListener {

    //ui components
    private RecyclerView mRecyclerView;

    //vars
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        mRecyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.fab).setOnClickListener(this);

        initRecyclerView();
    }

    private void initRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        VerticalItemSpacingDecorator itemSpacingDecorator = new VerticalItemSpacingDecorator(10);
        mRecyclerView.addItemDecoration(itemSpacingDecorator);

        final NotesRecyclerAdapter mAdapter = new NotesRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                mAdapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(mAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(NotesListActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);


        mAdapter.setOnItemClickListener(new NotesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(NotesListActivity.this, NotesActivity.class);
                intent.putExtra(NotesActivity.EXTRA_ID, note.getId());
                intent.putExtra(NotesActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(NotesActivity.EXTRA_CONTENT, note.getContent());
                intent.putExtra(NotesActivity.EXTRA_TIMESTAMP, note.getTimestamp());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab: {
                Intent intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
