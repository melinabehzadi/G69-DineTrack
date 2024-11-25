package ca.gbc.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private final List<Restaurant> restaurantList;
    private final Context context;

    public RestaurantAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        holder.name.setText(restaurant.getName());
        holder.tags.setText(restaurant.getTags());

        int rating = restaurant.getRating();
        ImageView[] stars = new ImageView[]{holder.star1, holder.star2, holder.star3, holder.star4, holder.star5};

        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled); // Use your filled star drawable
            } else {
                stars[i].setImageResource(R.drawable.ic_star_empty); // Use your empty star drawable
            }
        }

        holder.detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantDetailsActivity.class);
            intent.putExtra("restaurant_name", restaurant.getName());
            intent.putExtra("restaurant_address", restaurant.getAddress());
            intent.putExtra("restaurant_phone", restaurant.getPhone());
            intent.putExtra("restaurant_description", restaurant.getDescription());
            intent.putExtra("restaurant_tags", restaurant.getTags());
            intent.putExtra("restaurant_rating", restaurant.getRating());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, tags;
        Button detailsButton;
        ImageView star1, star2, star3, star4, star5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_restaurant_name);
            tags = itemView.findViewById(R.id.tv_tags);
            detailsButton = itemView.findViewById(R.id.btn_details);

            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }
}
