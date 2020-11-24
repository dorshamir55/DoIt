package com.example.doit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doit.R;
import com.example.doit.model.QuestionPostData;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class ChoosePictureAccountAdapter extends RecyclerView.Adapter<ChoosePictureAccountAdapter.PictureViewHolder> {
    private List<Uri> uriList;
    private Context context;
    private MyPictureListener listener;

    public interface MyPictureListener {
        void onPictureClicked(int position, View view);
    }

    public void setListener(MyPictureListener listener) {
        this.listener=listener;
    }

    public ChoosePictureAccountAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Uri> uriList) {
        this.uriList = uriList;
    }

    public List<Uri> getData() {
        return uriList;
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv;

        public PictureViewHolder(View itemView) {
            super(itemView);

            imageIv = itemView.findViewById(R.id.choose_picture_account_image);

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    if(listener!=null)
                        listener.onPictureClicked(getAdapterPosition(), v);
                    return false;
                }
            });
        }
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_picture_account_layout, parent, false);
        PictureViewHolder pictureViewHolder = new PictureViewHolder(view);
        return pictureViewHolder;
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        assert uriList != null;

        Glide
                .with(context)
                .load(uriList.get(position))
                .apply(new RequestOptions())
                .into(holder.imageIv);
    }

    @Override
    public int getItemCount() {
        if(uriList == null)
            return 0;
        return uriList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}