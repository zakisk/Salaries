package com.example.salaries.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salaries.Activities.SignInActivity;
import com.example.salaries.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainFragment extends Fragment implements View.OnClickListener {

    NavController navController;
    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(requireActivity(), SignInActivity.class);
            intent.addCategory(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


        view.findViewById(R.id.manage_employees_button).setOnClickListener(this);
        view.findViewById(R.id.transactions_button).setOnClickListener(this);
        view.findViewById(R.id.main_pay_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_employees_button :
                navController.navigate(R.id.action_mainFragment2_to_manageEmployeeFragment);
                break;
            case R.id.transactions_button :
                navController.navigate(R.id.action_mainFragment2_to_transactionFragment);
                break;
            case R.id.main_pay_button :
                navController.navigate(R.id.action_mainFragment2_to_selectForPayFragment);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}