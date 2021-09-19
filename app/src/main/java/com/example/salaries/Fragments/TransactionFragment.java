package com.example.salaries.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.salaries.Adapters.TransactionAdapter;
import com.example.salaries.Models.Transactions;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionFragment extends Fragment {

    RecyclerView recyclerView;
    SharedViewModel sharedViewModel;
    TransactionAdapter adapter;
    RelativeLayout emptyTransactionLayout;
    ShimmerFrameLayout shimmerFrameLayout;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.transaction_recycler_view);
        emptyTransactionLayout = view.findViewById(R.id.empty_transaction_layout);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_effect_layout);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.init(SharedRepository.TRANSACTIONS_PATH);

        sharedViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {
                adapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();
        checkDataIsEmptyObserver();
        checkDataIsLoadedObserver();
        setItemTouchHelper();
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
    }


    private void checkDataIsEmptyObserver() {
        sharedViewModel.getIsDataEmpty().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if (aLong == 0) {
                    hideShimmerEffect();
                    emptyTransactionLayout.setVisibility(View.VISIBLE);
                } else {
                    emptyTransactionLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkDataIsLoadedObserver() {
        sharedViewModel.getIsDataLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    hideShimmerEffect();
                    adapter.submitList(sharedViewModel.getTransactions().getValue());
                } else {
                    showShimmerEffect();
                }
            }
        });
    }

    private void showShimmerEffect() {
        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    private void hideShimmerEffect() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                sharedViewModel.deleteTransaction(
                        adapter.getDateOfTransactionAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
    }
}