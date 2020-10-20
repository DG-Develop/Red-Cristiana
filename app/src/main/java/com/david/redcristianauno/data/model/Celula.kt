package com.david.redcristianauno.data.model

import com.google.firebase.firestore.DocumentReference

class Celula {
    var created_by: DocumentReference? = null
    var id_celula: String = ""
    var name_leader: String = ""
    var users : MutableList<DocumentReference> = mutableListOf()
}