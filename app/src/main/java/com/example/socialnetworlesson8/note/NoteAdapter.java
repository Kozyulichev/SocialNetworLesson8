package com.example.socialnetworlesson8.note;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetworlesson8.R;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Note note;
    private List<Note> notes = new NotesRepo().getNotes();
    private OnNoteClickListener onNoteClickListener;
    private int position;
    private Fragment fragment;
    private int menuPosition;

    public int getMenuPosition() {
        return menuPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void addNote(Note note) {
        notes.add(note);
        int position = notes.size();
        notifyItemInserted(position - 1);
    }

    public void updateNote(Note note, int position) {
        notes.set(position, note);
        notifyItemChanged(position);

    }

    public void clearNote() {
        notes.clear();
        int position = notes.size();
        notifyDataSetChanged();
    }

    public void deleteNote(int a) {
        //int a = getPosition();
        notes.remove(a);
        notifyItemRemoved(a);

    }

    public NoteAdapter(Note note, Fragment fragment) {
        this.note = note;
        this.fragment = fragment;
    }

    public NoteAdapter() {
    }

    public void setOnItemClick(OnNoteClickListener oonNoteClickListener) {
        onNoteClickListener = oonNoteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new NoteViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.onBind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface OnNoteClickListener {
        void onItemClick(Note note);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_text;
        private TextView tv_date;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.name_note1);
            tv_text = itemView.findViewById(R.id.text_note1);
            tv_date = itemView.findViewById(R.id.date_note1);

            registerContextMenu(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    String name = notes.get(position).getName();
                    String text = notes.get(position).getText();
                    String date = notes.get(position).getDate();
                    note = new Note(name, text, date);
                    onNoteClickListener.onItemClick(note);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onLongClick(View v) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(10, 10);
                    return true;
                }
            });
        }

        private void registerContextMenu(View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        menuPosition = getLayoutPosition();
                        return false;
                    }
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public void onBind(Note note) {
            tv_name.setText(note.getName());
            tv_text.setText(note.getText());
            tv_date.setText(note.getDate());
        }
    }

}
