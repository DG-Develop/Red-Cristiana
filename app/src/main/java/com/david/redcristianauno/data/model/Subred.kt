package com.david.redcristianauno.data.model

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

class Subred: Serializable{
    var name_leader = ""
    var celulas = mutableListOf<String>()
    var id_subred = ""
    var created_by: DocumentReference? = null
    var max_celula = 0
}