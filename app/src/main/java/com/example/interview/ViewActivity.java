package com.example.interview;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {
    private ImageView viewImage;
    private TextView viewName;
    DataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        viewImage = findViewById(R.id.viewImage);
        viewName = (TextView)findViewById(R.id.viewName);

        Bundle bundle = new Bundle();
        Intent intent= getIntent();
        String vName = intent.getStringExtra("name");
        Picasso.get().load(intent.getStringExtra("image")).into(viewImage);
        viewName.setText(vName);

    }


}
