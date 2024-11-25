package ca.gbc.project;

public class Restaurant {
    private String name;
    private String address;
    private String phone;
    private String description;
    private String tags;
    private int rating;
    private double latitude;
    private double longitude;

    public Restaurant(String name, String address, String phone, String description, String tags, int rating, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.tags = tags;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public int getRating() {
        return rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
