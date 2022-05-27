package org.ieselcaminas.proyectofinal.model.recyclerViewNotes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.proyectofinal.R
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapter(private val items: ArrayList<Item>, private val languageTag: String): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderNotes>()  {
    lateinit var onClick : (View) -> Unit

    class ViewHolderNotes(val view: View, private val languageTag: String) : RecyclerView.ViewHolder(view) {

        private val button: Button = view.findViewById(R.id.doc_recview)

        @SuppressLint("SimpleDateFormat")
        fun bindItem(item: Item, onClick: (View) -> Unit) {
            if (item.date != "No Data") {
                val cal = GregorianCalendar.getInstance()
                cal.time = Date(item.date.toLong())
                cal.add(Calendar.DAY_OF_MONTH,90)
                button.text = SimpleDateFormat("EEEE,  dd MMMM, yyyy", Locale.forLanguageTag(languageTag)).format(cal.time)
                button.setOnClickListener { onClick(itemView) }
            } else {
                button.text = item.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotes {
        return ViewHolderNotes(LayoutInflater.from(parent.context).inflate(R.layout.item_recview,parent,false),languageTag)
    }

    override fun onBindViewHolder(holder: ViewHolderNotes, position: Int) {
        holder.bindItem(items[position], onClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}