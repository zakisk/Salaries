package com.example.salaries.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class UpdateDeleteAdapter extends ListAdapter<EmployeeModel, UpdateDeleteAdapter.ViewHolder> {
    OnItemClickListener listener;

    public UpdateDeleteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<EmployeeModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<EmployeeModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull EmployeeModel oldItem, @NonNull EmployeeModel newItem) {
            return oldItem.getEmployId().equals(newItem.getEmployId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull EmployeeModel oldItem, @NonNull EmployeeModel newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getD_O_B().equals(newItem.getD_O_B()) &&
                    oldItem.getDesignation().equals(newItem.getDesignation()) &&
                    oldItem.getMobile().equals(newItem.getMobile()) &&
                    oldItem.getSalary().equals(newItem.getSalary());
        }
    };

    @Override
    public void onBindViewHolder(@NonNull final UpdateDeleteAdapter.ViewHolder holder, final int position) {

        final EmployeeModel model = getItem(position);
        holder.name.setText(model.getName());
        holder.salary.setText(model.getSalary());
        holder.designation.setText(model.getDesignation());
    }


    @NonNull
    @Override
    public UpdateDeleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_layout, parent, false);
        Log.d("Modify_Delete_Activity", "onCreateViewHolder & view type : " + viewType);
        return new UpdateDeleteAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView designation;
        TextView salary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mName);
            salary = itemView.findViewById(R.id.mSalary);
            designation = itemView.findViewById(R.id.mDesignation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.Update(getItem(position));
                    }
                }
            });
        }
    }



    public interface OnItemClickListener {
        void Update(EmployeeModel model);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
