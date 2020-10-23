package com.example.infocovid_19.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infocovid_19.R
import com.example.infocovid_19.adapter.ProvinceAdapter
import com.example.infocovid_19.api.RetrofitClient
import com.example.infocovid_19.model.Province
import com.example.infocovid_19.model.ProvinceResponse
import kotlinx.android.synthetic.main.activity_province.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ProvinceActivity : AppCompatActivity() {
    private  lateinit var rvProgressBar : ProgressBar
    private lateinit var itemList : ArrayList<ProvinceResponse>
    private lateinit var mAdapter : ProvinceAdapter

    private lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_province)

        rvProgressBar = findViewById(R.id.RvprogressBar)
        editTextSearch = findViewById(R.id.et_search)

        showProvince()

        editTextSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable) {
                filterList(p0.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

    }

    private fun filterList(filterItem: String){
        var tempList: ArrayList<ProvinceResponse> = ArrayList()

        for(d in itemList){
            if (filterItem.toLowerCase() in d.attributes.province.toLowerCase())
            {
                tempList.add(d)
            }
        }
        mAdapter.updateList(tempList)


    }

    private fun showProvince() {
        rvProvince.setHasFixedSize(true)
        rvProvince.layoutManager = LinearLayoutManager(this)

        RetrofitClient.instance.getProvince().enqueue(object : retrofit2.Callback<ArrayList<ProvinceResponse>>{
            override fun onResponse(
                call: Call<ArrayList<ProvinceResponse>>,
                response: Response<ArrayList<ProvinceResponse>>
            ) {
               val list = response.body()
                val adapter = list?.let { ProvinceAdapter(it) }
                rvProvince.adapter=adapter

                rvProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ArrayList<ProvinceResponse>>, t: Throwable) {
                Toast.makeText(this@ProvinceActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                rvProgressBar.visibility = View.GONE
            }

        })
    }
}