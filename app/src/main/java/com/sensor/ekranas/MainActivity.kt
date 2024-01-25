package com.sensor.ekranas


import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sensor.ekranas.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask


private lateinit var timer: Timer // 1 sek
private lateinit var timerTask: TimerTask


// RS85 vidseo URL https://youtu.be/QRL-kDpGqJA

//KEY
private var savedIpaddress: String = "http://82.131.3.41:8091"
private val savedIPKEY = "IpAddress_Key"
private val port7KEY = "port7_Key"
private val port8KEY = "port8_Key"
private val is_app_closedKEY="is_app_closed_KEY"

private var kuulab7 = "zero inf"
private var kuulab8 = "vaikus"

private lateinit var recyclerView: RecyclerView

class MainActivity : AppCompatActivity() {

    private var sisend7 = false
    private var sisend8 = false

    private lateinit var binding: ActivityMainBinding


    private var adr_temp1: String = "82.131.3.41"

    private var adr_temp2: String = "82.131.3.41"
    private var adr_temp3: String = "82.131.3.41"
    private var adr_temp4: String = "82.131.3.41"
    private var adr_sisend7: String = "82.131.3.41"
    private var adr_sisend8: String = "82.131.3.41"

    private var port1: String = "8091"
    private var port2: String = "8092"
    private var port3: String = "8093"
    private var port4: String = "8094"
    private var port7: String = "xxxx7"
    private var port8: String = "xxxx8"

    //  val stringListAdapter = StringListAdapter<FileItem>(listOf()) // Muutke adapteri nimi siin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        // Looge WebSocket-ühendus
        val proovi_aadress: String = "ws://192.168.0.47"

        // timer

        // Taasta salvestatud port3 väärtus
        restoreIpAddress()  // siin tõmbab Saved preferencist sisse


        Log.d("onCreate","recycleview käivitamine2")

        //

      //  val customAdapter = CustomAdapter(AndmetePank().dataset)
        val customAdapter = CustomAdapter(deviceList)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view1)
        recyclerView.adapter = customAdapter



        timer = Timer() //................TIMER 1 sek ..................
        timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {

                    // tegevus timeri sees

                    //  binding.textViewTemp2.text="T2:"
                    // Käivita lugemine vastavate aadressidega
                    Log.d("Timer ", "adr_temp2 $adr_temp2  port:$port2")
                    Lugemine({ "http://$adr_temp2:$port2" }, binding.textViewTemp2).execute()

                    Log.d("Timer ", "adr_temp3 $adr_temp3  port:$port3")
                    Lugemine({ "http://$adr_temp3:$port3" }, binding.textViewTemp3).execute()

                    Log.d("Timer ", "adr_temp4 $adr_temp4  port:$port4")
                    Lugemine({ "http://$adr_temp4:$port4" }, binding.textViewTemp4).execute()

                    Log.d("Timer ", "adr_temp1 $adr_temp1  port:$port1")
                    Lugemine({ "http://$adr_temp1:$port1" }, binding.textViewTemp1).execute()


                    Log.d("Timer ", "adr_kyte $adr_sisend7  port:$port7")
                    Lugemine({ "http://$adr_sisend7:$port7" }, binding.textViewkyte7).execute()
                    Log.d("Timer ", "adr_kyte $adr_sisend8  port:$port8")
                    Lugemine({ "http://$adr_sisend8:$port8" }, binding.textViewkyte8).execute()




                    binding.textViewkyte7.text = "$adr_temp1:$port7  =>$kuulab7"

                    binding.textViewkyte8.text = "$adr_temp1:$port8  =>$kuulab8"

                    Log.d("Timeris", "adr_temp1 $adr_temp1")

                    //binding.imageButtonPlay.background = resources.getDrawable(R.drawable.play_up)
                    if (kuulab8 == "SEES") {
                        binding.imageViewFloorPlan.setImageResource(R.drawable.korteriplaan2heating)
                    } else {
                        binding.imageViewFloorPlan.setImageResource(R.drawable.korteriplaan2)


                    }

                }// thread


            }// run


        } // timerTask
        timer.schedule(timerTask, 10000, 10000)


        // Leia ImageButton-i viide
        val imageButtonA = findViewById<ImageButton>(R.id.imageButtonnuppA)

        // Lisa OnClickListener   Nupp7  kuulaja-----------------------------------
        imageButtonA.setOnClickListener {
            // See kood käivitatakse, kui ImageButton-ile klõpsatakse

            var urlsisend7 = "http://$adr_sisend7"


            val cal = Calendar.getInstance()
            val currentHour = cal.get(Calendar.HOUR_OF_DAY)
            val formattedHour = if (currentHour < 10) "0$currentHour" else currentHour.toString()
            Log.d("NUPP A", "port7 on $port7")


            // Vaheta nupu pilti
            if (sisend7) {
                // Kui kyte on sisse lülitatud, siis pane välja lülitatud pilt
                imageButtonA.setImageResource(R.drawable.kyteon)
                Log.d("Nupp", " Kuulamine is   if (isKyteOn) {")
                // toimub heat1++   Arduino poole peal
                val selline_kiri = "$urlsisend7:$port7/4/on"
                Lugemine({ selline_kiri }, binding.textViewTemp2).execute()
                sisend7 = false
            } else
                if (!sisend7) {
                    // Kui kyte on välja lülitatud, siis pane sisse lülitatud pilt
                    imageButtonA.setImageResource(R.drawable.kyteoff)
                    val selline_kiri = "$urlsisend7:$port7/4/off"

                    Lugemine({ selline_kiri }, binding.textViewTemp2).execute()
                    Log.d("Selline Kiri", selline_kiri)
                    sisend7 = true
                }

        }


        // teeme siia veel ühe nupu
        val imageButtonB = findViewById<ImageButton>(R.id.imageButtonnuppB)

        // Lisa OnClickListener   Nupp8  kuulaja-----------------------------------
        imageButtonB.setOnClickListener {
            // See kood käivitatakse, kui ImageButton-ile klõpsatakse

            var urlsisend8 = "http://$adr_sisend8"


            val cal = Calendar.getInstance()
            val currentHour = cal.get(Calendar.HOUR_OF_DAY)
            val formattedHour = if (currentHour < 10) "0$currentHour" else currentHour.toString()
            Log.d("NUPP B", "port8 on $port8")


            // Vaheta nupu pilti
            if (sisend8) {
                // Kui kyte on sisse lülitatud, siis pane välja lülitatud pilt
                imageButtonB.setImageResource(R.drawable.kyteon)
                Log.d("Nupp", " Kuulamine is   if (isKyteOn) {")
                // toimub heat1++   Arduino poole peal
                val selline_kiri = "$urlsisend8:$port8/4/on"
                Lugemine({ selline_kiri }, binding.textViewTemp2).execute()
                sisend8 = false
            } else
                if (!sisend8) {
                    // Kui kyte on välja lülitatud, siis pane sisse lülitatud pilt
                    imageButtonB.setImageResource(R.drawable.kyteoff)
                    val selline_kiri = "$urlsisend8:$port8/4/off"

                    //Lugemine({ selline_kiri }, binding.textViewTemp2).execute()
                    Log.d("Selline Kiri", selline_kiri)
                    sisend8 = true
                }

        }


    }// on create  ------------------------------------------------------


    private fun restoreIpAddress() {
        // Pääse ligi SharedPreferences-i
        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        // Loe salvestatud IP addressi väärtus
        val IpAddress = sharedPref.getString(savedIPKEY, null)

        val loll7 = sharedPref.getString(port7KEY, null)
        port7 = loll7.toString()
        Log.d("otsime Sharedist", "Port7 $port7")
        val loll8 = sharedPref.getString(port8KEY, null)
        port8 = loll8.toString()

        // Määra port3 muutuja väärtus vastavalt salvestatud väärtusele
        savedIpaddress =
            IpAddress ?: "82.131.3.41" // Kui salvestatud väärtus puudub, kasuta vaikimisi väärtust

        adr_temp1 = savedIpaddress
        adr_temp2 = savedIpaddress
        adr_temp3 = savedIpaddress
        adr_temp4 = savedIpaddress
        adr_sisend7 = savedIpaddress
        adr_sisend8 = savedIpaddress


    }


    //   private var lastEnteredAddress: String? = null

    private fun showIpAddressDialog() {
        val editText = EditText(this)
        editText.hint =
            adr_temp1 ?: "82.131.3.75" // Kasutab viimati sisestatud aadressi või vaikimisi hint-i
        val builder = AlertDialog.Builder(this)
            //  AlertDialog.Builder(context)
            .setTitle("Enter IP Address")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                var ipAddress = editText.text.toString()
                saveIpAddress(ipAddress)
                Log.d("dialoogi aken", "ipAdress $ipAddress")
                // Salvesta sisestatud aadress muutujasse

                // Kasutage seda IP-aadressi oma lugemisfunktsioonides
                // Näiteks: Lugemine(ipAddress, binding.textViewTemp2).execute()

                adr_temp1 = ipAddress
                adr_temp2 = ipAddress
                adr_temp3 = ipAddress
                adr_temp4 = ipAddress
                adr_sisend7 = ipAddress
                adr_sisend8 = ipAddress
                Log.d("ipAdressid ", " $adr_temp2 $adr_temp3 $adr_temp4 $adr_temp1 $adr_sisend7")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun saveIpAddress(sisendAddress: String) {
        // Pääse ligi SharedPreferences-i
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        // Alusta redigeerimist
        val editor = sharedPref.edit()

        // Salvesta port3 väärtus
        editor.putString(savedIPKEY, sisendAddress)

        // Rakenda muudatused
        editor.apply()

    }


    private fun salvestapordid() {
        // Pääse ligi SharedPreferences-i
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        // Alusta redigeerimist
        val editor = sharedPref.edit()

        // Salvesta port3 väärtus
        editor.putString(port7KEY, port7)
        editor.putString(port8KEY, port8)
        // Rakenda muudatused
        editor.apply()

        // Uuenda  muutujat, kui soovid seda hiljem kasutada
        //  savedIpaddress = sisendAddress
    }


    private fun showPort2Dialog() {
        val editText = EditText(this)
        editText.hint = port2 ?: "8092" // Kasutab viimati sisestatud aadressi või vaikimisi hint-i
        val builder = AlertDialog.Builder(this)

            .setTitle("Enter port2 address")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                var portAddress = editText.text.toString()
                Log.d("dialoogi aken", "port2 $port2")


                port2 = portAddress

            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showPort1Dialog() {
        val editText = EditText(this)
        editText.hint = port1 ?: "8091" // Kasutab viimati sisestatud aadressi või vaikimisi hint-i
        val builder = AlertDialog.Builder(this)

            .setTitle("Enter port1 address")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                var portAddress = editText.text.toString()
                Log.d("dialoogi aken", "port5 $port1")


                port3 = portAddress

            }
            .setNegativeButton("Cancel", null)
            .show()
    }

// ...

    private fun showPort3Dialog() {
        val editText = EditText(this)
        editText.hint = port3 ?: "8093" // Kasutab viimati sisestatud aadressi või vaikimisi hint-i
        val builder = AlertDialog.Builder(this)

            .setTitle("Enter port3 address")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                var port3 = editText.text.toString()
                Log.d("dialoogi aken", "port3 $port3")


            }
            .setNegativeButton("Cancel", null)
            .show()
    }
//  .........................................................

    private fun showPort4Dialog() {
        val editText = EditText(this)
        editText.hint = port4 ?: "8094" // Kasutab viimati sisestatud aadressi või vaikimisi hint-i
        val builder = AlertDialog.Builder(this)

            .setTitle("Enter port4 address")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                var portAddress = editText.text.toString()
                Log.d("dialoogi aken", "port4 $port4")


                port4 = portAddress

            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showPort7Dialog() {
        val editText = EditText(this)
        editText.hint = port7 ?: "8099" // Kasutab viimati sisestatud aadressi või vaikimisi hint-i
        val builder = AlertDialog.Builder(this)

            .setTitle("Enter port7:")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                var portAddress = editText.text.toString()
                Log.d("dialoogi aken", "port7 $port7")


                port7 = portAddress
                salvestapordid()

            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPort8Dialog() {
        val editText = EditText(this)
        editText.hint = port8 ?: "8100" // Kasutab viimati sisestatud aadressi või vaikimisi hint-i
        val builder = AlertDialog.Builder(this)

            .setTitle("Enter port8:")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                var portAddress = editText.text.toString()
                Log.d("dialoogi aken", "port8 $port8")


                port8 = portAddress
                salvestapordid()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.IP_Address -> {
                showIpAddressDialog()
                return true
            }

            R.id.Port_1 -> {
                showPort1Dialog()
                return true

            }


            R.id.Port_2 -> {
                showPort2Dialog()
                return true
            }

            R.id.Port_3 -> {
                showPort3Dialog()
                return true
            }

            R.id.Port_4 -> {
                showPort4Dialog()
                return true
            }

            R.id.Port_7 -> {
                showPort7Dialog()
                return true
            }

            R.id.Port_8 -> {
                showPort8Dialog()
                return true
            }


        }// when

        return super.onOptionsItemSelected(item)
    }


    class Lugemine(private val urlProvider: () -> String, private val textView: TextView) :
        AsyncTask<Void, Void, String>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): String? {
            var response: String? = null
            try {
                val url =
                    urlProvider.invoke() // Siin kutsume välja urlProvider'i, et saada ajakohane URL
                Log.d("CLASS Lugemine", "url: $url")

                val conn = URL(url).openConnection() as HttpURLConnection
                //  val conn = URL(url).openConnection() as HttpURLConnection
                // Ülejäänud kood jääb samaks
                conn.requestMethod = "GET"
                conn.connectTimeout = 20000 // Ühenduse ajalimiit 10 sekundit
                conn.readTimeout = 20000 // Lugemise ajalimiit 10 sekundit

                val statusCode = conn.responseCode
                if (statusCode == 200) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    response = reader.readText()
                    Log.d("DEBUG", "Response: $response ")
                }
                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return response
        }// override fun

        @Deprecated("Deprecated in Java")
        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val vastus: String = result.toString()
            if (!result.isNullOrEmpty()) {
                // Kasutage regulaaravaldist, et trimmida tekst enne ja pärast JSON-kujundust
                val muudetud: String = vastus.replace(Regex(".*<p>(\\{.*\\})</p>.*"), "$1")

                // Nüüd on muudetud String ainult JSON-kujundus
                Log.d("VASTUS", "kogutext: $vastus ")
                //              Log.d("PORT7 PORT8", "port7:$port7, port8:$port8")
//


                kuulab7 = if ("HEATING" in muudetud) {
                    "SEES"
                } else {
                    "OFF"
                }




                if ("HEATING" in muudetud) {
                    kuulab8 = "SEES"

                } else {
                    kuulab8 = "OFF"
                }

                Log.d("MUUDETUD DATA", "kogutext: $muudetud")
                try {
                    val jsonObject = JSONObject(muudetud)
                //    val jsonObject = JSONObject(vastus)
                    if (jsonObject.has("temperature")) {
                        val temperature = jsonObject.getString("temperature")
                        textView.text = "$temperature °"
                    } else if (jsonObject.has("kyte")) {
                        val kyttemuutuja = jsonObject.getString("kyte")
                        textView.text = "$kyttemuutuja funkab ju"
                        //tegevus
                    } else if ("temperature" in vastus) {
                        Log.d("temperature result = ", "$result")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // textView.text = "Andmete analüüsimisel tekkis viga."
                }
            } else {
                //  textView.text = "Andmete laadimisel tekkis viga."
            }
        }//
    }// inner class


    override fun onDestroy() {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putBoolean(is_app_closedKEY, true)
        editor.putString(port7KEY, port7)
        editor.putString(port8KEY, port8)
        editor.putString(savedIPKEY, adr_temp1)
        super.onDestroy()
    }


}//class


