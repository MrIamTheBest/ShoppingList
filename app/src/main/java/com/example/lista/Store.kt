package com.example.lista

import com.google.firebase.firestore.GeoPoint
import java.util.HashMap

class Store {
    var storeId: String? =null
    var store: String? =null
    var description: String? =null
    var location: GeoPoint? =null
    var radius: Float? =null
    var promotion: String? =null

    constructor()
    constructor(store: String?, description: String?, location: GeoPoint?, radius: Float?) {
        this.store = store
        this.description = description
        this.location = location
        this.radius = radius
        //this.promotion = promotion
    }
    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result["store"] = store!!
        result["description"] = description!!
        result["location"] = location!!
        result["radius"] = radius!!
        

        return result
    }


}