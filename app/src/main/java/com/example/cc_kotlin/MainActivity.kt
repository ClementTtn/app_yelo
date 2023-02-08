package com.example.cc_kotlin
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val latch = CountDownLatch(1)
    private var body :JSONObject? = null;

    // Appel de l'API
    fun run(){
        val request = Request.Builder().url("https://api.agglo-larochelle.fr/production/opendata/api/records/1.0/search/dataset=yelo___disponibilite_des_velos_en_libre_service&facet=station_nom&api-key=").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                latch.countDown()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    body = JSONObject(response.body!!.string())
                }
                else {
                    println("Request failed with code ${response.code}")                   // Code erreur
                }
                latch.countDown()
            }
        })
        latch.await()
        // Appel du traitement de l'API
        this.API()
    }

    // Traitement de l'API
    private fun API() {
        if (body != null) {
            val records = body!!.getJSONArray("records")                                                                // Récupère la partie records dans l'API
            val stationListe: ArrayList<station> = ArrayList<station>()                                                       // Liste des stations
            for (i in 0 until records.length()) {                                                                       // Exécute la boucle jusqu'au bout de records
                val stationJson = records.getJSONObject(i)                                                                    // Création d'un nouvel object
                val stationId = stationJson.getInt("recordid")                                                          // Id de la station
                var name = stationJson.getJSONObject("fields").getString("station_nom")                           // Nom de la station
                name = name.substring(2,name.length)                                                                          // Substring pour enlever le chiffre au début du nom de la station
                val lon = stationJson.getJSONObject("fields").getDouble("station_longitude")                     // Longitude de la station
                val lat = stationJson.getJSONObject("fields").getDouble("station_latitude")                      // Latitude de la station
                val nb_emplacements = stationJson.getJSONObject("fields").getInt("nombre_emplacements")          // Nombre d'emplacements au total dans la station
                val nb_place_libre = stationJson.getJSONObject("fields").getInt("accroches_libres")              // Nombre de places disponibles dans la station
                val station = station(stationId, name, lon, lat, nb_emplacements, nb_place_libre)                            // Création d'une station
                stationListe.add(station)                                                                                    // Ajout de cette station à l'ArrayList
            }
            // Récupératon du ListView
            val listView = findViewById<ListView>(R.id.listView);
            // Conversion des données
            listView.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, stationListe)
            // Ajout du Click Listener pour un élément du listView
            listView.setOnItemClickListener{ parent, _, position, _ ->
                // Appel vers la MapsActivity
                val intent = Intent(this, MapsActivity::class.java).apply{
                    putExtra("station", stationListe[position])
                }
                // Lancement du intent pour obtenir les données vers MapsActivity
                startActivity(intent)
            }
        }
    }

    // Lancement du programme
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.listView);
        val button = findViewById<Button>(R.id.stations_bouton);
        button.setOnClickListener{
            this.run()
        }
    }
}