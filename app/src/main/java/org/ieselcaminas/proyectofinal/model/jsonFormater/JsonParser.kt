package org.ieselcaminas.proyectofinal.model.jsonFormater

import org.json.JSONObject
import org.json.JSONTokener

class JsonParser() {
    companion object {
        fun parseEmotions(input: ArrayList<String>): HashMap<String, Float> {
            var happiness = 0f
            var positive = 0f
            var sadness = 0f
            var negative = 0f
            var anger = 0f
            val hasMap = HashMap<String,Float>()

            try {
                for (i in input) {
                    val json = JSONTokener(i).nextValue() as JSONObject
                    val array = json.getJSONArray("output").getJSONObject(0).getJSONArray("labels")
                    for (o in 0 until array.length()) {
                        val obj = array.getJSONObject(o)
                        val arraySpan = obj.getJSONArray("span")
                        var name = obj.getString("name")
                        if (name=="null") {
                            name = obj.getString("value")
                        }

                        val numSpan: Float = (arraySpan.getDouble(0).toFloat() + arraySpan.getDouble(1).toFloat())/2
                        when (name) {
                            "happiness" -> happiness += numSpan
                            "POS" -> positive += numSpan
                            "sadness" -> sadness += numSpan
                            "NEG" -> negative += numSpan
                            "anger" -> anger += numSpan
                        }
                    }
                }
            } catch (e: Exception) {}

            hasMap["happiness"] = happiness
            hasMap["positive"] = positive
            hasMap["sadness"] = sadness
            hasMap["negative"] = negative
            hasMap["anger"] = anger

            return hasMap
        }
    }
}