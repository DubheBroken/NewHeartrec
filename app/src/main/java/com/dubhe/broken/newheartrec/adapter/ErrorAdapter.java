package com.dubhe.broken.newheartrec.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.dubhe.broken.newheartrec.R;

import java.util.List;

public class ErrorAdapter extends RecyclerView.Adapter<ErrorAdapter.ErrorViewHolder> {

    private List<Error> list;
    private Context context;

    public ErrorAdapter(Context context, List<Error> list) {
        this.list = list;
        this.context = context;
    }

    public ErrorAdapter(List<Error> list) {
        this.list = list;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ErrorViewHolder holder, int position) {
        //给控件赋值
//        holder.textErrorImage.setBackground(context.getResources().getDrawable(list.get(position).error_image));
//        holder.textErrorText.setText(list.get(position).error_text);
//        if ((list.get(position).error_button_text) != null && !"".equals(list.get(position).error_button_text)) {
//            holder.buttonError.setText(list.get(position).error_button_text);
//            holder.buttonError.setVisibility(View.VISIBLE);
//            holder.buttonError.setOnClickListener(list.get(position).error_button_onclick);
//        }else{
//            holder.buttonError.setVisibility(View.INVISIBLE);
//        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void onBindViewHolder(@NonNull ErrorViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @NonNull
    @Override
    public ErrorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.error_layout, parent, false);
        return new ErrorViewHolder(v);
    }


    class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView textErrorImage;
        TextView textErrorText;
        Button buttonError;

        private ErrorViewHolder(View view) {
            super(view);
//            textErrorImage = view.findViewById(R.id.text_error_image);
//            textErrorText = view.findViewById(R.id.text_error_text);
//            buttonError = view.findViewById(R.id.button_error);

//                zMerchantName.setOnLongClickListener(view1 -> {
//                    onItemLongClickListener.onItemLongClick(view1, getAdapterPosition());
//                    return false;
//                });
//                zResponsibility.setOnLongClickListener(view1 -> {
//                    onItemLongClickListener.onItemLongClick(view1, getAdapterPosition());
//                    return false;
//                });
//                zCustomerType.setOnLongClickListener(view1 -> {
//                    onItemLongClickListener.onItemLongClick(view1, getAdapterPosition());
//                    return false;
//                });
//                zMerchantResource.setOnLongClickListener(view1 -> {
//                    onItemLongClickListener.onItemLongClick(view1, getAdapterPosition());
//                    return false;
//                });
//                zArea.setOnLongClickListener(view1 -> {
//                    onItemLongClickListener.onItemLongClick(view1, getAdapterPosition());
//                    return false;
//                });
//                zCreationTime.setOnLongClickListener(view1 -> {
//                    onItemLongClickListener.onItemLongClick(view1, getAdapterPosition());
//                    return false;
//                });
//                constraintlayoutMerchantItem.setOnLongClickListener(v -> {
//                    onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
//                    return false;
//                });
        }
    }


}