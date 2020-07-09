package com.david.redcristianauno.data.model

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

class User: Serializable {
    var id = ""
    var names = ""
    var last_names = ""
    var email = ""
    var address = ""
    var telephone = ""
    var permission = ""
    var subred_name = ""
    var iglesia_references: DocumentReference? = null
}