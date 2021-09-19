package com.example.salaries.WorkManager;

import android.content.Context;

import com.example.salaries.Models.EmployeeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public boolean execute = true;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        updateData();
        return Result.success();
    }

    private void updateData() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Minaz Patel");
        int date = new GregorianCalendar().get(GregorianCalendar.DATE);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (execute) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            EmployeeModel model = ds.getValue(EmployeeModel.class);

                            assert model != null;
                            int balance, credit;
                            balance = model.getBalance() + Integer.parseInt(model.getSalary());
                            credit = model.getCredit();

                            if (credit >= balance) {
                                credit -= balance;
                                balance = 0;
                            } else {
                                balance -= credit;
                                credit = 0;
                            }
                            reference.child(model.getEmployId()).child("balance").setValue(balance);
                            reference.child(model.getEmployId()).child("credit").setValue(credit);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        if (date == 1) {
                reference.addValueEventListener(listener);

                reference.removeEventListener(listener);
        }
            else {

                String today = new Date().toString();

                FirebaseDatabase.getInstance().getReference("Logs")
                    .child(String.valueOf(System.currentTimeMillis()/100))
                        .setValue("on " + today + " Operation not Executed.");
        }
        if (date == 2){
            execute = true;
        }
    }
}
