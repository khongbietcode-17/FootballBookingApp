package com.example.datsanbongda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datsanbongda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CapNhatThongTinActivity extends AppCompatActivity {

    private EditText edtTenSan, edtGiaSan, edtDiaChiSan;
    private Button btnXacNhanCapNhat;
    private String sanId; // ID sân để cập nhật Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capnhatsan);

        edtTenSan = findViewById(R.id.edtTenSan);
        edtGiaSan = findViewById(R.id.edtGiaSan);
        edtDiaChiSan = findViewById(R.id.edtDiaChiSan);
        btnXacNhanCapNhat = findViewById(R.id.btnXacNhanCapNhat);

        // Nhận dữ liệu từ Activity trước đó gửi sang
        Intent intent = getIntent();
        sanId = intent.getStringExtra("sanId");
        edtTenSan.setText(intent.getStringExtra("tenSan"));
        edtGiaSan.setText(intent.getStringExtra("giaSan"));
        edtDiaChiSan.setText(intent.getStringExtra("diaChiSan"));

        btnXacNhanCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhatThongTinSan();
            }
        });
    }

    private void capNhatThongTinSan() {
        String tenSanMoi = edtTenSan.getText().toString();
        String giaSanMoi = edtGiaSan.getText().toString();
        String diaChiMoi = edtDiaChiSan.getText().toString();

        DatabaseReference sanRef = FirebaseDatabase.getInstance().getReference("SanBong").child(sanId);
        sanRef.child("tenSan").setValue(tenSanMoi);
        sanRef.child("giaSan").setValue(giaSanMoi);
        sanRef.child("diaChiSan").setValue(diaChiMoi)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CapNhatThongTinActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Đóng Activity, quay lại trang trước
                    } else {
                        Toast.makeText(CapNhatThongTinActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
