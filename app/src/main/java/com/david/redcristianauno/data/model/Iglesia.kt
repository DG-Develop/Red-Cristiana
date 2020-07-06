package com.david.redcristianauno.data.model

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

class Iglesia : Serializable{
    var created_by: DocumentReference? = null
    var id_iglesia: String = ""
    var name: String = ""
}