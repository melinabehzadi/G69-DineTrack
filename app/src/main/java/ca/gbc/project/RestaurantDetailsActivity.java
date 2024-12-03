package ca.gbc.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        double latitude = getIntent().getDoubleExtra("restaurant_latitude", 0); // New: Latitude
        double longitude = getIntent().getDoubleExtra("restaurant_longitude", 0); // New: Longitude

        // Populate the UI
        ((TextView) findViewById(R.id.tv_restaurant_name)).setText(name);
        ((TextView) findViewById(R.id.tv_address)).setText(address);
        ((TextView) findViewById(R.id.tv_phone_number)).setText(phoneNumber);
        ((TextView) findViewById(R.id.tv_description)).setText(description);
        ((TextView) findViewById(R.id.tv_tags)).setText("Tags: " + tags);

        // Stars
        Button backButton = findViewById(R.id.details_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Star Ratings
        ImageView[] stars = new ImageView[]{
                findViewById(R.id.star1),
                findViewById(R.id.star2),
                findViewById(R.id.star3),
                findViewById(R.id.star4),
                findViewById(R.id.star5)
        };

        // Edit Button
        findViewById(R.id.btn_edit).setOnClickListener(v -> {
            // Show edit pop-up
            showEditPopup();
        });

        // Delete Button
        findViewById(R.id.btn_delete).setOnClickListener(v -> {
            // Show delete confirmation dialog
            showDeleteConfirmationDialog();
        });



        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_empty);


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

            }
        }
    }

    // Function to display delete confirmation dialog
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Restaurant")
                .setMessage("Are you sure you want to delete this restaurant?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the restaurant and close the activity
                    deleteRestaurant();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .show();
    }

    // Function to delete the restaurant
    private void deleteRestaurant() {
        // Logic to delete the restaurant (e.g., remove from database)
        Toast.makeText(this, "Restaurant deleted!", Toast.LENGTH_SHORT).show();
        finish(); // Close the details page
    }

    // Function to display edit pop-up
    private void showEditPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.edit_restaurant_popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        EditText etName = popupView.findViewById(R.id.et_edit_name);
        EditText etAddress = popupView.findViewById(R.id.et_edit_address);
        EditText etPhone = popupView.findViewById(R.id.et_edit_phone);
        EditText etDescription = popupView.findViewById(R.id.et_edit_description);
        EditText etTags = popupView.findViewById(R.id.et_edit_tags);

        // Populate existing data
        etName.setText(getIntent().getStringExtra("restaurant_name"));
        etAddress.setText(getIntent().getStringExtra("restaurant_address"));
        etPhone.setText(getIntent().getStringExtra("restaurant_phone"));
        etDescription.setText(getIntent().getStringExtra("restaurant_description"));
        etTags.setText(getIntent().getStringExtra("restaurant_tags"));

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Save updated restaurant details
            String updatedName = etName.getText().toString().trim();
            String updatedAddress = etAddress.getText().toString().trim();
            String updatedPhone = etPhone.getText().toString().trim();
            String updatedDescription = etDescription.getText().toString().trim();
            String updatedTags = etTags.getText().toString().trim();

            // Update UI and/or database
            ((TextView) findViewById(R.id.tv_restaurant_name)).setText(updatedName);
            ((TextView) findViewById(R.id.tv_address)).setText(updatedAddress);
            ((TextView) findViewById(R.id.tv_phone_number)).setText(updatedPhone);
            ((TextView) findViewById(R.id.tv_description)).setText(updatedDescription);
            ((TextView) findViewById(R.id.tv_tags)).setText("Tags: " + updatedTags);

            Toast.makeText(this, "Restaurant updated!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}