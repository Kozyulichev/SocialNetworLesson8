package com.example.socialnetworlesson8.observ;

import com.example.socialnetworlesson8.note.Note;
import com.example.socialnetworlesson8.observ.Observer;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    // Подписать
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    // Отписать
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    // Разослать событие
    public void notifySingle(Note note) {
        for (Observer observer : observers) {
            observer.updateNoteData(note);
            unsubscribe(observer);
        }
    }

}
