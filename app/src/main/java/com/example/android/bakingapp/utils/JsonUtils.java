package com.example.android.bakingapp.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

// These utilities will be used to communicate with the network.
// JsonUtils.java gets the json data from https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
public class JsonUtils {

    /**
     * Builds the URL used to get json data from.
     *
     * @return The URL used to get json data from
     */
    public static URL buildBakingAppUrl() {
        Uri builtUri = Uri.parse(Constants.BAKING_APP_JSON_DATA_URL);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            // \A stands for : start of a string!
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}