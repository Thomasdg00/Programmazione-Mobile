package com.example.progetto.data.model

class User(
    var id: String,
    var name: String, var email: String,
    var password: String){
    init {
        println ("nuovo utente creato: $name")
        if (!email.contains("@")){
            println ("email non valida")
        }
    }
}