package com.example.mauriciogodinez.basedatos1;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private final ViewHolderCallback mCallback;

    public MyAdapter(Context context, ViewHolderCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void swapCursor(Cursor newCursor) {
        // Siempre cerramos el mCursor previo
        if (mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            // Forzamos RecyclerView a actualizarse
            this.notifyDataSetChanged();
        }
    }

    public void setData(Cursor newCursor){
        swapCursor(newCursor);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.my_item_view;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if (!mCursor.moveToPosition(position))
            return; // no hace nada si returned null

        // Update view holder con la informacion necesaria para visualizar
        String name = mCursor.getString(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NAME));
        String lastName = mCursor.getString(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_LASTNAME));
        String marks = mCursor.getString(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_MARKS));
        long id = mCursor.getLong(mCursor.getColumnIndex(StudentContract.StudentEntry._ID));
        // Display data

        StringBuffer buffer = new StringBuffer();

        buffer.append(mContext.getString(R.string.etiqueta_id, String.valueOf(id))).append("\n");
        buffer.append(mContext.getString(R.string.etiqueta_name, name)).append("\n");
        buffer.append(mContext.getString(R.string.etiqueta_lastname, lastName)).append("\n");
        buffer.append(mContext.getString(R.string.etiqueta_marks, marks)).append("\n\n");

        myViewHolder.setItemTextView(buffer.toString());
        myViewHolder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemTextView = itemView.findViewById(R.id.item_text_view);

            itemView.setOnClickListener(this);
        }

        public void setItemTextView(String mItemTextView) {
            this.mItemTextView.setText(mItemTextView);
        }

        @Override
        public void onClick(View view) {
            mCallback.viewHolderOnClick(mItemTextView.getText().toString(),
                    String.valueOf(getAdapterPosition()), String.valueOf(view.getTag()));
        }
    }
}