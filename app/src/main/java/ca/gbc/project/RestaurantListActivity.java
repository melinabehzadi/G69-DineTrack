package ca.gbc.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
        restaurantList.add(new Restaurant(
                "Pizza Place",
                "1111 Yonge St, 2P5 8K6",
                "+1 (123) 456-7890",
                "The best pizza in town, made with fresh ingredients.",
                "Italian, Fast Food",
                5
        ));
        restaurantList.add(new Restaurant(
                "Sushi Spot",
                "2222 Bloor St, 3X8 1Y2",
                "+1 (321) 654-0987",
                "Authentic Japanese sushi and sashimi prepared by master chefs.",
                "Japanese, Seafood",
                4
        ));
        restaurantList.add(new Restaurant(
                "Burger Joint",
                "3333 King St, 5Z9 3L4",
                "+1 (213) 456-1234",
                "Juicy burgers with gourmet toppings and hand-cut fries.",
                "Fast Food, American",
                3
        ));
        restaurantList.add(new Restaurant(
                "Vegan Cafe",
                "4444 Queen St, 7T1 2V3",
                "+1 (987) 654-3210",
                "Plant-based meals with organic and locally-sourced ingredients.",
                "Vegan, Healthy",
                4
        ));
        restaurantList.add(new Restaurant(
                "Taco Paradise",
                "5555 Dundas St, 4Y7 6R5",
                "+1 (111) 222-3333",
                "Delicious tacos with authentic Mexican flavors.",
                "Mexican, Fast Food",
                5
        ));
        restaurantList.add(new Restaurant(
                "Pasta Heaven",
                "6666 College St, 8T3 7R8",
                "+1 (444) 555-6666",
                "Home-made pasta dishes with rich Italian sauces.",
                "Italian, Fine Dining",
                4
        ));
        restaurantList.add(new Restaurant(
                "BBQ Shack",
                "7777 Main St, 9L1 8M3",
                "+1 (555) 666-7777",
                "Slow-cooked barbecue ribs and smoked brisket.",
                "Barbecue, American",
                4
        ));
        restaurantList.add(new Restaurant(
                "Dim Sum Delight",
                "8888 Chinatown Ave, 6Y5 4R3",
                "+1 (666) 777-8888",
                "Authentic Chinese dim sum served fresh daily.",
                "Chinese, Dim Sum",
                5
        ));
        restaurantList.add(new Restaurant(
                "Seafood Bay",
                "9999 Ocean Blvd, 5X6 2T1",
                "+1 (777) 888-9999",
                "Fresh seafood with ocean views and signature cocktails.",
                "Seafood, Fine Dining",
                5
        ));
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
