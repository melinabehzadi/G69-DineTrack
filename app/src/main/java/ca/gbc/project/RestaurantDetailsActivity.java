package ca.gbc.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

        // Populate the UI
        ((TextView) findViewById(R.id.tv_restaurant_name)).setText(name);
        ((TextView) findViewById(R.id.tv_address)).setText(address);
        ((TextView) findViewById(R.id.tv_phone_number)).setText(phoneNumber);
        ((TextView) findViewById(R.id.tv_description)).setText(description);
        ((TextView) findViewById(R.id.tv_tags)).setText("Tags: " + tags);

        //Back button
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity and return to the previous screen
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

        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_empty);
            }
        }

    }
}
