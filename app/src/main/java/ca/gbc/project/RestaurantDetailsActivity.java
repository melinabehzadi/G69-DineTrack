package ca.gbc.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RestaurantDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        // Get data from intent
        String name = getIntent().getStringExtra("restaurant_name");
        String address = getIntent().getStringExtra("restaurant_address");
        String phoneNumber = getIntent().getStringExtra("restaurant_phone");
        String description = getIntent().getStringExtra("restaurant_description");
        String tags = getIntent().getStringExtra("restaurant_tags");
        int rating = getIntent().getIntExtra("restaurant_rating", 0);
        double latitude = getIntent().getDoubleExtra("restaurant_latitude", 0); // New: Latitude
        double longitude = getIntent().getDoubleExtra("restaurant_longitude", 0); // New: Longitude

        // Populate the UI
        ((TextView) findViewById(R.id.tv_restaurant_name)).setText(name);
        ((TextView) findViewById(R.id.tv_address)).setText(address);
        ((TextView) findViewById(R.id.tv_phone_number)).setText(phoneNumber);
        ((TextView) findViewById(R.id.tv_description)).setText(description);
        ((TextView) findViewById(R.id.tv_tags)).setText("Tags: " + tags);

        // Update the rating stars dynamically
        // Example: Use logic to display filled and empty stars

        // Add a button to view the restaurant on the map
        findViewById(R.id.btn_view_map).setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailsActivity.this, RestaurantMapActivity.class);
            intent.putExtra("latitude", getIntent().getDoubleExtra("restaurant_latitude", 0));
            intent.putExtra("longitude", getIntent().getDoubleExtra("restaurant_longitude", 0));
            intent.putExtra("name", getIntent().getStringExtra("restaurant_name"));
            startActivity(intent);
        });
    }
}
