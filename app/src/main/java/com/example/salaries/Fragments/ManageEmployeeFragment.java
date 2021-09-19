package com.example.salaries.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.salaries.Adapters.UpdateDeleteAdapter;
import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.R;
import com.example.salaries.Repositories.SharedRepository;
import com.example.salaries.ViewModels.SharedViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ManageEmployeeFragment extends Fragment {

    RecyclerView recyclerView;
    SharedViewModel sharedViewModel;
    UpdateDeleteAdapter adapter;
    NavController navController;
    RelativeLayout emptyEmployeeLayout;
    ShimmerFrameLayout shimmerFrameLayout;
    FloatingActionButton fabAddEmployee;

    public ManageEmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_manage_employees);
        emptyEmployeeLayout = view.findViewById(R.id.empty_employee_layout);
        fabAddEmployee = view.findViewById(R.id.fab_add_employee);
        navController = Navigation.findNavController(view);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_effect_layout);


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.init(SharedRepository.EMPLOYEES_PATH);
        sharedViewModel.getEmployees().observe(getViewLifecycleOwner(), new Observer<List<EmployeeModel>>() {
            @Override
            public void onChanged(List<EmployeeModel> employeeModels) {
                adapter.submitList(employeeModels);
                adapter.notifyDataSetChanged();
            }
        });

        fabAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_manageEmployeeFragment_to_employeeFormFragment);
            }
        });

        sharedViewModel.getIsDataLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    adapter.submitList(sharedViewModel.getEmployees().getValue());
                    hideShimmerEffect();
                } else {
                    showShimmerEffect();
                }
            }
        });

        sharedViewModel.getIsDataEmpty().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if (aLong == 0) {
                    hideShimmerEffect();
                    emptyEmployeeLayout.setVisibility(View.VISIBLE);
                } else {
                    emptyEmployeeLayout.setVisibility(View.GONE);
                }
            }
        });

        initRecyclerView();
        OnClickUpdateDetails();
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new UpdateDeleteAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void showShimmerEffect() {
        recyclerView.setVisibility(View.GONE);
        fabAddEmployee.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    private void hideShimmerEffect() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        recyclerView.setVisibility(View.VISIBLE);
        fabAddEmployee.setVisibility(View.VISIBLE);
    }


    private void OnClickUpdateDetails() {
        adapter.setOnItemClickListener(new UpdateDeleteAdapter.OnItemClickListener() {
            @Override
            public void Update(final EmployeeModel model) {

                String[] items = {"Update Employee Details", "Delete Employee"};
                new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                    .setTitle("Choose an Action")
                    .setBackground(getResources().getDrawable(R.drawable.bg, null))
                    .setIcon(R.drawable.ic_list)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0 :
                                    sharedViewModel.select(model);
                                    navController.navigate(R.id.action_manageEmployeeFragment_to_employeeFormFragment);
                                    break;
                                case 1:
                                    sharedViewModel.deleteEmployee(model.getEmployId());
                                    break;
                            }
                        }
                    })
                    .show();
            }
        });
    }
}