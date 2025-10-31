package com.sap.codelab.utils.extensions

import com.google.android.gms.maps.model.LatLng


fun LatLng.isEmpty(): Boolean   {
    return ((this.latitude==0.0) && (this.longitude==0.0))
}