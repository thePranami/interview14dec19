package com.example.interview;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private List<DataModel> modelList;
    ClickListener clickListener;
    Context context;

    public DataAdapter(List<DataModel> modelList, Context context, ClickListener clickListener) {
        this.modelList = modelList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {
        final DataModel dataModel = modelList.get(position);

        Picasso.get().load(dataModel.getImage()).into(holder.imageView);
        //holder.imageView.setImageResource(dataModel.getImage());
        holder.textView.setText(dataModel.getName());
        holder.clickBind(dataModel, clickListener);
//        if (position % 3 == 0)
//        {
//            holder.textView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
//        }
//        else  if (position % 2 == 0)
//        {
//            holder.textView.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_primary_dark));
//        }
//        else
//        {
//            holder.textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//        }



//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent((v.getContext()), ViewActivity.class);
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("viewData", dataModel);
//                intent.putExtra("name", dataModel.getName());
//                intent.putExtra("image", dataModel.getImage());
//                v.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.name);
            imageView= itemView.findViewById(R.id.image);
        }

        public void clickBind(final DataModel model, final ClickListener clickListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(model);
                }
            });
        }
    }

    public interface ClickListener{
        void onClick(DataModel item);
    }
}
