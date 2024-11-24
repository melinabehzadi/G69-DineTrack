package ca.gbc.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Navigate to Favorites (Restaurant List Activity)
        ImageButton btnFavorites = findViewById(R.id.btn_favorites);
        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RestaurantListActivity.class);
            startActivity(intent);
        });

        // Navigate to About Activity
        ImageButton btnAbout = findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Navigate to Add Restaurant Activity
        ImageButton btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddRestaurantActivity.class);
            startActivity(intent);
        });

        // Handle Search Button
        EditText searchBar = findViewById(R.id.search_bar);
        ImageButton btnSearch = findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            Intent intent = new Intent(HomeActivity.this, RestaurantListActivity.class);
            intent.putExtra("query", query); // Pass the search query to RestaurantListActivity
            startActivity(intent);
        });
    }
}
