package com.digitalhouse.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticiaFragment extends Fragment{

    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Noticia> noticias = new ArrayList<>();
    private RecyclerView recyclerViewNoticias;

    public NoticiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticia, container, false);

        recyclerViewNoticias = view.findViewById(R.id.recyclerViewNoticias);

        new GetNoticias().execute();

        return view;
    }



    @SuppressLint("StaticFieldLeak")
    private class GetNoticias extends AsyncTask<Void, Void, ArrayList<Noticia>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<Noticia> doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting responseç
            String url = "\n" +
                    "https://newsapi.org/v2/top-headlines?country=ar&apiKey=8b141017cf6848908829489044ed6f71";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray noticiasJSON = jsonObj.getJSONArray("articles");

                    // looping through All Entidades
                    for (Integer i = 0; i < noticiasJSON.length(); i++) {
                        JSONObject e = noticiasJSON.getJSONObject(i);
                        String autor = e.getString("author");
                        String titulo = e.getString("title");
                        String descripcion = e.getString("description");
                        String urlNoticia = e.getString("url");
                        String urlImage = e.getString("urlToImage");
                        if(urlImage.startsWith("//")){
                            urlImage = "http:"+urlImage;
                        }
                        String publicado = e.getString("publishedAt");
                        String contenido = e.getString("content");

                        Noticia noticia = new Noticia(autor,titulo,descripcion,urlNoticia,urlImage,publicado,contenido);

                        noticias.add(noticia);
                    }
                } catch (final JSONException e) {

                }

            } else {

            }

            return noticias;
        }

        @Override
        protected void onPostExecute(ArrayList<Noticia> result) {
            super.onPostExecute(result);

            traerDatos();
        }
    }

    public void traerDatos(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerViewNoticias.setHasFixedSize(true);

        NoticiasAdaptador adaptador = new NoticiasAdaptador(noticias);
        recyclerViewNoticias.setAdapter(adaptador);
        recyclerViewNoticias.setLayoutManager(layoutManager);
    }

}
