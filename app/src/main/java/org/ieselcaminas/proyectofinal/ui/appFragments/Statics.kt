package org.ieselcaminas.proyectofinal.ui.appFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentStaticsBinding
import org.ieselcaminas.proyectofinal.model.jsonFormater.JsonParser

class Statics : Fragment() {

    private var _binding: FragmentStaticsBinding? = null
    private val binding get() = _binding!!

    private val user = Firebase.auth.currentUser!!
    private val db = Firebase.firestore
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaticsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        barChart = binding.barChart
        val loading = activity?.let { LoadingDialog(it) }!!
        loading.startLoading()
        db.collection("sentiments").document(user.email!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val jsonArray = ArrayList<String>()
                    for (i in it.result.data!!.keys) {
                        jsonArray.add(it.result.data!![i].toString())
                    }
                    initBarChart()
                    setDataToBarChart(JsonParser.parseEmotions(jsonArray))
                    loading.dismissDialog()
                } else {
                    loading.dismissDialog()
                }
            }
        super.onResume()
    }

    private fun initBarChart() {
        barChart.setExtraOffsets(20f, 0f, 20f, 20f)
        barChart.description.text = ""
        barChart.setTouchEnabled(false)
        barChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        barChart.legend.isWordWrapEnabled = true
    }

    private fun setDataToBarChart(hashMap: HashMap<String,Float>) {
        val dataEntries = ArrayList<BarEntry>()
        dataEntries.add(BarEntry(0f,hashMap["happiness"]!!))
        dataEntries.add(BarEntry(20f,hashMap["positive"]!!))
        dataEntries.add(BarEntry(40f,hashMap["anger"]!!))
        dataEntries.add(BarEntry(60f,hashMap["sadness"]!!))
        dataEntries.add(BarEntry(80f,hashMap["negative"]!!))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(requireContext(), R.color.green))
        colors.add(ContextCompat.getColor(requireContext(), R.color.yellow))
        colors.add(ContextCompat.getColor(requireContext(), R.color.red))
        colors.add(ContextCompat.getColor(requireContext(), R.color.blue))
        colors.add(ContextCompat.getColor(requireContext(), R.color.blueviolet))

        val dataSet = BarDataSet(dataEntries, "")
        val data = BarData(dataSet)

        // In Percentage
       // data.setValueFormatter(PercentFormatter())
        dataSet.colors = colors
        barChart.data = data
        data.setValueTextSize(12f)
        barChart.setExtraOffsets(5f, 10f, 5f, 5f)
        barChart.animateY(1400, Easing.EaseInOutQuad)


        barChart.invalidate()

    }
}