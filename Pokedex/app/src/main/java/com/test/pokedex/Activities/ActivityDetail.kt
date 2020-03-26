package com.test.pokedex.Activities
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.test.pokedex.R
import kotlinx.android.synthetic.main.activity_detail.*

class ActivityDetail: AppCompatActivity() {
    private lateinit var nom: TextView
    private lateinit var num: TextView
    private lateinit var type: TextView
    private lateinit var moves: TextView
    private lateinit var stats: TextView
    var url : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        nom = findViewById(R.id.pokemon_name)
        num = findViewById(R.id.pokemon_number)
        type = findViewById(R.id.pokemon_types)
        stats = findViewById(R.id.pokemon_stats)
        moves = findViewById(R.id.pokemon_moves)

        val bundle :Bundle ?=intent.extras
        url = bundle!!.getString("url").toString()

        main()
    }
    fun main(){
        Ion.with(this)
            .load(url)
            .asJsonObject()
            .done { e, result ->
                if(e == null){
                    nom.text = result.get("name").toString().replace("\"","").capitalize()
                    num.text = result.get("id").toString()
                    type.text = getTypes(result.get("types").asJsonArray)

                    moves.text = getMoves(result.get("moves").asJsonArray)
                    stats.text = getStats(result.get("stats").asJsonArray)

                    if(!result.get("sprites").isJsonNull){

                        if(result.get("sprites").asJsonObject.get("front_default") != null){
                            Log.i("Res", result.get("sprites").asJsonObject.get("front_default").asString)
                            Glide
                                .with(this)
                                .load(result.get("sprites").asJsonObject.get("front_default").asString)
                                .placeholder(R.drawable.pokemon_logo_min)
                                .error(R.drawable.pokemon_logo_min)
                                .into(pokemon_image)
                        }else{
                            pokemon_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pokemon_logo_min))
                        }
                    }else{
                        pokemon_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pokemon_logo_min))
                    }
                }
            }
    }
    fun getTypes(a: JsonArray): String{

        var types: String = ""
        for(i in a){
            var x: JsonObject = i.asJsonObject.getAsJsonObject("type")
            types = types + x.get("name").asString.capitalize() + " \n"
        }
        return types
    }
    fun getStats(a: JsonArray): String{

        var stats: String = ""
        for(i in a){
            val stat = i.asJsonObject.get("base_stat").asString
            val statName: JsonObject = i.asJsonObject.getAsJsonObject("stat")

            stats = stats + statName.get("name").asString.capitalize() + ": " + stat + "\n"
        }
        return stats
    }
    fun getMoves(a: JsonArray): String{

        var moves: String = ""
        for(i in a){
            var x: JsonObject = i.asJsonObject.getAsJsonObject("move")
            moves = moves + x.get("name").asString.capitalize() + "\n"
        }
        return moves
    }







}