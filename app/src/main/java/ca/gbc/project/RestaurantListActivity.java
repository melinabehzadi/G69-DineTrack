package ca.gbc.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
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

        // Initialize the data
        restaurantList = new ArrayList<>();
        populateRestaurantList();
        filteredList = new ArrayList<>(restaurantList);

        // Set Adapter
        adapter = new RestaurantAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        // Handle incoming search query from HomeActivity
        handleIncomingQuery();

        // Set Listeners
        btnSearch.setOnClickListener(v -> handleSearch());
        btnSort.setOnClickListener(v -> handleSort());
        btnFilter.setOnClickListener(v -> handleFilter());
    }

    private void handleIncomingQuery() {
        String query = getIntent().getStringExtra("query");
        if (query != null && !query.isEmpty()) {
            etSearch.setText(query); // Optional: Display the query in the search bar
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
        // Add hardcoded sample restaurants with coordinates
        restaurantList.add(new Restaurant("Pizza Place", "1111 Yonge St, 2P5 8K6", "+1 (123) 456-7890", "Best pizza in town.", "Italian, Fast Food", 5, 43.678827, -79.389233));
        restaurantList.add(new Restaurant("Sushi Spot", "2222 Bloor St, 3X8 1Y2", "+1 (321) 654-0987", "Authentic sushi and sashimi.", "Japanese, Seafood", 4, 43.665407, -79.451332));
        restaurantList.add(new Restaurant("Burger Joint", "3333 King St, 5Z9 3L4", "+1 (213) 456-1234", "Juicy burgers and fries.", "Fast Food, American", 3, 43.641384, -79.395644));
        restaurantList.add(new Restaurant("Vegan Cafe", "4444 Queen St, 7T1 2V3", "+1 (987) 654-3210", "Plant-based meals.", "Vegan, Healthy", 4, 43.653908, -79.377184));
        restaurantList.add(new Restaurant("Taco Paradise", "5555 Dundas St, 4Y7 6R5", "+1 (111) 222-3333", "Authentic tacos.", "Mexican, Fast Food", 5, 43.646547, -79.452939));
        restaurantList.add(new Restaurant("Pasta Heaven", "6666 College St, 8T3 7R8", "+1 (444) 555-6666", "Homemade pasta.", "Italian, Fine Dining", 4, 43.662631, -79.395445));
        restaurantList.add(new Restaurant("BBQ Shack", "7777 Main St, 9L1 8M3", "+1 (555) 666-7777", "BBQ ribs and brisket.", "Barbecue, American", 4, 43.704304, -79.420903));
        restaurantList.add(new Restaurant("Dim Sum Delight", "8888 Chinatown Ave, 6Y5 4R3", "+1 (666) 777-8888", "Chinese dim sum.", "Chinese, Dim Sum", 5, 43.853947, -79.361051));
        restaurantList.add(new Restaurant("Seafood Bay", "9999 Ocean Blvd, 5X6 2T1", "+1 (777) 888-9999", "Fresh seafood.", "Seafood, Fine Dining", 5, 43.647174, -79.374295));
    }

    private void handleSearch() {
        String query = etSearch.getText().toString().toLowerCase();
        filteredList.clear();

        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getName().toLowerCase().contains(query) ||
                    restaurant.getTags().toLowerCase().contains(query)) {
                filteredList.add(restaurant);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void handleSort() {
        Collections.sort(filteredList, Comparator.comparing(Restaurant::getName));
        adapter.notifyDataSetChanged();
    }

    private void handleFilter() {
        filteredList.clear();

        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getRating() >= 4) { // Example: Filter only restaurants with ratings >= 4
                filteredList.add(restaurant);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
