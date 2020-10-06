package com.david.redcristianauno.data.model

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

class Red : Serializable{
    var created_by: DocumentReference? = null
    var id_red: String = ""
    var name_leader: String = ""
}