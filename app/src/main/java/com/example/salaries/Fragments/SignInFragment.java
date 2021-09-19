package com.example.salaries.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.salaries.Activities.MainActivity;
import com.example.salaries.R;
import com.example.salaries.ViewModels.SharedViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SignInFragment extends Fragment implements View.OnClickListener {
    EditText emailEditText;
    EditText passwordEditText;
    Button LoginButton;
    TextView ForgetPasswordTV;
    TextView SignUpClickTV;
    SharedViewModel sharedViewModel;
    NavController navController;
    RelativeLayout progressBarLayout;
    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        LoginButton = view.findViewById(R.id.sign_in_button);
        ForgetPasswordTV = view.findViewById(R.id.forgot_password_textView);
        SignUpClickTV = view.findViewById(R.id.sign_up_textView);
        progressBarLayout = view.findViewById(R.id.sign_in_progress_bar_layout);
        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        LoginButton.setOnClickListener(this);
        SignUpClickTV.setOnClickListener(this);

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
                    startActivity(new Intent(requireContext(), MainActivity.class));
                } else {
                    emailEditText.setError("");
                    LoginButton.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        clearFocus();
        switch (v.getId()) {
            case R.id.sign_in_button :
                if (validate()){
                    LoginButton.setEnabled(false);
                    SignIn();
                }
                break;
            case R.id.sign_up_textView :
                    navController.navigate(R.id.action_signInFragment_to_signUpFragment);
                break;
            case R.id.forgot_password_textView:

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void clearFocus() {
        emailEditText.clearFocus();
        passwordEditText.clearFocus();
    }

    private boolean validate() {
        boolean valid = true;

        if (emailEditText.getText().toString().isEmpty()){
            emailEditText.setError("Please Enter Email Address");
            emailEditText.requestFocus();
            valid = false;
        }

        if (passwordEditText.getText().toString().isEmpty()){
            emailEditText.setError("Please Enter Password");
            emailEditText.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void SignIn() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        sharedViewModel.signInUser(email, password, getContext());
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