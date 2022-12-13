package com.example.nustywallpapers.DatabaseListView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nustywallpapers.ImageModel;
import com.example.nustywallpapers.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    ArrayList<ImageModel> mImageModelArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ImageAdapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.mImageModelArrayList = imageModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageModel imageModel = mImageModelArrayList.get(position);
        String uri = imageModel.getPath()+"%2F"+imageModel.getName();
        holder.myImageView.setImageURI(Uri.parse(uri));
    }

    @Override
    public int getItemCount() {
        return mImageModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.list_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    String getItem(int id) {
        return mImageModelArrayList.get(id).toString();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
