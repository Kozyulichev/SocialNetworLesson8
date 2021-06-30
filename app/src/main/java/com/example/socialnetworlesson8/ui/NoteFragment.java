package com.example.socialnetworlesson8.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetworlesson8.R;
import com.example.socialnetworlesson8.note.Note;
import com.example.socialnetworlesson8.note.NoteAdapter;
import com.example.socialnetworlesson8.note.NotesRepo;
import com.example.socialnetworlesson8.observ.Observer;
import com.example.socialnetworlesson8.observ.Publisher;
import com.example.socialnetworlesson8.ui.DetailsNoteFragment;
import com.example.socialnetworlesson8.ui.MainActivity;

import java.util.Date;
import java.util.List;

public class NoteFragment extends Fragment {
    private static final String CURRENT_POSITION = "currentPosition";
    private int currentPosition = 0;
    private boolean isLandscape;
    private Note note;
    private List<Note> notes = new NotesRepo().getNotes();
    private NoteAdapter noteAdapter;
    private RecyclerView recyclerView;
    int position = 0;
    private Publisher publisher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        publisher = mainActivity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(CURRENT_POSITION);
        }
        if (isLandscape) {
            openLandDetailsFragment(note);
        }
        setHasOptionsMenu(true);
        initView(view);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_POSITION, note);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Date date = new Date();
                String date1 = String.valueOf(date.getDate());
                openDetailsFragment(new Note("Имя заметки", "Текст заметки", date1));
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(Note note) {
                        noteAdapter.addNote(note);
                        recyclerView.scrollToPosition(noteAdapter.getItemCount() - 1);
                    }
                });
                //Toast.makeText(getContext(), "1213131", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        noteAdapter = new NoteAdapter(note, this);
        noteAdapter.setOnItemClick(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onItemClick(Note note) {
                openDetailsFragment(note);
            }
        });
        Context context = getContext();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, LinearLayout.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(noteAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);


        /*for (Note note:notes) {
            View listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, linearLayout, false);

            TextView tv_name = listView.findViewById(R.id.name_note1);
            TextView tv_text = listView.findViewById(R.id.text_note1);
            TextView tv_date = listView.findViewById(R.id.date_note1);

            String name = note.getName();
            String text = note.getText();
            String date = note.getDate();

            tv_name.setText(name);
            tv_text.setText(text);
            tv_date.setText(date);

            linearLayout.addView(listView);

            listView.setOnClickListener(v -> {

                openDetailsFragment(note);
            });
        }*/

        /*for (int i = 0; i < notes.size(); i++) {
            View listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, linearLayout, false);
            String name = notes.get(i).getName();
            String text = notes.get(i).getText();
            String date = notes.get(i).getDate();
            TextView tv_name = listView.findViewById(R.id.name_note1);
            TextView tv_text = listView.findViewById(R.id.text_note1);
            TextView tv_date = listView.findViewById(R.id.date_note1);
            tv_name.setText(name);
            tv_text.setText(text);
            tv_date.setText(date);
            linearLayout.addView(listView);
            final int fi = i;

            listView.setOnClickListener(v -> {
                currentPosition = fi;
                note = new Note(name, text, date);
                openDetailsFragment(note);
            });

        }*/

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                int pos = noteAdapter.getMenuPosition();
                Date date = new Date();
                String date1 = String.valueOf(date.getDate());
                try {
                    note = new Note(notes.get(noteAdapter.getMenuPosition()).getName(),
                            notes.get(noteAdapter.getMenuPosition()).getText(), date1);

                    openDetailsFragment(note);
                    publisher.subscribe(
                            note -> noteAdapter.updateNote(note, pos));
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getContext(), "(((((", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_remove:
                    noteAdapter.deleteNote(noteAdapter.getMenuPosition());

                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void openDetailsFragment(Note note) {
        if (isLandscape) {
            openLandDetailsFragment(note);
        } else openPortDetailsFragment(note);
    }

    private void openLandDetailsFragment(Note note) {
        DetailsNoteFragment detailsNoteFragment = DetailsNoteFragment.newInstance(note);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.details_notes, detailsNoteFragment);
        ft.commit();

    }

    private void openPortDetailsFragment(Note note) {
        /*Intent intent = new Intent(getActivity(), DetailsNoteActivity.class);
        intent.putExtra(DetailsNoteFragment.ARG_PARAM1, note);
        startActivity(intent);*/
        DetailsNoteFragment detailsNoteFragment = DetailsNoteFragment.newInstance(note);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.note_container, detailsNoteFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}