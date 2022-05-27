package org.ieselcaminas.proyectofinal.model.recyclerViewNews

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.proyectofinal.R
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors


class RecyclerViewAdapterNews(private val items: ArrayList<New>): RecyclerView.Adapter<RecyclerViewAdapterNews.ViewHolderNews>()  {

    class ViewHolderNews(val view: View) : RecyclerView.ViewHolder(view) {

        private val container: CardView = view.findViewById(R.id.newCardContainer)
        private val title: TextView = view.findViewById(R.id.textTitleNew)
        private val description: TextView = view.findViewById(R.id.textViewDescription)
        private val date: TextView = view.findViewById(R.id.textViewNewDate)
        private val image: ImageView = view.findViewById(R.id.imageViewNew)

        fun bindItem(item: New) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var imageSRC: Bitmap?
            val placeholder = getDrawable(view.context,R.drawable.no_image)
            executor.execute {
                try {
                    val inImage: InputStream = URL(item.imageURL).openStream()
                    imageSRC = BitmapFactory.decodeStream(inImage)
                    handler.post {
                        if (imageSRC == null){
                            image.setImageDrawable(placeholder)
                        } else {
                            image.setImageBitmap(imageSRC)
                        }
                    }
                } catch (e: Exception) {}
            }
            title.text = item.title
            description.text = item.description
            date.text = item.date.substring(0,item.date.length-7)
            container.setOnClickListener {
                val uri = Uri.parse(item.link)
                val i = Intent(Intent.ACTION_VIEW,uri)
                view.context.startActivity(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNews {
        return ViewHolderNews(LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderNews, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}