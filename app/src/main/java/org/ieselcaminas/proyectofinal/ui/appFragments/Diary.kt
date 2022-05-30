package org.ieselcaminas.proyectofinal.ui.appFragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentDiaryBinding
import org.ieselcaminas.proyectofinal.model.jsonFormater.JsonParser
import org.ieselcaminas.proyectofinal.model.recyclerViewNotes.Item
import org.ieselcaminas.proyectofinal.model.recyclerViewNotes.RecyclerViewAdapter
import org.ieselcaminas.proyectofinal.ui.CreatePage
import org.ieselcaminas.proyectofinal.ui.LoadingDialog


class Diary : Fragment() {

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser!!
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var array: ArrayList<Item>
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        pieChart = binding.piechart
        val loading = activity?.let { LoadingDialog(it) }!!
        loading.startLoading()
        db.collection("docs").document(user.email!!).get()
            .addOnCompleteListener { task ->
                val mutableList: Map<String,Any>
                if (task.isSuccessful) {
                    mutableList = task.result.data.orEmpty()
                    val newArray = ArrayList<Item>(0)
                    for (i in mutableList.keys) {
                        newArray.add(Item(mutableList[i].toString(),i))
                    }
                    newArray.sortByDescending { it.date }
                    array = newArray
                    recViewConstruction()
                    db.collection("sentiments").document(user.email!!).get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val jsonArray = ArrayList<String>()
                                val data = it.result.data
                                if (data!=null && data.isNotEmpty())
                                    for (i in data.keys) {
                                        jsonArray.add(data[i].toString())
                                        initPieChart()
                                        setDataToPieChart(JsonParser.parseEmotions(jsonArray))
                                    }
                                loading.dismissDialog()
                            } else {
                                loading.dismissDialog()
                            }
                        }
                } else {
                    loading.dismissDialog()
                }
            }
        super.onResume()
    }

    private fun recViewConstruction() {
        val recView = binding.recView
        recView.setHasFixedSize(true)
        val adapter = RecyclerViewAdapter(array,getString(R.string.languaje_tag))
        adapter.onClick = {
            val date = array[recView.getChildAdapterPosition(it)].date
            val text = array[recView.getChildAdapterPosition(it)].text

            if (date!="No Data") {
                val newIntent = Intent(context, CreatePage::class.java)
                newIntent.putExtra("text", text)
                newIntent.putExtra("date", date)
                startActivity(newIntent)
            }
        }
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    private fun initPieChart() {
        pieChart.isDrawHoleEnabled = false
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        pieChart.setTouchEnabled(false)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true
    }

    private fun setDataToPieChart(hashMap: HashMap<String,Float>) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(hashMap["happiness"]!!,"Happiness"))
        dataEntries.add(PieEntry(hashMap["positive"]!!,"Positive"))
        dataEntries.add(PieEntry(hashMap["anger"]!!,"Anger"))
        dataEntries.add(PieEntry(hashMap["sadness"]!!,"Sadness"))
        dataEntries.add(PieEntry(hashMap["negative"]!!,"Negative"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(requireContext(),R.color.green))
        colors.add(ContextCompat.getColor(requireContext(),R.color.yellow))
        colors.add(ContextCompat.getColor(requireContext(),R.color.red))
        colors.add(ContextCompat.getColor(requireContext(),R.color.blue))
        colors.add(ContextCompat.getColor(requireContext(),R.color.blueviolet))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 2f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        //add text in center
        pieChart.setDrawCenterText(true)
        pieChart.centerText = "Sentiments"

        pieChart.invalidate()

    }
}