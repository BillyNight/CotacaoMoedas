package com.example.cotacaomoedas;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cotacaomoedas.databinding.ActivityMainBinding;
import com.example.cotacaomoedas.databinding.FragmentFirstBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensorProximity;
    Sensor sensorLuminosity;

    FragmentManager fragmentManager;
    FirstFragment firstFragment = new FirstFragment();

    UserLocalStore userLocalStore;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private int apiFlag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorLuminosity = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        userLocalStore = new UserLocalStore(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(authenticate() == true){
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigateUp();
            navController.navigate(R.id.FirstFragment);
        }
    }


    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
    }

    public void requestAPI() {

        final Spinner spinner = findViewById(R.id.spinner);
        String baseUrl = "https://economia.awesomeapi.com.br/last/";
        long option = spinner.getSelectedItemId();
        String postUrl = new String();
        String pathJson = new String();
        System.out.println(option);

        if(option == 0){
            postUrl = "USD-BRL";
            pathJson = "USDBRL";
        } else if (option == 1){
            postUrl = "EUR-BRL";
            pathJson = "EURBRL";
        } else if (option == 2){
            postUrl = "CAD-BRL";
            pathJson = "CADBRL";
        } else if (option == 3){
            postUrl = "JPY-BRL";
            pathJson = "JPYBRL";
        } else if (option == 4){
            postUrl = "CNY-BRL";
            pathJson = "CNYBRL";
        }

        baseUrl = baseUrl+postUrl;

        String finalPathJson = pathJson;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(baseUrl, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.getJSONObject(finalPathJson));
                    Double actual = response.getJSONObject(finalPathJson).getDouble("bid");
                    Double high = response.getJSONObject(finalPathJson).getDouble("high");
                    Double low = response.getJSONObject(finalPathJson).getDouble("low");
                    System.out.println(actual);
                    final TextView textActual = findViewById(R.id.actual_brl);
                    final TextView textHigh = findViewById(R.id.max_brl);
                    final TextView textLow = findViewById(R.id.min_brl);
                    textActual.setText("R$ "+actual);
                    textHigh.setText("R$ "+high);
                    textLow.setText("R$ "+low);

                } catch (JSONException e) {
                    e.printStackTrace();

                } finally {
                    apiFlag = 1;
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                final TextView textActual = findViewById(R.id.actual_brl);
                textActual.setText("error ");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(jsonObjectRequest);


    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorLuminosity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int valor = Math.round(sensorEvent.values[0]);
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
                //final TextView debug = findViewById(R.id.text_debug);
                //debug.setText('a');
                System.out.println(Math.round(sensorEvent.values[0]));
                if(valor == 0 && apiFlag == 1){
                    apiFlag=0;
                    requestAPI();
                }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}