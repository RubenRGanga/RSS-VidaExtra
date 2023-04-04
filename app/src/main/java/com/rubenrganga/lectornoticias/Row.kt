package com.rubenrganga.lectornoticias

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class Row : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_row)
    }
}

data class Item(var title: String, var date: String, var link: String)

class ItemAdapter(context: Context, objects: List<Item>) :
    ArrayAdapter<Item>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.activity_row, parent, false)
        }

        val item = getItem(position)

        val nombreTextView = itemView?.findViewById<TextView>(R.id.tvTitulo)
        nombreTextView?.text = item?.title

        /*

        Intenté añadir el nombre del autor al lector RSS pero esa propiedad
        aparece en el Item como "dc:creator" y aun repitiendo el código de las
        propiedades title, pubDate, o link no fui capaz de añadirlo, creo que por
        no usar una sintaxis correcta en este caso, intente crear una variable
        llamada Dc_creator como se muestra abajo y utilizar el nombre de la variable
        pero tampoco encontré así la manera, dejo el código comentado sobre si al verlo
        le importaría decirme la solución adecuada.

        var Dc_creator = String.format("dc:{0}","creator");

        val autorTextView = itemView?.findViewById<TextView>(R.id.tvAutor)
        autorTextView?.text = item?.Dc_creator

         */

        val fechaTextView = itemView?.findViewById<TextView>(R.id.tvFecha)
        fechaTextView?.text = item?.date

        val enlaceTextView = itemView?.findViewById<TextView>(R.id.tvEnlace)
        enlaceTextView?.text = item?.link

        return itemView!!
    }
}