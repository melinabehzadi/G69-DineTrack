package ca.gbc.project;

import java.util.ArrayList;
import java.util.List;

public class SeedRestaurants {

    public static List<Restaurant> getRestaurantSeedData() {
        List<Restaurant> restaurants = new ArrayList<>();

        // Add Toronto restaurants
        restaurants.add(new Restaurant(
                "CN Tower 360 Restaurant",
                "301 Front St W, Toronto, ON M5V 2T6",
                "416-362-5411",
                "Fine dining with panoramic views from Toronto's iconic CN Tower.",
                "fine dining,landmark,view",
                5,
                43.642566,
                -79.387057
        ));

        restaurants.add(new Restaurant(
                "St. Lawrence Market Kitchen",
                "93 Front St E, Toronto, ON M5E 1C3",
                "416-392-7120",
                "Cooking classes and meals in Toronto's historic market.",
                "casual,market,cooking class",
                4,
                43.648948,
                -79.371697
        ));

        restaurants.add(new Restaurant(
                "Terroni",
                "57 Adelaide St E, Toronto, ON M5C 1K6",
                "416-504-1992",
                "Rustic Italian fare focusing on authentic ingredients.",
                "Italian,pizza,pasta",
                4,
                43.650570,
                -79.375405
        ));

        restaurants.add(new Restaurant(
                "Terroni Queen",
                "720 Queen St W, Toronto, ON M6J 1E8",
                "416-504-0320",
                "Authentic Italian cuisine in a vibrant atmosphere.",
                "Italian,casual,pizza",
                4,
                43.647763,
                -79.409443
        ));

        restaurants.add(new Restaurant(
                "Planta Yorkville",
                "1221 Bay St, Toronto, ON M5R 3P5",
                "647-348-7000",
                "Upscale vegan restaurant with creative dishes.",
                "vegan,fine dining,plant-based",
                5,
                43.670905,
                -79.389690
        ));

        restaurants.add(new Restaurant(
                "Planta Queen",
                "180 Queen St W, Toronto, ON M5V 3X3",
                "647-812-1221",
                "Asian-inspired vegan cuisine in a chic setting.",
                "vegan,Asian,fusion",
                4,
                43.650300,
                -79.389976
        ));

        restaurants.add(new Restaurant(
                "Khao San Road",
                "11 Charlotte St, Toronto, ON M5V 2H5",
                "647-352-5773",
                "Authentic Thai dishes with bold flavors.",
                "Thai,spicy,casual",
                5,
                43.646548,
                -79.392575
        ));

        restaurants.add(new Restaurant(
                "Pai Northern Thai Kitchen",
                "18 Duncan St, Toronto, ON M5H 3G8",
                "416-901-4724",
                "Cozy Thai eatery specializing in northern Thai cuisine.",
                "Thai,cozy,spicy",
                5,
                43.647693,
                -79.388320
        ));

        restaurants.add(new Restaurant(
                "Pizzeria Libretto",
                "221 Ossington Ave, Toronto, ON M6J 2Z8",
                "416-532-8000",
                "Neapolitan-style pizzas served in a warm setting.",
                "pizza,Italian,casual",
                4,
                43.650566,
                -79.418151
        ));

        restaurants.add(new Restaurant(
                "Buca",
                "604 King St W, Toronto, ON M5V 1M6",
                "416-865-1600",
                "High-end Italian spot with innovative pasta dishes.",
                "Italian,fine dining,pasta",
                5,
                43.644220,
                -79.401011
        ));

        return restaurants;
    }
}
