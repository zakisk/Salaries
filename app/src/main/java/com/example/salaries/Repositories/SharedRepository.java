package com.example.salaries.Repositories;

import android.util.Log;

import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.Models.Transactions;
import com.example.salaries.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class SharedRepository {
    public static String EMPLOYEES_PATH = "Employees/";

    public static String TRANSACTIONS_PATH = "Transactions/";
    public static final String USERS_PATH = "Users";

    public static SharedRepository instance;

    private ArrayList<EmployeeModel> employees = new ArrayList<>();

    private ArrayList<Transactions> transactions = new ArrayList<>();

    private MutableLiveData<Boolean> isLoading;

    private OnDataLoadedListener dataLoadedListener;

    private DatabaseReference employeesRef;

    private DatabaseReference transactionsRef;

    /**
     * SharedRepository#getInstance
     *
     * Singleton Pattern
     * @return an Instance of this Class
     */
    public static SharedRepository getInstance() {
        if (instance == null) {
            instance = new SharedRepository();
        }
        return instance;
    }

    /**
     * SharedRepository#getAllEmployees
     *
     * @param query Firebase Database String query
     * @return MutableLiveData Object containing all employees objects
     */
    public MutableLiveData<List<EmployeeModel>> getAllEmployees(String query) {
        employeesRef = FirebaseDatabase.getInstance()
                .getReference(EMPLOYEES_PATH + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        fetchData(query);
        MutableLiveData<List<EmployeeModel>> data = new MutableLiveData<>();
        data.setValue(employees);
        return data;
    }

    /**
     * SharedRepository#getAllTransactions
     *
     * @param query Firebase Database String query
     * @return MutableLiveData Object containing all transactions objects
     */
    public MutableLiveData<List<Transactions>> getAllTransactions(String query) {
        transactionsRef = FirebaseDatabase.getInstance()
                .getReference(TRANSACTIONS_PATH + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        fetchData(query);
        MutableLiveData<List<Transactions>> data = new MutableLiveData<>();
        data.setValue(transactions);
        return data;
    }

    /**
     * SharedRepository#updateAddEmployeeDetails
     *
     * @param model Employee model object containing updated or new employee information
     */
    public void updateAddEmployeeDetails(EmployeeModel model) {
        employeesRef = FirebaseDatabase.getInstance()
                .getReference(EMPLOYEES_PATH + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        if (model.getEmployId() != null) {
            employeesRef.child(model.getEmployId()).setValue(model);
        } else {
            String Id = employeesRef.push().getKey();
            model.setEmployId(Id);
            assert Id != null;
            employeesRef.child(Id).setValue(model);
        }
    }

    /**
     * SharedRepository#deleteEmployee
     *
     * @param employeeId Employee's Unique Id to Delete
     * @return true of Success else false
     */
    public void deleteEmployee(String employeeId) {
        employeesRef.child(employeeId).removeValue();
    }

    /**
     * SharedRepository#addTransaction
     *
     * @param transaction Transactions object containing transaction details i.e date, amount etc
     */
    public void addTransaction(Transactions transaction) {
        transactionsRef = FirebaseDatabase.getInstance()
                .getReference(TRANSACTIONS_PATH + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        transactionsRef.child(transaction.getDate()).setValue(transaction);
    }


    public void deleteTransaction(String date) {
        transactionsRef.child(date).removeValue();
    }

    /**
     * SharedRepository#addNewUser
     *
     * Adding new User by Firebase Email and Password Sign in Method
     * @param user The Company/Shop Owner Credential Variable Passed by ViewModel#SignInUser Method
     */

    public void addNewUser(@NotNull User user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(USERS_PATH);
        reference.child(user.getAuthUid()).setValue(user);
    }


    private void fetchData(final String query) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(query
                + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("onDataChange", "onDataChange Function Executed");
                if (snapshot.exists()) {

                    dataLoadedListener.getItemCount(snapshot.getChildrenCount());
                    dataLoadedListener.OnDataLoaded(false);
                    if (query.equals(EMPLOYEES_PATH)) {
                        employees.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            EmployeeModel model = ds.getValue(EmployeeModel.class);
                            employees.add(model);
                        }

                    } else if (query.equals(TRANSACTIONS_PATH)) {
                        transactions.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Transactions transaction = ds.getValue(Transactions.class);
                            transactions.add(transaction);
                        }
                    }
                }

                if (employees.size() == snapshot.getChildrenCount() ||
                        transactions.size() == snapshot.getChildrenCount()) {
                    dataLoadedListener.OnDataLoaded(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SharedRepo#setData", error.getMessage());
            }
        };

        reference.addValueEventListener(listener);
    }


    public interface OnDataLoadedListener {
        void OnDataLoaded(boolean val);
        void getItemCount(long count);
    }

    public void setOnDataLoadedListener(OnDataLoadedListener listener) {
        this.dataLoadedListener = listener;
    }
}