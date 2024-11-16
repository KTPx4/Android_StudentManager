package com.example.essay.services;

import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SvgLoader {

    public static void loadSvgFromUrl(String urlString, ImageView imageView) {
        new AsyncTask<Void, Void, PictureDrawable>() {
            @Override
            protected PictureDrawable doInBackground(Void... voids) {
                try {
                    // Tải SVG từ URL
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();

                    // Parse SVG
                    SVG svg = SVG.getFromInputStream(inputStream);
                    return new PictureDrawable(svg.renderToPicture());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(PictureDrawable drawable) {
                if (drawable != null) {
                    // Hiển thị SVG trong ImageView
                    imageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null); // Tắt hardware acceleration
                    imageView.setImageDrawable(drawable);
                }
            }
        }.execute();
    }
}
