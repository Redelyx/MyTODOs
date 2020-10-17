package com.example.androidmobdev;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ToDo> mDataset;
    private Context mContext = null;
    private int mType = 0;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View v = null;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
            v.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mContext = v.getContext();
                    int position = getAdapterPosition();
                    ToDo todo = mDataset.get(position);
                    ToDoManager.getInstance(mContext).modifyToDoStatus(todo);
                    switch(mType){
                        case 0:
                            setmDataset(ToDoManager.getInstance(mContext).getToDoList()); break;
                        case 1:
                            setmDataset(ToDoManager.getInstance(mContext).getYetToDoList()); break;
                        case 2:
                            setmDataset(ToDoManager.getInstance(mContext).getDoneList()); break;
                        default:
                            setmDataset(ToDoManager.getInstance(mContext).getToDoList()); break;
                    }
                    notifyDataSetChanged();
                    return false;
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//-----------------------------------------------------------------
                    Log.d("TAG", "Click, add new ToDo");
                    mContext = v.getContext();

                    Intent intent = new Intent(mContext, ToDoActivity.class);

                    int position = getAdapterPosition();  // get position of clicked item

                    //ToDo todo = mDataset.get(position);   // get clicked new object from mDataset(mDataset is an ArrayList)

                    intent.putExtra("myToDoPos", position);

                    mContext.startActivity(intent);
                }
            });
        }




        //recupera dalla vista il campo di testo e imposta il testo che è stato passato
        public void setText(String text) {
            TextView tView = (TextView) v.findViewById(R.id.myTextView);
            tView.setText(text);
        }

        public void setImage(int id) {
            ImageView tView = (ImageView) v.findViewById(R.id.checkBox);
            tView.setImageResource(id);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<ToDo> myDataset, Context context, int i) {
        mDataset = myDataset;
        mContext = context;
        mType = i;
    }

    // Create new views (invoked by the layout manager), crea una cella in funzione della posizione
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_element, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ToDo todo = mDataset.get(position);
         if(todo.getStatus()) {
            holder.setText(mDataset.get(position).getName());
            holder.setImage(R.drawable.ic_check_box_white_18dp);
        } else {
            holder.setText(mDataset.get(position).getName());
            holder.setImage(R.drawable.ic_check_box_outline_blank_white_18dp);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public List<ToDo> getmDataset() {
        return mDataset;
    }

    public void setmDataset(List<ToDo> mDataset) {
        this.mDataset = mDataset;
    }

}
