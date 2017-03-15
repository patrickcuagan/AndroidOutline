package com.example.patrick.outline;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Patrick on 3/14/17.
 */

public class NoteAdapter extends CursorRecyclerViewAdapter<NoteAdapter.NoteViewHolder>{

    public NoteAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID));
        String text = cursor.getString(cursor.getColumnIndex(Note.COLUMN_TEXT));
        String date = cursor.getString(cursor.getColumnIndex(Note.COLUMN_DATE));
        Note note = new Note(id, text, date);

        viewHolder.tvText.setText(text);
        viewHolder.tvDate.setText(note.getDate_created() + "");

        // For passing to onClickListener
        viewHolder.container.setTag(id);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    int id = (int) v.getTag();
                    onItemClickListener.onItemClick(id);
                }
            }
        });
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);

        return new NoteViewHolder(v);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        TextView tvDate;
        CardView container;
        public NoteViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            container = (CardView) itemView.findViewById(R.id.container);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int id);
    }
}
