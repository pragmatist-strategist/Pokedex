package com.example.pranavsharma.lec_13;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PRANAV SHARMA on 3/19/2018.
 */

public class MainActivity extends AppCompatActivity {
    String PokemonResult = " ";
    TextView PokemonName;
    ImageView PokemonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btnGO);
        final EditText editText = findViewById(R.id.editTextName);
        PokemonName = findViewById(R.id.PokemonName);
        PokemonImage = findViewById(R.id.PokemonImage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseurl = "http://pokeapi.co/api/v2/pokemon/";
                String input = editText.getText().toString();
                String url = baseurl + input;
                makeNetworkCall(url);

            }
        });
    }

    private void makeNetworkCall(String url) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "ON FAILURE: " + call.request().url());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                PokemonResult = response.body().string();
                Log.e("TAG", "ON RESPONSE: " + PokemonResult);

                Gson gson = new Gson();
                final Pokemon pokemon = gson.fromJson(PokemonResult, Pokemon.class);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PokemonName.setText(pokemon.getName());
                        Picasso.with(getBaseContext())
                                .load(pokemon.getSprites().getBack_default())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher_round)
                                .into(PokemonImage);
                    }
                });
            }
        });
    }
}
