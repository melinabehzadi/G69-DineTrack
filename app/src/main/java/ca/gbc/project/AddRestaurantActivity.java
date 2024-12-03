package ca.gbc.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddRestaurantActivity extends AppCompatActivity {

    private EditText etRestaurantName, etAddress, etPhoneNumber, etDescription, etTags;
    private ImageView[] stars;
    private int selectedRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        // Initialize fields
        etRestaurantName = findViewById(R.id.et_restaurant_name);
        etAddress = findViewById(R.id.et_address);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etDescription = findViewById(R.id.et_description);
        etTags = findViewById(R.id.et_tags);

        // Initialize rating stars
        stars = new ImageView[5];
        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        for (int i = 0; i < stars.length; i++) {
            final int index = i;
            stars[i].setOnClickListener(v -> updateRating(index + 1));
        }

        // Cancel button
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            // Return to previous activity
            finish();
        });

        // Save button
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            // Validate inputs and save restaurant
            saveRestaurant();
        });
    }

    private void updateRating(int rating) {
        selectedRating = rating;
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_empty);
            }
        }
    }

    private void saveRestaurant() {
        String name = etRestaurantName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String tags = etTags.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || description.isEmpty() || tags.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return;
        }

        Restaurant restaurant = new Restaurant(name, address, phone, description, tags, selectedRating, 0, 0);
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addRestaurant(restaurant);

        Toast.makeText(this, "Restaurant saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

}
