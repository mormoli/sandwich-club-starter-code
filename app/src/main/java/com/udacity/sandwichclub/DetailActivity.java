package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    //initializing textviews in activty_detail.xml
    TextView origin_tv, description_tv, ingredients_tv, also_known_tv;
    ImageView ingredientsIv;

    @SuppressWarnings("ConstantConditions") //may produce null on method !
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ingredientsIv = findViewById(R.id.image_iv);
        origin_tv = findViewById(R.id.origin_tv);
        description_tv = findViewById(R.id.description_tv);
        ingredients_tv = findViewById(R.id.ingredients_tv);
        also_known_tv = findViewById(R.id.also_known_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        } else {
            populateUI(sandwich);
            setTitle(sandwich.getMainName());
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        //Log.d("DetailActivity: ", sandwich.getImage());
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        if (!sandwich.getPlaceOfOrigin().isEmpty()) origin_tv.setText(sandwich.getPlaceOfOrigin());
        else origin_tv.setText(R.string.detail_error_message);

        if (!sandwich.getDescription().isEmpty()) description_tv.setText(sandwich.getDescription());
        else description_tv.setText(R.string.detail_error_message);

        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0)
            for (String strAlsoKnownAs : sandwich.getAlsoKnownAs()) also_known_tv.append(strAlsoKnownAs + "\n");
        else also_known_tv.setText(R.string.detail_error_message);

        if (sandwich.getIngredients() != null && sandwich.getIngredients().size() > 0)
            for (String strIngredients : sandwich.getIngredients()) ingredients_tv.append(strIngredients + "\n");
        else ingredients_tv.setText(R.string.detail_error_message);
    }
}
