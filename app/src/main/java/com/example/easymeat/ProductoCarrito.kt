package com.example.easymeat

import android.os.Parcel
import android.os.Parcelable

data class ProductoCarrito(

    // de momento tenemos variables publicas, pero deben hacerse privadas
    public val name: String?,
    public val id:String,
    public val state:String,
    public val cost:Double,
    public val type:String,
    public val tienda:String,
    public var cantidad: Int,


)
