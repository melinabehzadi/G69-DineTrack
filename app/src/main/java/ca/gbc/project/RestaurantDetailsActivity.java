package ca.gbc.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
        double latitude = getIntent().getDoubleExtra("restaurant_latitude", 0);
        double longitude = getIntent().getDoubleExtra("restaurant_longitude", 0);

        // Initialize back button
        ImageButton backButton = findViewById(R.id.btn_back_to_home);

        // Button listeners
        backButton.setOnClickListener(v -> finish());

        // Populate the UI
        ((TextView) findViewById(R.id.tv_restaurant_name)).setText(name);
        ((TextView) findViewById(R.id.tv_address)).setText(address);
        ((TextView) findViewById(R.id.tv_phone_number)).setText(phoneNumber);
        ((TextView) findViewById(R.id.tv_description)).setText(description);
        ((TextView) findViewById(R.id.tv_tags)).setText("Tags: " + tags);

        // Stars
        ImageView[] stars = new ImageView[]{
                findViewById(R.id.star1),
                findViewById(R.id.star2),
                findViewById(R.id.star3),
                findViewById(R.id.star4),
                findViewById(R.id.star5)
        };

        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_empty);
            }
        }
        // View the restaurant on the map
        findViewById(R.id.btn_view_map).setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailsActivity.this, RestaurantMapActivity.class);
            intent.putExtra("latitude", getIntent().getDoubleExtra("restaurant_latitude", 0));
            intent.putExtra("longitude", getIntent().getDoubleExtra("restaurant_longitude", 0));
            intent.putExtra("name", getIntent().getStringExtra("restaurant_name"));
            startActivity(intent);
        });

        // Get Directions Button
        findViewById(R.id.btn_directions).setOnClickListener(v -> {
            Uri navigationUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Uri browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + latitude + "," + longitude);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                startActivity(browserIntent);
            }
        });

        //Share
        findViewById(R.id.btn_share).setOnClickListener(v -> {
            String shareMessage = "Check out this restaurant!\n\n" +
                    "Name: " + name + "\n" +
                    "Address: " + address + "\n" +
                    "Location: https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // Edit Button
        findViewById(R.id.btn_edit).setOnClickListener(v -> showEditPopup());

        // Delete Button
        findViewById(R.id.btn_delete).setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Restaurant")
                .setMessage("Are you sure you want to delete this restaurant?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the restaurant from the database
                    DBHandler dbHandler = new DBHandler(this);
                    String restaurantName = getIntent().getStringExtra("restaurant_name");
                    dbHandler.deleteRestaurant(restaurantName);

                    // Notify the user and finish the activity
                    Toast.makeText(this, "Restaurant deleted successfully!", Toast.LENGTH_SHORT).show();

                    // Ensure that the list is refreshed in RestaurantListActivity
                    Intent intent = new Intent();
                    intent.putExtra("deleted_restaurant", restaurantName);
                    setResult(RESULT_OK, intent);

                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }



    private void updateRestaurantInDatabase(String name, String address, String phone, String description, String tags, int rating) {
        DBHandler dbHandler = new DBHandler(this);

        // Create a new Restaurant object with updated values
        Restaurant updatedRestaurant = new Restaurant(
                name, address, phone, description, tags, rating,
                getIntent().getDoubleExtra("restaurant_latitude", 0),
                getIntent().getDoubleExtra("restaurant_longitude", 0)
        );

        // Update the restaurant in the database
        dbHandler.updateRestaurant(updatedRestaurant);

        // Notify user
        Toast.makeText(this, "Restaurant updated successfully!", Toast.LENGTH_SHORT).show();

        // Refresh the details screen with data fetched from the database
        refreshDetailsScreen(updatedRestaurant);

        // Update the Intent with the new data
        getIntent().putExtra("restaurant_name", updatedRestaurant.getName());
        getIntent().putExtra("restaurant_address", updatedRestaurant.getAddress());
        getIntent().putExtra("restaurant_phone", updatedRestaurant.getPhone());
        getIntent().putExtra("restaurant_description", updatedRestaurant.getDescription());
        getIntent().putExtra("restaurant_tags", updatedRestaurant.getTags());
        getIntent().putExtra("restaurant_rating", updatedRestaurant.getRating());
    }

    private void refreshDetailsScreen(Restaurant updatedRestaurant) {
        // Update the UI with new data
        ((TextView) findViewById(R.id.tv_restaurant_name)).setText(updatedRestaurant.getName());
        ((TextView) findViewById(R.id.tv_address)).setText(updatedRestaurant.getAddress());
        ((TextView) findViewById(R.id.tv_phone_number)).setText(updatedRestaurant.getPhone());
        ((TextView) findViewById(R.id.tv_description)).setText(updatedRestaurant.getDescription());
        ((TextView) findViewById(R.id.tv_tags)).setText("Tags: " + updatedRestaurant.getTags());

        // Update star ratings
        ImageView[] stars = new ImageView[]{
                findViewById(R.id.star1),
                findViewById(R.id.star2),
                findViewById(R.id.star3),
                findViewById(R.id.star4),
                findViewById(R.id.star5)
        };

        for (int i = 0; i < stars.length; i++) {
            if (i < updatedRestaurant.getRating()) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_empty);
            }
        }
    }

    private void showEditPopup() {
        // Inflate the popup view
        View popupView = getLayoutInflater().inflate(R.layout.edit_restaurant_popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        // Initialize input fields
        EditText etName = popupView.findViewById(R.id.et_edit_name);
        EditText etAddress = popupView.findViewById(R.id.et_edit_address);
        EditText etPhone = popupView.findViewById(R.id.et_edit_phone);
        EditText etDescription = popupView.findViewById(R.id.et_edit_description);
        EditText etTags = popupView.findViewById(R.id.et_edit_tags);
        Spinner spinnerRating = popupView.findViewById(R.id.spinner_rating);

        // Populate fields with current data
        etName.setText(getIntent().getStringExtra("restaurant_name"));
        etAddress.setText(getIntent().getStringExtra("restaurant_address"));
        etPhone.setText(getIntent().getStringExtra("restaurant_phone"));
        etDescription.setText(getIntent().getStringExtra("restaurant_description"));
        etTags.setText(getIntent().getStringExtra("restaurant_tags"));

        // Set rating spinner
        int rating = getIntent().getIntExtra("restaurant_rating", 0);
        spinnerRating.setSelection(rating - 1); // Assuming spinner options are 1-5

        // Handle Save button
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Retrieve updated values
            String updatedName = etName.getText().toString().trim();
            String updatedAddress = etAddress.getText().toString().trim();
            String updatedPhone = etPhone.getText().toString().trim();
            String updatedDescription = etDescription.getText().toString().trim();
            String updatedTags = etTags.getText().toString().trim();
            int updatedRating = spinnerRating.getSelectedItemPosition() + 1;

            // Save to database
            updateRestaurantInDatabase(updatedName, updatedAddress, updatedPhone, updatedDescription, updatedTags, updatedRating);
        });

        // Handle Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}
