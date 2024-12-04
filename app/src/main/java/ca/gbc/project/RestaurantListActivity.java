package ca.gbc.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;
    private List<Restaurant> filteredList;

    private EditText etSearch;
    private Button btnSearch, btnSort, btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        // Back button logic
        ImageButton btnBackToHome = findViewById(R.id.btn_back_to_home);
        btnBackToHome.setOnClickListener(v -> finish());

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rv_restaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize UI components
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        btnSort = findViewById(R.id.btn_sort);
        btnFilter = findViewById(R.id.btn_filter);

        // Initialize the data lists
        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView
        adapter = new RestaurantAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        // Populate the restaurant list
        populateRestaurantList();

        // Handle incoming search query from HomeActivity
        handleIncomingQuery();

        // Set button listeners
        btnSearch.setOnClickListener(v -> handleSearch());
        btnSort.setOnClickListener(v -> handleSort());
        btnFilter.setOnClickListener(v -> loadTags());
    }



    private void handleIncomingQuery() {
        String query = getIntent().getStringExtra("query");
        if (query != null && !query.isEmpty()) {
            etSearch.setText(query);
            filteredList.clear();

            for (Restaurant restaurant : restaurantList) {
                if (restaurant.getName().toLowerCase().contains(query.toLowerCase()) ||
                        restaurant.getTags().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(restaurant);
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void populateRestaurantList() {
        DBHandler dbHandler = new DBHandler(this);
        restaurantList = dbHandler.getAllRestaurants();
        filteredList.clear();
        filteredList.addAll(restaurantList);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            Log.e("RestaurantListActivity", "Adapter is null when attempting to update the RecyclerView.");
        }
    }


    private void handleSearch() {
        String query = etSearch.getText().toString().trim().toLowerCase();

        if (!query.isEmpty()) {
            DBHandler dbHandler = new DBHandler(this);
            filteredList = dbHandler.searchRestaurants(query);
            adapter = new RestaurantAdapter(filteredList, this);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFilterWithParams(String[] tags) {
        int minRating = 0; // Optionally include a rating filter
        DBHandler dbHandler = new DBHandler(this);
        filteredList = dbHandler.filterRestaurants(minRating, tags);

        // Update the adapter
        adapter = new RestaurantAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);
    }

    private void showFilterDialog(List<String> tags) {
        // Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        // Find the LinearLayout where checkboxes will be added
        LinearLayout tagsContainer = dialogView.findViewById(R.id.tags_container);

        // Dynamically add checkboxes for each tag
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(tag);
            checkBox.setPadding(8, 8, 8, 8);
            tagsContainer.addView(checkBox);
            checkBoxes.add(checkBox);
        }

        // Set Positive and Negative Buttons
        builder.setPositiveButton("Apply", (dialog, which) -> {
            List<String> selectedTags = new ArrayList<>();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    selectedTags.add(checkBox.getText().toString());
                }
            }

            // Pass selected tags to filter handler
            handleFilterWithParams(selectedTags.toArray(new String[0]));
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Create and customize the AlertDialog
        AlertDialog dialog = builder.create();

        // Customize dialog dimensions and appearance
        dialog.setOnShowListener(d -> {
            // Dynamically set dimensions: Width = 80%, Height = 60%
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
            dialog.getWindow().setLayout(width, height);
        });

        // Show the dialog
        dialog.show();
    }

    private void loadTags() {
        DBHandler dbHandler = new DBHandler(this);
        List<String> tags = dbHandler.getAllUniqueTags();

        // Example: Print tags to verify
        for (String tag : tags) {
            Log.d("TAG", "Available Tag: " + tag);
        }

        // Pass the tags to your filter UI
        showFilterDialog(tags);
    }

    private void handleSort() {
        Collections.sort(filteredList, Comparator.comparing(Restaurant::getName));
        adapter.notifyDataSetChanged();
    }

    private void handleFilter() {
        DBHandler dbHandler = new DBHandler(this);

        int minRating = 4;
        String[] tags = {"Italian", "Fast Food"};

        filteredList = dbHandler.filterRestaurants(minRating, tags);
        adapter = new RestaurantAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRestaurantList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String deletedRestaurant = data.getStringExtra("deleted_restaurant");
            if (deletedRestaurant != null) {
                populateRestaurantList();
            }
        }
    }



}
