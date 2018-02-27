package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "Sandwich";
    //JSON data is parsed correctly to a Sandwich object in JsonUtils *required
    //JSON is parsed without using 3rd party libraries *required
    //initializing string fields in order arrayName : { mainName - alsoKnownAs - placeOfOrigin - description - image - indregents }
    private static final String ARRAY_NAME = "name";
    private static final String MAIN_NAME = "mainName";
    private static final String ALSO_KNOWN_AS = "alsoKnownAs"; // list
    private static final String PLACE_OF_ORIGINS = "placeOfOrigins";
    private static final String SANDWICH_DESCRIPTION = "description";
    private static final String SANDWICH_IMAGE = "image";
    private static final String SANDWICH_INGREDIENTS = "ingredients"; // list
    /**
     * Sample data from strings.xml file
     *
     * {\"name\":{\"mainName\":\"Ham and cheese
     * sandwich\",\"alsoKnownAs\":[]},\"placeOfOrigin\":\"\",\"description\":\"A ham and cheese
     * sandwich is a common type of sandwich. It is made by putting cheese and sliced ham
     * between two slices of bread. The bread is sometimes buttered and/or toasted. Vegetables
     * like lettuce, tomato, onion or pickle slices can also be included. Various kinds of
     * mustard and mayonnaise are also
     * common.\",\"image\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Grilled_ham_and_cheese_014.JPG/800px-Grilled_ham_and_cheese_014.JPG\",
     * \"ingredients\":[\"Sliced
     * bread\",\"Cheese\",\"Ham\"]}
     * */
    //required readings : https://developer.android.com/reference/org/json/JSONObject.html - JSONArray.html
    public static Sandwich parseSandwichJson(String json) {
        List<String> alsoKnownAs = null, ingredients = null;
        String strMainName = "", strPlaceOfOringins = "", strDescription = "", strImage = "";
        JSONObject mainName = null, placeOfOrigins = null, description = null, image = null;
        JSONArray alsoKnownJson, ingredientsJson;
        try{
            json = json.replace("\\", "");
            JSONObject jsonObject = new JSONObject(json);
            Log.d(TAG, jsonObject.getJSONObject(ARRAY_NAME).toString());
            Log.d(TAG, json);
            for(Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                String keyName = iter.next();
                Log.d(TAG, keyName);
                switch (keyName) {
                    case ARRAY_NAME:
                        strMainName = jsonObject.getString(MAIN_NAME);
                        break;
                    case ALSO_KNOWN_AS://https://stackoverflow.com/questions/6697147/json-iterate-through-jsonarray
                        alsoKnownJson = jsonObject.getJSONArray(ALSO_KNOWN_AS);
                        alsoKnownAs = new ArrayList<>();
                        if (alsoKnownJson != null && alsoKnownJson.length() > 1){//not null and also has elements greater than 1
                            for (int i = 0; i < alsoKnownJson.length(); i++) {
                                JSONObject objects = alsoKnownJson.getJSONObject(i);
                                Iterator key = objects.keys();
                                while (key.hasNext()) {
                                    String k = key.next().toString();//key name
                                    String strValue = objects.getString(k);// value
                                    alsoKnownAs.add(strValue);
                                }
                            }
                        } else if(alsoKnownJson != null && alsoKnownJson.length() == 1){//not null and at least 1 element
                            alsoKnownAs.add(alsoKnownJson.getString(0));
                        } else {//array is empty
                            alsoKnownAs.add(String.valueOf(R.string.detail_error_message));
                        }
                        break;
                    case PLACE_OF_ORIGINS:
                        strPlaceOfOringins = jsonObject.getString(PLACE_OF_ORIGINS);
                        break;
                    case SANDWICH_DESCRIPTION:
                        strDescription = jsonObject.getString(SANDWICH_DESCRIPTION);
                        break;
                    case SANDWICH_IMAGE:
                        strImage = jsonObject.getString(SANDWICH_IMAGE);
                        break;
                    case SANDWICH_INGREDIENTS:
                        ingredientsJson = jsonObject.getJSONArray(SANDWICH_INGREDIENTS);
                        ingredients = new ArrayList<>();
                        if (ingredientsJson != null && ingredientsJson.length() > 1){//not null and also has elements greater than 1
                            for (int i = 0; i < ingredientsJson.length(); i++) {
                                JSONObject objects = ingredientsJson.getJSONObject(i);
                                Iterator key = objects.keys();
                                while (key.hasNext()) {
                                    String k = key.next().toString();//key name
                                    String strValue = objects.getString(k);// value
                                    ingredients.add(strValue);
                                }
                            }
                        } else if(ingredientsJson != null && ingredientsJson.length() == 1){//not null and at least 1 element
                            ingredients.add(ingredientsJson.getString(0));
                        } else {//array is empty
                            ingredients.add(String.valueOf(R.string.detail_error_message));
                        }
                        break;
                    default:
                        //Log.d(TAG, "default: " + keyName);
                        break;
                }//end switch
            }//end for loop

            //null check string values
            if(strMainName == null || strMainName.isEmpty()) strMainName = String.valueOf(R.string.detail_error_message);
            if(strPlaceOfOringins == null || strPlaceOfOringins.isEmpty()) strPlaceOfOringins = String.valueOf(R.string.detail_error_message);
            if(strDescription == null || strDescription.isEmpty()) strDescription = String.valueOf(R.string.detail_error_message);
            if(strImage == null || strImage.isEmpty()) strImage = "https://en.wikipedia.org/wiki/List_of_HTTP_status_codes#/media/File:404_error_sample.png";

        } catch (JSONException e) {
            e.getLocalizedMessage();
        }

        return new Sandwich(strMainName, alsoKnownAs, strPlaceOfOringins, strDescription, strImage, ingredients);
    }
}
