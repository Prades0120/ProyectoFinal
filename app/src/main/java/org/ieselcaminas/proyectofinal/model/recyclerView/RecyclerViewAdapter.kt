package org.ieselcaminas.proyectofinal.model.recyclerView

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.ui.CreatePage
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapter(private val items: ArrayList<Item>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>()  {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val button: Button = view.findViewById(R.id.doc_recview)

        @SuppressLint("SimpleDateFormat")
        fun bindItem(item: Item) {
            if (item.date != "No Data") {
                val cal = GregorianCalendar.getInstance()
                cal.time = Date(item.date.toLong())
                cal.add(Calendar.DAY_OF_MONTH,90)
                button.text = SimpleDateFormat("EEEE,  dd MMMM, yyyy", Locale.UK).format(cal.time)
                button.setOnClickListener {
                    val i = Intent(view.context, CreatePage::class.java)
                    i.putExtra("text", item.text)
                    view.context.startActivity(i)
                }
            } else {
                button.text = item.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recview,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}