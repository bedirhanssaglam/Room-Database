package com.example.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.base.EmployeeApp
import com.example.roomdemo.database.EmployeeDao
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.DialogUpdateBinding
import com.example.roomdemo.model.EmployeeEntity
import com.example.roomdemo.ui.ItemAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * MainActivity handles the main user interface for managing employee records.
 * It integrates with Room database through EmployeeDao to perform CRUD operations.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var employeeDao: EmployeeDao
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize EmployeeDao from the application context
        employeeDao = (application as EmployeeApp).database.employeeDao()

        // Setup UI components and observe changes in employee records
        setupViews()
        observeEmployees()
    }

    /**
     * Sets up click listeners and RecyclerView for displaying employees.
     */
    private fun setupViews() {
        binding.apply {
            btnAdd.setOnClickListener { addRecord() }
            rvItemsList.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    /**
     * Observes changes in the list of employees and updates the UI accordingly.
     */
    private fun observeEmployees() {
        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect { employees ->
                if (employees.isEmpty()) {
                    showNoRecordsMessage()
                } else {
                    showEmployees(employees)
                }
            }
        }
    }

    /**
     * Adds a new employee record to the database based on user input.
     * Displays a toast message on successful addition.
     */
    private fun addRecord() {
        val name: String = binding.etName.text.toString()
        val email: String = binding.etEmailId.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                clearFields()
                showToast(getString(R.string.record_saved))
            }
        } else {
            showToast(getString(R.string.empty_field_warning))
        }
    }

    /**
     * Displays the list of employees in RecyclerView.
     * Handles click events for updating and deleting employee records.
     */
    private fun showEmployees(employees: List<EmployeeEntity>) {
        binding.apply {
            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            itemAdapter = ItemAdapter(
                employees,
                { id -> updateRecordDialog(id) },
                { id -> deleteRecordDialog(id) }
            )
            rvItemsList.adapter = itemAdapter
        }
    }

    /**
     * Shows a message when no employee records are available.
     */
    private fun showNoRecordsMessage() {
        binding.apply {
            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Displays a dialog for updating an employee record.
     * Updates the record in the database upon user confirmation.
     */
    private fun updateRecordDialog(id: Int) {
        val dialog = Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog_Alert)
        val dialogBinding: DialogUpdateBinding = DialogUpdateBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)

        lifecycleScope.launch {
            val employee: Flow<EmployeeEntity> = employeeDao.fetchEmployeeByID(id)
            dialogBinding.apply {
                employee.collect {
                    etUpdateName.setText(it.name)
                    etUpdateEmailId.setText(it.email)
                }
            }
        }

        dialogBinding.apply {
            tvUpdate.setOnClickListener {
                val name: String = etUpdateName.text.toString()
                val email: String = etUpdateEmailId.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    lifecycleScope.launch {
                        employeeDao.update(EmployeeEntity(id, name, email))
                        dialog.dismiss()
                        showToast(getString(R.string.record_updated_message))
                    }
                } else {
                    showToast(getString(R.string.empty_field_warning))
                }
            }
            tvCancel.setOnClickListener { dialog.dismiss() }
        }

        dialog.show()
    }

    /**
     * Displays a confirmation dialog for deleting an employee record.
     * Deletes the record from the database upon user confirmation.
     */
    private fun deleteRecordDialog(id: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_record))
            .setPositiveButton(R.string.delete) { _, _ ->
                lifecycleScope.launch {
                    employeeDao.delete(EmployeeEntity(id))
                    showToast(getString(R.string.record_deleted_successfully))
                }
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
            .setCancelable(false)
            .show()
    }

    /**
     * Displays a short toast message.
     */
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Clears the input fields for new records.
     */
    private fun clearFields() {
        binding.apply {
            etName.text.clear()
            etEmailId.text.clear()
        }
    }
}
