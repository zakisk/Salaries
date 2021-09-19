package com.example.salaries.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.Models.Transactions;
import com.example.salaries.R;
import com.example.salaries.ViewModels.SharedViewModel;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class PayFragment extends Fragment implements View.OnClickListener{
    TextView NameTV;
    TextView BalanceTV;
    TextView CreditTV;
    EditText amountEditText;
    NavController navController;
    EmployeeModel model;
    SharedViewModel sharedViewModel;

    public PayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NameTV = view.findViewById(R.id.payee_name);
        BalanceTV = view.findViewById(R.id.payee_balance);
        CreditTV = view.findViewById(R.id.payee_credit);
        amountEditText = view.findViewById(R.id.amount_edit_text);
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        view.findViewById(R.id.pay_button).setOnClickListener(this);
        model = sharedViewModel.getSelected();
        setTextInViews();
    }


    private void setTextInViews() {
        NameTV.setText(model.getName());
        BalanceTV.setText(String.valueOf(model.getBalance()));
        CreditTV.setText(String.valueOf(model.getCredit()));
        amountEditText.setText(String.valueOf(model.getBalance()));
    }

    private void pay() {
        if (amountEditText.getText().toString().isEmpty() ||
                amountEditText.getText().toString().equals("0")) {
            amountEditText.requestFocus();
            amountEditText.setError("Please Enter Amount");
            return;
        }

        addTransaction();
        sharedViewModel.updateAddEmployeeDetails(model);

        String message = "Dear " + model.getName() +
                ",\n" + "you're paid " + amountEditText.getText().toString() +
                "\n" + "Current Balance : " + model.getBalance() +
                "\n" + "Current Credit : " + model.getCredit();

        sendSms("0" + model.getMobile(), message);
        navController.navigate(R.id.action_global_mainFragment2);
    }


    private void addTransaction() {

        int amount, balance, credit;
        amount = Integer.parseInt(amountEditText.getText().toString().trim());
        balance = model.getBalance();
        credit = model.getCredit();

        if (amount <= balance) {
            balance = balance - amount;
        } else {
            credit = credit + (amount - balance);
            balance = 0;
        }

        model.setBalance(balance);
        model.setCredit(credit);

        sharedViewModel.addTransaction(new Transactions(
                new Date().toString(),
                amountEditText.getText().toString(),
                model.getName(),
                credit
        ));
    }

    private void sendSms(String phone, String message) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Please Allow Send SMS Permission", Toast.LENGTH_LONG).show();
        } else {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClick(View v) {
        clearFocus();
        if (v.getId() == R.id.pay_button) {
            pay();
        } else {
            throw new IllegalStateException("Unknown View Clicked : " + v.getId());
        }
    }

    private void clearFocus() {
        amountEditText.clearFocus();
    }

    @Override
    public void onDetach() {
        sharedViewModel.select(null);
        super.onDetach();
    }

    @Override
    public void onStop() {
        clearFocus();
        super.onStop();
    }

}