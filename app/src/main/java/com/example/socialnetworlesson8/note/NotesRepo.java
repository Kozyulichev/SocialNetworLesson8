package com.example.socialnetworlesson8.note;

import java.util.ArrayList;
import java.util.List;

public class NotesRepo {

    private List<Note> notes = new ArrayList<>();

    public List<Note> getNotes() {
        //ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note("Дело 1", "Сделать дело 1", "22.04.12"));
        notes.add(new Note("Дело 2", "Сделать дело 2", "22.04.12"));
        notes.add(new Note("Дело 3", "Сделать дело 3", "22.04.12"));
        notes.add(new Note("Дело 4", "Сделать дело 4", "22.04.12"));
        notes.add(new Note("Дело 5", "Сделать дело 5", "22.04.12"));
        return notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }


}
