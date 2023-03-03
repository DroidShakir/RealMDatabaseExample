package com.protocol.realmdatabaseexample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.realmdatabaseexample.R
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.data_input.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var realm: Realm? = null
    private val dataModel = DataModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()

        btnInsertData.setOnClickListener(this)
        btnReadData.setOnClickListener(this)
        btnUpdateData.setOnClickListener(this)
        btnDeleteData.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnInsertData -> {
                addData()
            }

            R.id.btnReadData -> {
                readData()
            }

            R.id.btnUpdateData -> {
                updateData()
            }

            R.id.btnDeleteData -> {
                deleteData()
            }
        }
    }

    private fun addData() {
        try {
            dataModel.id = edtId.text.toString().toInt()
            dataModel.name = edtName.text.toString()
            dataModel.email = edtEmail.text.toString()

            realm!!.executeTransaction { realm -> realm.copyToRealm(dataModel) }

            clearFields()

            Log.d("Status", "Data Inserted !!!")
        } catch (e: Exception) {
            Log.d("Status", "Something went Wrong !!!")
        }
    }

    private fun clearFields() {
        edtId.setText("")
        edtName.setText("")
        edtEmail.setText("")
    }

    private fun readData() {
        try {
            val dataModels: List<DataModel> =
                realm!!.where(DataModel::class.java).findAll()

            for (i in dataModels.indices) {
                edtId?.setText("" + dataModels[i].id)
                edtName?.setText(dataModels[i].name)
                edtEmail?.setText(dataModels[i].email)
            }
            Log.d("Status", "Data Fetched !!!")
        } catch (e: Exception) {
            Log.d("Status", "Something went Wrong !!!")
        }
    }

    private fun updateData() {
        try {
            val id: Long = edtId.text.toString().toLong()
            val dataModel =
                realm!!.where(DataModel::class.java).equalTo("id", id).findFirst()

            edtName.setText(dataModel?.name)
            edtEmail.setText(dataModel?.email)

            Log.d("Status", "Data Fetched !!!")
        } catch (e: Exception) {
            Log.d("Status", "Something went Wrong !!!")
        }
    }

    private fun deleteData() {
        try {
            val id: Long = edtId.text.toString().toLong()
            val dataModel =
                realm!!.where(DataModel::class.java).equalTo("id", id).findFirst()
            realm!!.executeTransaction {
                dataModel?.deleteFromRealm()
            }
            clearFields()

            Log.d("Status", "Data deleted !!!")
        } catch (e: Exception) {
            Log.d("Status", "Something went Wrong !!!")
        }
    }
}