package com.farm.management.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.farm.management.R;
import com.farm.management.database.AppDatabase;
import com.farm.management.models.HealthRecord;
import com.farm.management.models.Sheep;

import java.util.List;

public class HealthManagementActivity extends AppCompatActivity {

    private int userId;
    private AppDatabase db;
    private Spinner sheepSpinner;
    private EditText illnessEditText;
    private EditText treatmentEditText;
    private EditText treatmentCostEditText;
    private EditText notesEditText;
    private Spinner statusSpinner;
    private Button addHealthRecordButton;
    private RecyclerView healthRecordsRecyclerView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_management);

        userId = getIntent().getIntExtra("userId", -1);
        db = AppDatabase.getInstance(this);

        sheepSpinner = findViewById(R.id.sheepSpinner);
        illnessEditText = findViewById(R.id.illnessEditText);
        treatmentEditText = findViewById(R.id.treatmentEditText);
        treatmentCostEditText = findViewById(R.id.treatmentCostEditText);
        notesEditText = findViewById(R.id.notesEditText);
        statusSpinner = findViewById(R.id.statusSpinner);
        addHealthRecordButton = findViewById(R.id.addHealthRecordButton);
        healthRecordsRecyclerView = findViewById(R.id.healthRecordsRecyclerView);
        backButton = findViewById(R.id.backButton);

        // Setup status spinner
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.health_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        addHealthRecordButton.setOnClickListener(v -> addHealthRecord());
        backButton.setOnClickListener(v -> finish());

        loadSheepSpinner();
    }

    private void loadSheepSpinner() {
        new Thread(() -> {
            List<Sheep> sheepList = db.sheepDao().getActiveSheepByUser(userId);
            runOnUiThread(() -> {
                SheepSpinnerAdapter adapter = new SheepSpinnerAdapter(this, sheepList);
                sheepSpinner.setAdapter(adapter);
            });
        }).start();
    }

    private void addHealthRecord() {
        if (sheepSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "اختر غنمة", Toast.LENGTH_SHORT).show();
            return;
        }

        Sheep selectedSheep = (Sheep) sheepSpinner.getSelectedItem();
        String illness = illnessEditText.getText().toString().trim();
        String treatment = treatmentEditText.getText().toString().trim();
        String costStr = treatmentCostEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();

        if (illness.isEmpty() || treatment.isEmpty() || costStr.isEmpty()) {
            Toast.makeText(this, "جميع الحقول مطلوبة", Toast.LENGTH_SHORT).show();
            return;
        }

        double cost = Double.parseDouble(costStr);
        HealthRecord record = new HealthRecord(selectedSheep.id, illness, treatment, cost, notes, status);

        new Thread(() -> {
            db.healthRecordDao().insertHealthRecord(record);
            runOnUiThread(() -> {
                Toast.makeText(HealthManagementActivity.this, "تم تسجيل السجل الصحي بنجاح", Toast.LENGTH_SHORT).show();
                clearInputs();
            });
        }).start();
    }

    private void clearInputs() {
        illnessEditText.setText("");
        treatmentEditText.setText("");
        treatmentCostEditText.setText("");
        notesEditText.setText("");
    }
}
