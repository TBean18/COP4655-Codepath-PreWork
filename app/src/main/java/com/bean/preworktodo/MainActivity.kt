package com.bean.preworktodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfToDos = mutableListOf<String>()
    lateinit var adapter: ToDoItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Long Click Action for tasks
        val onClickListeners = object : ToDoItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // remove from list
                listOfToDos.removeAt(position)
                // Notify adapter
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }


        // Populate the Data
//        listOfToDos.add("Task 1")
        loadItems()

        // Look up the recycler view in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.rViewer)
        // Create the adapter to supply the data
        adapter = ToDoItemAdapter(listOfToDos, onClickListeners)
        // Attach the adapter to the Recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val textFieldRef = findViewById<EditText>(R.id.editTextToDoName)
        // Set up input so user can add tasks
        findViewById<Button>(R.id.button).setOnClickListener {

            val toDoText: String = textFieldRef.text.toString();
            listOfToDos.add(toDoText)
            Log.i("testTag", "User CLicked On the Button Containing $toDoText")

            adapter.notifyItemInserted(listOfToDos.size - 1)

            textFieldRef.setText("");
            saveItems()
        }

    }

    // Save thew data that the user has created

    // Get teh file we need
    fun getDataFile(): File {
        return File(filesDir, "data.txt")
    }

    // Save Items
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfToDos)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    // Read Tasks
    fun loadItems(){
        try {
        listOfToDos = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (e: IOException){
            e.printStackTrace()
        }

    }


}