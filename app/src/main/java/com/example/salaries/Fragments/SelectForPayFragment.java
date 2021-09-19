package com.example.salaries.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.salaries.Adapters.employeeAdapter;
import com.example.salaries.Models.EmployeeModel;
import com.example.salaries.R;
import com.example.salaries.Repositories.SharedRepository;
import com.example.salaries.ViewModels.SharedViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

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


public class SelectForPayFragment extends Fragment {
    RecyclerView recyclerView;
    SharedViewModel sharedViewModel;
    employeeAdapter adapter;
    NavController navController;
    RelativeLayout noEmployeeLayout;
    ShimmerFrameLayout shimmerFrameLayout;

    public SelectForPayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_for_pay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.employees_recycler_view);
        noEmployeeLayout = view.findViewById(R.id.empty_employee_layout_2);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_effect_layout);
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.init(SharedRepository.EMPLOYEES_PATH);
        sharedViewModel.getEmployees().observe(getViewLifecycleOwner(), new Observer<List<EmployeeModel>>() {
            @Override
            public void onChanged(List<EmployeeModel> employeeModels) {
                adapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();
        checkDataIsEmptyObserver();
        checkDataIsLoadedObserver();
        navigate();
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new employeeAdapter();
        recyclerView.setAdapter(adapter);
    }


    private void checkDataIsEmptyObserver() {
        sharedViewModel.getIsDataEmpty().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if (aLong == 0) {
                    hideShimmer();
                    recyclerView.setVisibility(View.GONE);
                    noEmployeeLayout.setVisibility(View.VISIBLE);
                } else {
                    noEmployeeLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    private void checkDataIsLoadedObserver() {
        sharedViewModel.getIsDataLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    adapter.setList(sharedViewModel.getEmployees().getValue());
                    hideShimmer();
                } else {
                    showShimmer();
                }
            }
        });
    }

    private void showShimmer() {
        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    private void hideShimmer() {
        recyclerView.setVisibility(View.VISIBLE);
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
    }

    private void navigate() {
        adapter.setOnItemClickListener(new employeeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(EmployeeModel model) {
                sharedViewModel.select(model);
                navController.navigate(R.id.action_selectForPayFragment_to_payFragment);
            }
        });
    }
}