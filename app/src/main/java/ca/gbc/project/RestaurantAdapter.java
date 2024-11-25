package ca.gbc.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        holder.detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantDetailsActivity.class);
            intent.putExtra("restaurant_name", restaurant.getName());
            intent.putExtra("restaurant_address", restaurant.getAddress());
            intent.putExtra("restaurant_phone", restaurant.getPhone());
            intent.putExtra("restaurant_description", restaurant.getDescription());
            intent.putExtra("restaurant_tags", restaurant.getTags());
            intent.putExtra("restaurant_rating", restaurant.getRating());
            intent.putExtra("restaurant_latitude", restaurant.getLatitude()); // Add latitude
            intent.putExtra("restaurant_longitude", restaurant.getLongitude()); // Add longitude
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_restaurant_name);
            tags = itemView.findViewById(R.id.tv_tags);
            detailsButton = itemView.findViewById(R.id.btn_details);
        }
    }
}
