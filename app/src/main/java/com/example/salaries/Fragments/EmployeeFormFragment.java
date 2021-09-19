package com.example.salaries.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.R;
import com.example.salaries.ViewModels.SharedViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class EmployeeFormFragment extends Fragment implements View.OnClickListener{
    private EditText nameEditText;
    private EditText designationEditText;
    private EditText mobileEditText;
    private EditText salaryEditText;
    private EditText dateOfBirthEditText;
    private Button addEmployeeButton;
    private TextView titleTV;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    EmployeeModel model;
    private String employeeId;
    private int balance;
    private int credit;

    public EmployeeFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        nameEditText = view.findViewById(R.id.name);
        dateOfBirthEditText = view.findViewById(R.id.date_of_birth);
        mobileEditText = view.findViewById(R.id.mobile);
        designationEditText = view.findViewById(R.id.designation);
        salaryEditText = view.findViewById(R.id.salary);
        titleTV = view.findViewById(R.id.textView);
        addEmployeeButton = view.findViewById(R.id.add_button);
        addEmployeeButton.setOnClickListener(this);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model = sharedViewModel.getSelected();
                if (model != null) {
                    setDataInTextViews();
                } else {
                    titleTV.setText(R.string.add_emp);
                    employeeId = null;
                    balance = 0;
                    credit = 0;
                }
    }

    private void setDataInTextViews() {
        nameEditText.setText(model.getName());
        designationEditText.setText(model.getDesignation());
        mobileEditText.setText(model.getMobile());
        salaryEditText.setText(model.getSalary());
        dateOfBirthEditText.setText(model.getD_O_B());
        employeeId = model.getEmployId();
        balance = model.getBalance();
        credit = model.getCredit();

        titleTV.setText(R.string.update_emp);
        addEmployeeButton.setText(R.string.modify);
    }

    private void updateAddEmployeeDetails() {
           EmployeeModel model = new EmployeeModel(
                    employeeId,
                    nameEditText.getText().toString().trim(),
                    dateOfBirthEditText.getText().toString(),
                    mobileEditText.getText().toString(),
                    designationEditText.getText().toString().trim(),
                    salaryEditText.getText().toString(),
                    balance,
                    credit
            );

            sharedViewModel.updateAddEmployeeDetails(model);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_button) {
            if (validate()){
                updateAddEmployeeDetails();
                navController.navigate(R.id.action_employeeFormFragment_to_manageEmployeeFragment);
            }
        } else {
            throw new IllegalStateException("Unknown View Clicked : " + v.getId());
        }
    }

    private void clearFocus() {
        nameEditText.clearFocus();
        dateOfBirthEditText.clearFocus();
        mobileEditText.clearFocus();
        designationEditText.clearFocus();
        salaryEditText.clearFocus();
    }

    private boolean validate() {
        boolean valid = true;
        clearFocus();
        if(nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("Please Enter Name");
            nameEditText.requestFocus();
            valid = false;
        }

        if(dateOfBirthEditText.getText().toString().isEmpty()) {
            dateOfBirthEditText.setError("Please Enter Date of Birth");
            dateOfBirthEditText.requestFocus();
            valid = false;
        }

        if(mobileEditText.getText().toString().isEmpty()) {
            mobileEditText.setError("Please Enter Mobile Number");
            mobileEditText.requestFocus();
            valid = false;
        }

        if(designationEditText.getText().toString().isEmpty()) {
            designationEditText.setError("Please Enter Designation");
            designationEditText.requestFocus();
            valid = false;
        }

        if(salaryEditText.getText().toString().isEmpty()) {
            salaryEditText.setError("Please Enter Salary");
            salaryEditText.requestFocus();
            valid = false;
        }

        return valid;
    }

    @Override
    public void onStop() {
        clearFocus();
        super.onStop();
    }

    @Override
    public void onDetach() {
        sharedViewModel.select(null);
        super.onDetach();
    }
}