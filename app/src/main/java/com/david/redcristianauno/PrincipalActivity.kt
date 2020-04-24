package com.david.redcristianauno

import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.david.redcristianauno.Firestore.InsertarDatos
import com.david.redcristianauno.Firestore.LeerDatos
import com.david.redcristianauno.Historico.HistoricoDiarioFragment
import com.david.redcristianauno.POJOs.HistoricoSemanal
import com.david.redcristianauno.POJOs.HistoricoSemanalSubred
import com.david.redcristianauno.POJOs.RegistroCelula
import com.david.redcristianauno.POJOs.RegistroSubred

import com.david.redcristianauno.ui.activities.LoginActivity
import com.david.redcristianauno.ui.fragments.BiblieFragment
import com.david.redcristianauno.ui.fragments.CreateConfigurationDialogFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.text.DateFormat
import java.util.*

class PrincipalActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener, OnDateSetListener {
    private var tv_nombre_usuario: TextView? = null
    private var tv_correo_usuario: TextView? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private val listaSemanal: List<String> =
        ArrayList()
    private var navigationView: NavigationView? = null
    private val menu: Menu? = null
    var resvar = 0
    var maxid: Long = 0
    var maxidSubred: Long = 0
    private val l = LeerDatos()
    private val inda = InsertarDatos()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_principal)
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)



        val nombreview = navigationView?.getHeaderView(0)
        val correoview = navigationView?.getHeaderView(0)
        tv_nombre_usuario =
            nombreview?.findViewById<View>(R.id.tv_nombre_usuario) as TextView
        tv_correo_usuario = correoview?.findViewById<View>(R.id.tv_correo) as TextView
        navigationView?.setNavigationItemSelectedListener(this)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.contenedor, MenuFragment()).commit()
        inicializarFirebase()
        val c = Calendar.getInstance()
        c[Calendar.DAY_OF_MONTH]
        c[Calendar.MONTH]
        c[Calendar.YEAR]
        val fecha =
            DateFormat.getDateInstance(DateFormat.FULL).format(c.time)

        //checar(fecha);
        inda.leerHistorico(fecha)
        correo_usuario =
            Preferences.obtenerPreferencesString(
                this,
                Preferences.PREFERENCES_USUARIO_LOGIN
            )
        tv_correo_usuario!!.text = correo_usuario
        l.datosSesion(
            tv_nombre_usuario,
            correo_usuario,
            menu,
            navigationView,
            this@PrincipalActivity
        )

        //ocultardatos();
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.salida) {
            AlertDialog.Builder(this@PrincipalActivity)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de cerrar la sesión?")
                .setPositiveButton("Aceptar") { dialog, which ->
                    Preferences.savePreferenceBoolean(
                        this@PrincipalActivity,
                        false,
                        Preferences.PREFENCE_ESTADO_BUTTON_SESION
                    )
                    val i = Intent(this@PrincipalActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
                .setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val fragmentManager = supportFragmentManager
        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, MenuFragment()).commit()
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, RegistroSubredFragment())
                .commit()
            resvar = 1
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, RegistroCelulaFragment())
                .commit()
            resvar = 2
        } else if (id == R.id.nav_tools) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, HistoricoFragment())
                .commit()
            resvar = 3
        } else if (id == R.id.nav_biblia) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,
                BiblieFragment()
            ).commit()
        } else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, PermisosUsuariosFragment())
                .commit()
        } else if (id == R.id.crear) {
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, CreateConfigurationDialogFragment()).commit()
        } else if (id == R.id.noticias) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, noticiasFragment()).commit()
        }

        return true
    }

    fun inicializarFirebase() {
        FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference
    }

    private fun checar(fecha: String) {
        databaseReference!!.child("Historico").orderByChild("fecha")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                var bandera = true
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val rc = snapshot.getValue(
                            HistoricoSemanal::class.java
                        )
                        val fecha_celula = rc!!.fecha
                        println("Fecha historico:$fecha_celula")
                        if (fecha == fecha_celula) {
                            bandera = false
                        }
                        println("Bandera: $bandera")
                    }
                    if (bandera == true) {
                        formatoSemanal()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun formatoSemanal() {
        val miCalendario = Calendar.getInstance()
        miCalendario.firstDayOfWeek = Calendar.TUESDAY
        var bandera = false
        val diaSemana = miCalendario[Calendar.DAY_OF_WEEK]
        bandera = if (diaSemana != miCalendario.firstDayOfWeek) {
            false
        } else {
            true
        }
        if (bandera == true) {
            val dia_del_mes = miCalendario[Calendar.DAY_OF_MONTH]
            println("El dia es: $dia_del_mes")
            var mes = miCalendario[Calendar.MONTH] + 1
            println("El mes es: $mes")
            val año = miCalendario[Calendar.YEAR]
            println("El año es: $año")
            var nuevo = dia_del_mes
            val fechas = arrayOfNulls<String>(7)
            for (i in 0..6) {
                if (nuevo == 0) {
                    nuevo = 1
                }
                nuevo -= 1
                when (mes) {
                    1 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 31 - nuevo
                    }
                    2 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 31 - nuevo
                    }
                    3 -> if (año % 4 == 0 && (año % 100 != 0 || año % 400 == 0)) {
                        if (nuevo <= 0) {
                            mes = mes - 1
                            nuevo = 29 - nuevo
                        }
                    } else {
                        if (nuevo <= 0) {
                            mes = mes - 1
                            nuevo = 28 - nuevo
                        }
                    }
                    4 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 31 - nuevo
                    }
                    5 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 30 - nuevo
                    }
                    6 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 31 - nuevo
                    }
                    7 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 30 - nuevo
                    }
                    8 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 31 - nuevo
                    }
                    9 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 31 - nuevo
                    }
                    10 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 30 - nuevo
                    }
                    11 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 31 - nuevo
                    }
                    12 -> if (nuevo <= 0) {
                        mes = mes - 1
                        nuevo = 30 - nuevo
                    }
                }
                miCalendario[Calendar.DAY_OF_MONTH] = nuevo
                miCalendario[Calendar.MONTH] = mes - 1
                miCalendario[Calendar.YEAR] = año
                val fecha =
                    DateFormat.getDateInstance(DateFormat.FULL)
                        .format(miCalendario.time)
                println(fecha)
                fechas[i] = fecha
            }
            crearFormato(fechas)
            crearFormatoSubred(fechas)
        }
    }

    private fun crearFormatoSubred(fechas: Array<String?>) {
        databaseReference!!.child("Registro Subred")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var total_asistencia = 0
                    var total_ofrenda = 0.0
                    for (snapshot in dataSnapshot.children) {
                        val rc =
                            snapshot.getValue(RegistroSubred::class.java)
                        val fecha_celula = rc!!.fecha_subred
                        for (i in fechas.indices) {
                            if (fechas[i] == fecha_celula) {
                                total_asistencia = total_asistencia + rc.asistencia_subred
                                total_ofrenda = total_ofrenda + rc.ofrenda_subred
                            }
                        }
                    }
                    totalSemanalSubred(total_asistencia, total_ofrenda)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun totalSemanalSubred(
        total_asistencia: Int,
        total_ofrenda: Double
    ) {
        databaseReference!!.child("Historico Subred")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        maxidSubred = dataSnapshot.childrenCount
                        val c = Calendar.getInstance()
                        c[Calendar.DAY_OF_MONTH]
                        c[Calendar.MONTH]
                        c[Calendar.YEAR]
                        val fecha =
                            DateFormat.getDateInstance(DateFormat.FULL)
                                .format(c.time)
                        val hs = HistoricoSemanalSubred()
                        hs.id_historico = (maxidSubred + 1).toString()
                        hs.total_asistencia = total_asistencia
                        hs.total_ofrenda = total_ofrenda
                        hs.fecha = fecha
                        databaseReference!!.child("Historico Subred").child(hs.id_historico)
                            .setValue(hs)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun crearFormato(fecha: Array<String?>) {
        databaseReference!!.child("Registro Celula")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var total_asistencia = 0
                    var total_invitados = 0
                    var total_ninos = 0
                    var total_ofrenda = 0.0
                    for (snapshot in dataSnapshot.children) {
                        val rc =
                            snapshot.getValue(RegistroCelula::class.java)
                        val fecha_celula = rc!!.fecha_celula
                        for (i in fecha.indices) {
                            if (fecha[i] == fecha_celula) {
                                total_asistencia = total_asistencia + rc.asistencia_celula
                                total_invitados = total_invitados + rc.invitados_celula
                                total_ninos = total_ninos + rc.ninos_celula
                                total_ofrenda = total_ofrenda + rc.ofrenda_celula
                            }
                        }
                    }
                    totalSemanal(total_asistencia, total_invitados, total_ninos, total_ofrenda)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun totalSemanal(
        total_asistencia: Int,
        total_invitados: Int,
        total_ninos: Int,
        total_ofrenda: Double
    ) {
        databaseReference!!.child("Historico")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        maxid = dataSnapshot.childrenCount
                        Toast.makeText(this@PrincipalActivity, maxid.toString(), Toast.LENGTH_SHORT)
                            .show()
                        val c = Calendar.getInstance()
                        c[Calendar.DAY_OF_MONTH]
                        c[Calendar.MONTH]
                        c[Calendar.YEAR]
                        val fecha =
                            DateFormat.getDateInstance(DateFormat.FULL)
                                .format(c.time)
                        val hs3 = HistoricoSemanal()
                        hs3.id_historico = (maxid + 1).toString()
                        hs3.total_asistencia = total_asistencia
                        hs3.total_invitados = total_invitados
                        hs3.total_ninos = total_ninos
                        hs3.total_ofrenda = total_ofrenda
                        hs3.fecha = fecha
                        databaseReference!!.child("Historico").child(hs3.id_historico)
                            .setValue(hs3)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun onDateSet(
        view: DatePicker,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        val bundle = Bundle()
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        c[Calendar.DAY_OF_MONTH] = dayOfMonth
        val currentDateString =
            DateFormat.getDateInstance(DateFormat.FULL).format(c.time)
        c.firstDayOfWeek = Calendar.TUESDAY
        val c2 = Calendar.getInstance()
        c2.firstDayOfWeek = Calendar.TUESDAY
        val numero_semana_original = c2[Calendar.WEEK_OF_YEAR]
        val numero_semana = c[Calendar.WEEK_OF_YEAR]
        println("El numero de la semana es: $numero_semana")
        println("El numero de la semana de hoy es: $numero_semana_original")
        if (numero_semana_original == numero_semana) {
            println(currentDateString)
            if (resvar == 1) {
                RegistroSubredFragment.setFecha(currentDateString)
            }
            if (resvar == 2) {
                RegistroCelulaFragment.setFecha(currentDateString)
            }
        } else {
            Toast.makeText(
                this@PrincipalActivity,
                "La fecha de la semana no coincide con el de hoy",
                Toast.LENGTH_LONG
            ).show()
        }
        if (resvar == 3) {
            HistoricoDiarioFragment.setFecha(currentDateString)
        }
    }

    companion object {
        var asistencia = 0
        var invitados = 0
        var ninos = 0
        var ofrenda = 0.0
        var correo_usuario = ""
        var res = ""
    }
}