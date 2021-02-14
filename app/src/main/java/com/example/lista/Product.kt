package com.example.lista

import java.util.HashMap

class Product {
    var id: String? =null
    var  product: String? =null
    var cena: String? =null
    var ilosc: String? =null
    var zakup: Boolean = false

    constructor(){}

    constructor(product: String?, cena: String?, ilosc: String?, zakup: Boolean) {
        this.product = product
        this.cena = cena
        this.ilosc = ilosc
        this.zakup = zakup
    }

    constructor(id: String?, product: String?, cena: String?, ilosc: String?, zakup: Boolean) {
        this.id = id
        this.product = product
        this.cena = cena
        this.ilosc = ilosc
        this.zakup = zakup
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result["product"] = product!!
        result["cena"] = cena!!
        result["ilosc"] = ilosc!!
        result["zakup"] = zakup!!

        return result
    }
}