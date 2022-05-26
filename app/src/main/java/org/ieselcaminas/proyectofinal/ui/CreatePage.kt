package org.ieselcaminas.proyectofinal.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.ActivityCreatePageBinding
import org.ieselcaminas.proyectofinal.model.restCaller.VolleySingleton


class CreatePage : AppCompatActivity() {

    private var _binding: ActivityCreatePageBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private var _user: FirebaseUser? = null
    private val user get() = _user!!
    private var clicked = false

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val translateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_right_anim) }
    private val translateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_right_anim) }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        _user = Firebase.auth.currentUser
        _binding = ActivityCreatePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.fabDelete.visibility = View.INVISIBLE
        binding.fabSave.visibility = View.INVISIBLE

        val text = intent.getStringExtra("text")
        val date = intent.getStringExtra("date")

        if (text!=null){
            binding.editTextTextMultiLine.text.append(text)
        }

        binding.floatingActionButton.setOnClickListener {
            onAddButtonClicked()
        }

        binding.buttonIgnore.setOnClickListener {
            finish()
        }

        binding.fabDelete.setOnClickListener {
            if (date!=null) {
                val updates = hashMapOf<String,Any>(
                    date to FieldValue.delete()
                )
                db.collection("docs").document(user.email!!).update(updates).addOnCompleteListener {
                    if (it.isSuccessful) {
                        finish()
                    } else {
                        finish()
                    }
                }
            } else {
                finish()
            }
        }

        binding.fabSave.setOnClickListener {
            val loading = LoadingDialog(this)
            loading.startLoading()
            val newDate = date ?: System.currentTimeMillis().toString()
            val textEdited = binding.editTextTextMultiLine.text.toString()
            val textSended: String = if (textEdited.length > 1800) {
                textEdited.replace("\"","\'").replace("\n"," ").replace("\t", " ").substring(0,1799)
            } else {
                textEdited.replace("\"","\'").replace("\n"," ").replace("\t", " ")
            }
            val url = "https://api.oneai.com/api/v0/pipeline"
            val doc = hashMapOf<String,Any>(
                newDate to textEdited
            )
            db.collection("docs").document(user.email!!).update(doc).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Volley post request with parameters
                    val request = object : StringRequest(Method.POST, url,
                        Response.Listener { response ->
                            // Process the json
                            try {
                                val map = hashMapOf<String,Any>(
                                    newDate to response.toString()
                                )
                                db.collection("sentiments").document(user.email!!).update(map)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            loading.dismissDialog()
                                            finish()
                                        } else {
                                            db.collection("sentiments").document(user.email!!).set(map)
                                                .addOnCompleteListener { insert ->
                                                    if (insert.isSuccessful) {
                                                        loading.dismissDialog()
                                                        finish()
                                                    } else {
                                                        Log.e(Log.ERROR.toString(),insert.exception.toString())
                                                        loading.dismissDialog()
                                                        finish()
                                                    }
                                                }
                                        }
                                    }
                            }catch (e:Exception){
                                Log.e(Log.ERROR.toString(),"Exceptio: ${e.message}")
                                loading.dismissDialog()
                                finish()
                            }

                        }, Response.ErrorListener{
                            // Error in request
                            Log.e(Log.ERROR.toString(),"Volley error: ${it.message}")
                            loading.dismissDialog()
                            finish()
                        }) {

                        override fun getBodyContentType(): String {
                            return "application/json; charset=utf-8"
                        }

                        override fun getBody(): ByteArray {
                            val requestBody = "{\"input\":\"$textSended\",\n" +
                                    "\"input_type\":\"auto-detect\",\"steps\":[{\"skill\":\"emotions\"},{\"skill\":\"sentiments\"}]}"
                            return requestBody.toByteArray(charset("utf-8"))
                        }

                        override fun getHeaders(): MutableMap<String, String> {
                            val mutableMap = mutableMapOf<String,String>()
                            mutableMap["accept"] = "application/json"
                            mutableMap["api-key"] = "9b91af9a-872e-4c16-8ee2-1557391942d0"
                            return mutableMap
                        }
                    }
                    // Volley request policy, only one time request to avoid duplicate transaction
                    request.retryPolicy = DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        // 0 means no retry
                        0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                        1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
                    // Add the volley post request to the request queue
                    VolleySingleton.getInstance(this).addToRequestQueue(request)
                } else {
                    loading.dismissDialog()
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.fabSave.visibility = View.VISIBLE
            binding.fabDelete.visibility = View.VISIBLE
        } else {
            binding.fabSave.visibility = View.INVISIBLE
            binding.fabDelete.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fabSave.startAnimation(translateOpen)
            binding.fabDelete.startAnimation(translateOpen)
            binding.floatingActionButton.startAnimation(rotateOpen)
        } else {
            binding.fabSave.startAnimation(translateClose)
            binding.fabDelete.startAnimation(translateClose)
            binding.floatingActionButton.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.fabSave.isClickable = true
            binding.fabDelete.isClickable = true
        } else {
            binding.fabSave.isClickable = false
            binding.fabDelete.isClickable = false
        }
    }
}