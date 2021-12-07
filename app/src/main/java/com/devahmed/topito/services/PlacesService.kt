package com.devahmed.topito.services

import android.util.Log
import com.devahmed.topito.models.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

object PlacesService {

    private const val TAG = "FirebasePlacesService"
    private const val COLLECTION_NAME = "Places"

    suspend fun getAllPlaces(): ArrayList<Place> {
        val db = FirebaseFirestore.getInstance()
        val PlacesList = ArrayList<Place>()
        db.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    PlacesList.add(document.toObject<Place>())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }.await()
        return PlacesList
    }

    suspend fun getPlaceById(PlaceId :String): Place?{
        val db = FirebaseFirestore.getInstance()
        var Place: Place? = null
        val docRef = db.collection(COLLECTION_NAME).document(PlaceId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    Place = document.toObject<Place>()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }.await()
        return Place
    }

    suspend fun addPlace(Place: Place){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(Place.id)
            .set(Place)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deletePlace(PlaceId:String){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(PlaceId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    suspend fun updatePlace(Place: Place){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(Place.id)
            .set(Place)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }
}