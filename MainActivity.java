

/*
Creado por IT PRO
Fines educativos.
Alfredo Suárez Romero

*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ArrayList<String> elementos =new ArrayList<>();
            
			//esta lista se deberia llenar con los resultados del WS
			elementos.add("Hoy-Soleado 25/88");
            elementos.add("Ma�ana-Soleado 25/88");
            elementos.add("Jueves-soleado");

            ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),R.layout.list_item_forecast_layout,R.id.list_item_forecast_textview,elementos);
            ListView lista=(ListView)getActivity().findViewById(R.id.listview_forecast);
            lista.setAdapter(adapter);
            return peticionWS(inflater,container,savedInstanceState);
        }


        public View peticionWS(LayoutInflater inflater,ViewGroup container,
                               Bundle savedInstanceState){

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //Este Objeto es el usado y recomendado por google para hacer peticiones http, WS
            //ya que es mas ligero, tiene mas seguridad y esta echo para aplicaciones moviles.
            ///HttpURLConnection

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
				
				////PARA OBTENER TTU APPID  visita el siguiente link
				////http://openweathermap.org/appid#get
				///Sustituyela en YOURAPPID en la siguiente linea
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=YOURAPPID");

                // Creamos un request
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // La respuesta esta vacia
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return rootView;

        }/// fin metodo peticion web service


    }

}
