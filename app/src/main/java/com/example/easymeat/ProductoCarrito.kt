package com.example.easymeat

data class ProductoCarrito(

    // de momento tenemos variables publicas, pero deben hacerse privadas
    public val name:String,
    public val id:String,
    public val state:String,
    public val cost:Double,
    public val tienda:String,
    public var cantidad: Int
)
