package com.example.salaries.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salaries.Models.Transactions;
import com.example.salaries.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends ListAdapter<Transactions, TransactionAdapter.TransactionHolder> {


    public TransactionAdapter() {
        super(DIFF_CALLBACK);
    }


    private static DiffUtil.ItemCallback<Transactions> DIFF_CALLBACK = new DiffUtil.ItemCallback<Transactions>() {
        @Override
        public boolean areItemsTheSame(@NonNull Transactions oldItem, @NonNull Transactions newItem) {
            return oldItem.getDate().equals(newItem.getDate());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transactions oldItem, @NonNull Transactions newItem) {
            return true;
        }
    };


    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_layout, parent, false);
        return new TransactionHolder(view);
    }


    public String getDateOfTransactionAt(int position) {
        return  getItem(position).getDate();
    }


    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Transactions transaction = getItem(position);
        holder.name.setText(transaction.getName());
        holder.date.setText(transaction.getDate());
        holder.amount.setText(transaction.getAmount());
        holder.current_credit.setText(String.valueOf(transaction.getCurrentCredit()));
    }


    class TransactionHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView date;
        TextView amount;
        TextView current_credit;
        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Name);
            date = itemView.findViewById(R.id.Date);
            amount = itemView.findViewById(R.id.Amount);
            current_credit = itemView.findViewById(R.id.Current_Credit);
        }
    }
}