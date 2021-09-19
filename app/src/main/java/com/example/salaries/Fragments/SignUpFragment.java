package com.example.salaries.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.salaries.Activities.MainActivity;
import com.example.salaries.Models.User;
import com.example.salaries.R;
import com.example.salaries.ViewModels.SharedViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText nameEditText;
    EditText companyNameEditText;
    EditText mobileEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button signUpButton;
    NavController navController;
    SharedViewModel sharedViewModel;
    RelativeLayout progressBarLayout;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEditText = view.findViewById(R.id.name);
        companyNameEditText = view.findViewById(R.id.company_shop_name);
        mobileEditText = view.findViewById(R.id.mobile);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password);
        signUpButton = view.findViewById(R.id.sign_up_button);
        progressBarLayout = view.findViewById(R.id.sign_up_progress_bar_layout);

        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        signUpButton.setOnClickListener(this);

        sharedViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });

        sharedViewModel.getIsSucceeded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(requireActivity(), MainActivity.class));
                } else {
                    signUpButton.setEnabled(true);
                    Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_LONG).show();
                }
            }
        });

        
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_up_button) {
            if (validate()) {
                signUpButton.setEnabled(false);
                SignUp();
            } else {
                signUpButton.setEnabled(true);
            }
        } else {
            throw new IllegalStateException("Unexpected Value" + v.getId());
        }
    }

    private void clearFocus() {
        nameEditText.clearFocus();
        companyNameEditText.clearFocus();
        mobileEditText.clearFocus();
        emailEditText.clearFocus();
        passwordEditText.clearFocus();
        confirmPasswordEditText.clearFocus();
    }


    private boolean validate() {
        boolean valid = true;
        clearFocus();

        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("Please Enter Your Name");
            nameEditText.requestFocus();
            valid = false;
        }

        if (companyNameEditText.getText().toString().isEmpty()) {
            companyNameEditText.setError("Please Enter Your Company Name");
            companyNameEditText.requestFocus();
            valid = false;
        }

        if (mobileEditText.getText().toString().isEmpty()) {
            mobileEditText.setError("Please Enter Your Mobile Number");
            mobileEditText.requestFocus();
            valid = false;
        }

        if (emailEditText.getText().toString().isEmpty()) {
            emailEditText.setError("Please Enter Email");
            emailEditText.requestFocus();
            valid = false;
        }

        if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setError("Please Enter Password");
            passwordEditText.requestFocus();
            valid = false;
        }

        if (confirmPasswordEditText.getText().toString().isEmpty()) {
            confirmPasswordEditText.setError("Please Enter Password Again");
            confirmPasswordEditText.requestFocus();
            valid = false;
        }

        if (!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
            passwordEditText.setError("Password did not Match");
            passwordEditText.requestFocus();
            valid = false;
        }

        return valid;
    }


    private void SignUp() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        User user = new User(
                null,
                nameEditText.getText().toString().trim(),
                companyNameEditText.getText().toString().trim(),
                mobileEditText.getText().toString().trim(),
                email
        );

        sharedViewModel.signUpUser(user, password, requireContext());
    }

    private void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        clearFocus();
        super.onStop();
    }
}