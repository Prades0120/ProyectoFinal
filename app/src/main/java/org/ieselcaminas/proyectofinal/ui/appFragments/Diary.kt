package org.ieselcaminas.proyectofinal.ui.appFragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.eazegraph.lib.models.PieModel
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentDiaryBinding
import org.ieselcaminas.proyectofinal.model.recyclerView.Item
import org.ieselcaminas.proyectofinal.model.recyclerView.RecyclerViewAdapter
import org.ieselcaminas.proyectofinal.ui.CreatePage


class Diary : Fragment() {

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser!!
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var array: ArrayList<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mPieChart = binding.piechart

        mPieChart.addPieSlice(PieModel("Happiness", 25F,ContextCompat.getColor(requireContext(),R.color.yellow)))
        mPieChart.addPieSlice(PieModel("Neutral", 35F, ContextCompat.getColor(requireContext(),R.color.grey)))
        mPieChart.addPieSlice(PieModel("Angry", 15F, ContextCompat.getColor(requireContext(),R.color.red)))
        mPieChart.addPieSlice(PieModel("Sadness", 9F,ContextCompat.getColor(requireContext(),R.color.blue)))

        mPieChart.startAnimation()
    }

    override fun onResume() {
        val loading = activity?.let { LoadingDialog(it) }!!
        loading.startLoading()
        db.collection("docs").document(user.email!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val mutableList = it.result.data.orEmpty()
                    val newArray = ArrayList<Item>(0)
                    for (i in mutableList.keys) {
                        newArray.add(Item(mutableList[i].toString(),i))
                    }
                    if (newArray.isEmpty())
                        newArray.add(Item("","No Data"))
                    array = newArray
                    recViewConstruction()
                    loading.dismissDialog()
                } else {
                    array = ArrayList(0)
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
}