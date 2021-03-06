package com.example.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.databinding.ActivityTaskFormBinding
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private var _binding: ActivityTaskFormBinding? = null

    private lateinit var mViewModel: TaskFormViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
    private val listPriority: MutableList<Int> = arrayListOf()
    private var taskID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()

        mViewModel.listPriorities()
        loadDataFromActivity()

    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null){
            taskID = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            mViewModel.load(taskID)
            _binding!!.buttonSave.text = getString(R.string.update_task)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save) {
            handleSave()
        } else if (id == R.id.button_date) {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }


    private fun observe() {

        mViewModel.priorityList.observe(this){
            val list: MutableList<String> = arrayListOf()
            for (item in it ){
                list.add(item.description)
                listPriority.add(item.id)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            _binding!!.spinnerPriority.adapter = adapter
        }

        mViewModel.taskSave.observe(this) {
            if (it.success()){
                if (taskID == 0) {
                    toast(getString(R.string.task_created))
                } else{
                    toast(getString(R.string.task_updated))
                }
                finish()
            } else {
                toast(it.message())
            }
        }

        mViewModel.task.observe(this, Observer{
            _binding!!.editDescription.setText(it.description)
            _binding!!.checkComplete.isChecked = it.complete
            _binding!!.spinnerPriority.setSelection(getIndex(it.priorityId))

            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            _binding!!.buttonDate.text = dateFormat.format(date!!)
        })
    }

    private fun toast(str:String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (i in 0 until listPriority.count()){
            if (listPriority[i] == priorityId ){
                index = i
                break
            }
        }
        return index
    }

    private fun listeners() {
        _binding!!.buttonSave.setOnClickListener(this)
        _binding!!.buttonDate.setOnClickListener(this)
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = taskID
            this.description = _binding!!.editDescription.text.toString()
            this.complete = _binding!!.checkComplete.isChecked
            this.dueDate = _binding!!.buttonDate.text.toString()
            this.priorityId = listPriority[_binding!!.spinnerPriority.selectedItemPosition]

        }
        mViewModel.save(task)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calender = Calendar.getInstance()
        calender.set(year, month, dayOfMonth)

        val dateString : String = dateFormat.format(calender.time)
        _binding!!.buttonDate.text = dateString
    }
}
