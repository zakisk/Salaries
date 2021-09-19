package com.example.salaries.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class employeeAdapter extends RecyclerView.Adapter<employeeAdapter.listViewHolder> {
    OnItemClickListener listener;
    List<EmployeeModel> list = new ArrayList<>();

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, int position) {
        EmployeeModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.designation.setText(model.getDesignation());
        holder.salary.setText(model.getSalary());
    }

    public void setList(List<EmployeeModel> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder{
        TextView name, designation, salary;
        public listViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.EmpName);
            designation = itemView.findViewById(R.id.EmpDesignation);
            salary = itemView.findViewById(R.id.EmpSalary);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(EmployeeModel model);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}