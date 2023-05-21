package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

/**
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.administrator.ClothingRentalSystem.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context context;
    private List<book> mBookList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.book_image);
            textView = (TextView) view.findViewById(R.id.book_name);

        }

    }

    public BookAdapter(List<book> bookList) {
        mBookList = bookList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();

        }
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        book book1 = mBookList.get(position);
        holder.textView.setText(book1.getName());

        Glide.with(context).load(book1.getImageId()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
