package ca.gbc.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "dinetrack.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TAGS = "tags";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_RESTAURANTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_TAGS + " TEXT, " +
                COLUMN_RATING + " INTEGER, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        onCreate(db);
    }

    public void addRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, restaurant.getName());
        values.put(COLUMN_ADDRESS, restaurant.getAddress());
        values.put(COLUMN_PHONE, restaurant.getPhone());
        values.put(COLUMN_DESCRIPTION, restaurant.getDescription());

        // Normalize tags
        String normalizedTags = normalizeTags(restaurant.getTags());
        values.put(COLUMN_TAGS, normalizedTags);

        values.put(COLUMN_RATING, restaurant.getRating());
        values.put(COLUMN_LATITUDE, restaurant.getLatitude());
        values.put(COLUMN_LONGITUDE, restaurant.getLongitude());

        db.insert(TABLE_RESTAURANTS, null, values);
        db.close();
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESTAURANTS, null, null, null, null, null, COLUMN_NAME);
        if (cursor.moveToFirst()) {
            do {
                restaurants.add(new Restaurant(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurants;
    }

    public void updateRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, restaurant.getName());
        values.put(COLUMN_ADDRESS, restaurant.getAddress());
        values.put(COLUMN_PHONE, restaurant.getPhone());
        values.put(COLUMN_DESCRIPTION, restaurant.getDescription());

        // Normalize tags
        String normalizedTags = normalizeTags(restaurant.getTags());
        values.put(COLUMN_TAGS, normalizedTags);

        values.put(COLUMN_RATING, restaurant.getRating());
        values.put(COLUMN_LATITUDE, restaurant.getLatitude());
        values.put(COLUMN_LONGITUDE, restaurant.getLongitude());

        db.update(TABLE_RESTAURANTS, values, COLUMN_NAME + " = ?", new String[]{restaurant.getName()});
        db.close();
    }

    private String normalizeTags(String tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        // Split tags by commas, trim spaces, and convert to lowercase
        String[] tagArray = tags.split(",");
        List<String> normalizedTags = new ArrayList<>();
        for (String tag : tagArray) {
            normalizedTags.add(tag.trim().toLowerCase());
        }

        // Join back into a single string
        return String.join(",", normalizedTags);
    }

    public void deleteRestaurant(String restaurantName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, COLUMN_NAME + " = ?", new String[]{restaurantName});
        db.close();
    }

    public List<Restaurant> searchRestaurants(String query) {
        List<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_NAME + " LIKE ? OR " + COLUMN_TAGS + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(TABLE_RESTAURANTS, null, selection, selectionArgs, null, null, COLUMN_NAME);
        if (cursor.moveToFirst()) {
            do {
                restaurants.add(new Restaurant(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurants;
    }

    public List<Restaurant> filterRestaurants(int minRating, String[] tags) {
        List<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder selection = new StringBuilder(COLUMN_RATING + " >= ?");
        List<String> selectionArgsList = new ArrayList<>();
        selectionArgsList.add(String.valueOf(minRating));

        if (tags != null && tags.length > 0) {
            selection.append(" AND (");
            for (int i = 0; i < tags.length; i++) {
                if (i > 0) selection.append(" OR ");
                selection.append(COLUMN_TAGS).append(" LIKE ?");
                selectionArgsList.add("%" + tags[i] + "%");
            }
            selection.append(")");
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        Cursor cursor = db.query(TABLE_RESTAURANTS, null, selection.toString(), selectionArgs, null, null, COLUMN_NAME);
        if (cursor.moveToFirst()) {
            do {
                restaurants.add(new Restaurant(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurants;
    }

    public List<String> getAllUniqueTags() {
        List<String> tagsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Fetch all tags
        String query = "SELECT DISTINCT " + COLUMN_TAGS + " FROM " + TABLE_RESTAURANTS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Split tags (assuming they're comma-separated)
                String[] tags = cursor.getString(0).split(",");
                for (String tag : tags) {
                    String trimmedTag = tag.trim(); // Remove extra spaces
                    if (!tagsList.contains(trimmedTag)) {
                        tagsList.add(trimmedTag);
                    }
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tagsList;
    }

    public void seedDatabase() {
        List<Restaurant> seedData = SeedRestaurants.getRestaurantSeedData();
        for (Restaurant restaurant : seedData) {
            if (!doesRestaurantExist(restaurant.getName())) {
                addRestaurant(restaurant);
            }
        }
    }

    public boolean doesRestaurantExist(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_RESTAURANTS,
                new String[]{COLUMN_ID},
                COLUMN_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }



}
