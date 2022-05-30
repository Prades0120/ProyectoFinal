package org.ieselcaminas.proyectofinal.ui.appFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentStaticsBinding
import org.ieselcaminas.proyectofinal.model.jsonFormater.JsonParser
import org.ieselcaminas.proyectofinal.ui.LoadingDialog

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
                    val jsonArray = ArrayList<String>(0)
                    val data = it.result.data
                    if (data!=null && data.isNotEmpty()) {
                        for (i in data.keys) {
                            jsonArray.add(data[i].toString())
                        }
                        initBarChart()
                        val emotionData = JsonParser.parseEmotions(jsonArray)
                        setDataToBarChart(emotionData)
                    }
                    loading.dismissDialog()
                } else {
                    loading.dismissDialog()
                }
            }
        super.onResume()
    }

    private fun initBarChart() {
        barChart.description.text = ""
        barChart.setTouchEnabled(false)
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToBarChart(hashMap: HashMap<String,Float>) {
        val dataEntries = ArrayList<BarEntry>()

        val happiness = hashMap["happiness"]!!
        val positive = hashMap["positive"]!!
        val anger = hashMap["anger"]!!
        val sadness = hashMap["sadness"]!!
        val negative = hashMap["negative"]!!

        if (negative+sadness > happiness+positive && negative+sadness > anger) {
            binding.resultStatics.setBackgroundResource(R.drawable.statics_bad)
            binding.resultStatics.text = "You feel frequently sad or with negative emotions.\nWe recommend you to talk with someone to solve that.\nYou are fantastic. :)"
        } else if (happiness+positive > anger) {
            binding.resultStatics.setBackgroundResource(R.drawable.statics_good)
            binding.resultStatics.text = "You commonly feel positive and happy. You are affronting with no issues the problems in your live.\nYou must be proud of you for been that awesome. ;)"
        } else {
            binding.resultStatics.setBackgroundResource(R.drawable.statics_anger)
            binding.resultStatics.text = "You are angry with the world that is around you. You need to get some vacations and relax with your family and friends or meet someone\nGet a break."
        }

        dataEntries.add(BarEntry(0f,happiness))
        dataEntries.add(BarEntry(20f,positive))
        dataEntries.add(BarEntry(40f,anger))
        dataEntries.add(BarEntry(60f,sadness))
        dataEntries.add(BarEntry(80f,negative))

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
        barChart.barData.barWidth = 6f
        data.setValueTextSize(12f)
        barChart.setExtraOffsets(0f,0f,0f,10f)
        barChart.animateY(1400, Easing.EaseInOutQuad)


        barChart.invalidate()

    }
}