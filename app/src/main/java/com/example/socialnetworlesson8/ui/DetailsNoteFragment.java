package com.example.socialnetworlesson8.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialnetworlesson8.R;
import com.example.socialnetworlesson8.note.Note;
import com.example.socialnetworlesson8.note.NoteAdapter;
import com.example.socialnetworlesson8.note.NotesRepo;
import com.example.socialnetworlesson8.observ.Publisher;

import java.util.List;

public class DetailsNoteFragment extends Fragment {

    static final String ARG_PARAM1 = "param1";
    private int index;
    private Note note;
    private NoteAdapter noteAdapter = new NoteAdapter();
    private RecyclerView recyclerView;
    private List<Note> notes = new NotesRepo().getNotes();
    private Publisher publisher;
    private EditText et_name;
    private EditText et_text;


    public static DetailsNoteFragment newInstance(Note note) {
        DetailsNoteFragment fragment = new DetailsNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        publisher = mainActivity.getPublisher();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        publisher = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }

    @Override
    public void onStop() {
        super.onStop();
        note = collectNoteData();
    }

    private Note collectNoteData() {
        String name = "Hello";
        if (et_name != null) {
            name = this.et_name.getText().toString();
        }
        String text = "Note";
        if (et_text!=null){
        text = this.et_text.getText().toString();}
        return new Note(name, text, "30.06.2021");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_note, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_PARAM1, note);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(ARG_PARAM1);
        }
        initList(view);
    }

    private void initList(View view) {
        if (getArguments() != null && getArguments().getParcelable(ARG_PARAM1) != null) {
            note = getArguments().getParcelable(ARG_PARAM1);
            et_name = view.findViewById(R.id.name_note);
            et_text = view.findViewById(R.id.text_note);

            Button button = view.findViewById(R.id.btn_save);
            if (note != null) {

                String name = note.getName();
                String text = note.getText();

                et_name.setText(name);
                et_text.setText(text);

            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    //noteAdapter = new NoteAdapter(note1);
                    //notes.add(new Note(et_name.getText().toString()
                    //,et_text.getText().toString(),"15.02.2020"));
                    fm.popBackStack();
                   /* NotesRepo notesRepo = new NotesRepo();
                    notesRepo.getNotes();
                    notesRepo.addNote(new Note(et_name.getText().toString()
                            ,et_text.getText().toString(),"15.02.2020"));*/
                    //noteAdapter.updateNote(new Note(et_name.getText().toString()
                            //,et_text.getText().toString(),"22.05.2021"),noteAdapter.getMenuPosition());
                    //noteAdapter.notifyDataSetChanged();

                }
            });
        }
    }
}