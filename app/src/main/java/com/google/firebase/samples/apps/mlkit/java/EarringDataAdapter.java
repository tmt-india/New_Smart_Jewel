package com.google.firebase.samples.apps.mlkit.java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.samples.apps.mlkit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EarringDataAdapter extends RecyclerView.Adapter<EarringDataAdapter.MyDataHolder> {

    Context mContext;
    ArrayList<EarringData> mEarringDataArrayList;

    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(int position); // create callback function
    }

    public EarringDataAdapter(Context mContext, ArrayList<EarringData> mEarringDataArrayList, ListAdapterListener mListener) {
        this.mContext = mContext;
        this.mEarringDataArrayList = mEarringDataArrayList;

        this.mListener = mListener; // receive mListener from Fragment (or Activity)

    }

    @NonNull
    @Override
    public MyDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDataHolder(LayoutInflater.from(mContext).inflate(R.layout.new_lay,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataHolder holder, final int position) {

//        holder.mTxtName.setText(mEarringDataArrayList.get(position).getmName());
        Picasso.get().load(mEarringDataArrayList.get(position).getmImgUrl()).into(holder.mImgVw);


        holder.mImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LivePreviewActivity.mEarringId = position;
                mListener.onClickAtOKButton(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mEarringDataArrayList.size();
    }

    public class MyDataHolder extends RecyclerView.ViewHolder {

        TextView mTxtName;
        ImageView mImgVw;

        public MyDataHolder(@NonNull View itemView) {
            super(itemView);

            mTxtName = itemView.findViewById(R.id.txtName);
            mImgVw = itemView.findViewById(R.id.imgSrc);

        }
    }
}
