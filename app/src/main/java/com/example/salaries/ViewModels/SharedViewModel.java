package com.example.salaries.ViewModels;

import android.content.Context;
import android.widget.Toast;

import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.Models.Transactions;
import com.example.salaries.Models.User;
import com.example.salaries.Repositories.SharedRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel implements SharedRepository.OnDataLoadedListener {

    private MutableLiveData<List<EmployeeModel>> allEmployees;

    private MutableLiveData<List<Transactions>> allTransactions;

    private MutableLiveData<Boolean> isDataLoaded = new MutableLiveData<>();

    private MutableLiveData<Long> isDataEmpty = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private EmployeeModel Selected = null;

    private SharedRepository repository = SharedRepository.getInstance();

    private MutableLiveData<Boolean> isSucceeded = new MutableLiveData<>();

    private FirebaseAuth mAuth;


    public void init(String query) {
        if (query.equals(SharedRepository.EMPLOYEES_PATH)) {
                allEmployees = repository.getAllEmployees(query);

        } else if (query.equals(SharedRepository.TRANSACTIONS_PATH)) {
                allTransactions = repository.getAllTransactions(query);

        }
        isDataLoaded.setValue(false);
        repository.setOnDataLoadedListener(this);
    }


    public void updateAddEmployeeDetails(EmployeeModel model) {
        repository.updateAddEmployeeDetails(model);
    }


    public void deleteEmployee(String employeeId) {
        repository.deleteEmployee(employeeId);
    }


    public void deleteTransaction(String date) {
        repository.deleteTransaction(date);
    }


    public LiveData<List<EmployeeModel>> getEmployees() {
        return allEmployees;
    }


    public LiveData<List<Transactions>> getTransactions() {
        return allTransactions;
    }


    public void select(EmployeeModel model) {
        Selected = model;
    }


    public EmployeeModel getSelected() {
        return Selected;
    }


    public void addTransaction(Transactions transaction) {
        repository.addTransaction(transaction);
    }


    public LiveData<Boolean> getIsDataLoaded() {
        return isDataLoaded;
    }


    public LiveData<Long> getIsDataEmpty() {
        return isDataEmpty;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsSucceeded() {
        return isSucceeded;
    }



    public void signInUser(String email, String password, final Context context) {
        isLoading.setValue(true);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                isLoading.postValue(false);
                isSucceeded.setValue(task.isSuccessful());
                if (!task.isSuccessful()) {
                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void signUpUser(final User user ,String password, final Context context) {
        isLoading.setValue(true);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                isLoading.postValue(false);
                isSucceeded.setValue(task.isSuccessful());
                if (task.isSuccessful()) {
                    user.setAuthUid(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                    repository.addNewUser(user);
                } else {
                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void OnDataLoaded(boolean val) {
        isDataLoaded.postValue(val);
    }

    @Override
    public void getItemCount(long count) {
        isDataEmpty.postValue(count);
    }
}