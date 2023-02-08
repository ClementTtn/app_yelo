package com.example.cc_kotlin

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import okhttp3.*

//  Classe station qui contient les infos provenant de l'API
class station (private val recordID: Int): Parcelable{
    private var body :JSONObject? = null;
    private var name: String? = null
    private var lon: Double = 0.0
    private var lat: Double = 0.0
    private var nb_emplacements: Int = 0
    private var nb_place_libre: Int = 0

    // Constructeur
    constructor(recordID: Int,name: String, lon: Double, lat: Double, nb_emplacements: Int, nb_place_libre: Int): this(recordID){
        this.name = name
        this.lon = lon
        this.lat = lat
        this.nb_emplacements = nb_emplacements
        this.nb_place_libre = nb_place_libre
    }

    // Getters / Setters
    fun setName(name: String?){
        this.name = name
    }

    fun getName(): String?{
        return name
    }

    fun setLongitude(lon: Double){
        this.lon = lon
    }

    fun getLongitude(): Double{
        return lon
    }

    fun setLatitude(lat: Double){
        this.lat = lat
    }

    fun getLatitude(): Double{
        return lat
    }

    fun setNbEmplacements(nb_emplacements: Int){
        this.nb_emplacements = nb_emplacements
    }

    fun getNbEmplacements(): Int{
        return nb_emplacements
    }

    fun setNbPlaceLibre(nb_place_libre: Int){
        this.nb_place_libre = nb_place_libre
    }

    fun getNbPlaceLibre(): Int{
        return nb_place_libre
    }

    fun getStationRecordId(): Int{
        return recordID
    }

    fun getStationBody(): JSONObject?{
        return body
    }

    // ArrayList qui stocke toutes les stations qui proviennent de l'API
    private val stationListe : ArrayList<station> = ArrayList<station>()


    // Constructeur Parcelable
    constructor(parcel: Parcel) : this(parcel.readInt()) {
        name = parcel.readString()
        lon = parcel.readDouble()
        lat = parcel.readDouble()
        nb_emplacements = parcel.readInt()
        nb_place_libre = parcel.readInt()
    }

    // toString qui s'affiche sur la vue principale (détail d'une station)
    override fun toString(): String{
        return "$name \n $nb_place_libre place(s) libre(s) sur $nb_emplacements"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(recordID)
        parcel.writeString(name)
        parcel.writeDouble(lon)
        parcel.writeDouble(lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<station> {
        override fun createFromParcel(parcel: Parcel): station {
            return station(parcel)
        }

        override fun newArray(size: Int): Array<station?> {
            return arrayOfNulls(size)
        }
    }
}