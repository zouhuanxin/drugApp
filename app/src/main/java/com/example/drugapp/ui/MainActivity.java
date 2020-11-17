package com.example.drugapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drugapp.R;
import com.example.drugapp.ui.drug.DrugFragment;
import com.example.drugapp.ui.durglist.DruglistFragment;
import com.example.drugapp.ui.guardianship.GuardianshipFragment;
import com.example.drugapp.ui.person.PersonFragment;
import com.example.drugapp.util.PermissionUtils;
import com.example.drugapp.util.ShapeUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout fram;
    private LinearLayout drug;
    private ImageView drugImage;
    private TextView drugText;
    private LinearLayout guardianship;
    private ImageView guardianshipImage;
    private TextView guardianshipText;
    private LinearLayout person;
    private ImageView personImage;
    private TextView personText;
    private LinearLayout druglist;
    private ImageView druglistImage;
    private TextView druglistText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PermissionUtils().verifyStoragePermissions(this, null);
        initView();
    }

    private void initView() {
        fram = findViewById(R.id.fram);
        drug = findViewById(R.id.drug);
        drugImage = findViewById(R.id.drug_image);
        drugText = findViewById(R.id.drug_text);
        guardianship = findViewById(R.id.guardianship);
        guardianshipImage = findViewById(R.id.guardianship_image);
        guardianshipText = findViewById(R.id.guardianship_text);
        druglist = (LinearLayout) findViewById(R.id.druglist);
        druglistImage = (ImageView) findViewById(R.id.druglist_image);
        druglistText = (TextView) findViewById(R.id.druglist_text);
        person = findViewById(R.id.person);
        personImage = findViewById(R.id.person_image);
        personText = findViewById(R.id.person_text);

        drug.setOnClickListener(this);
        guardianship.setOnClickListener(this);
        druglist.setOnClickListener(this);
        person.setOnClickListener(this);

        click(3);
        getSupportFragmentManager().beginTransaction().replace(R.id.fram, new PersonFragment()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drug:
                if (ShapeUtil.INSTANCE.getUser(this) != null) {
                    click(0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram, new DrugFragment()).commit();
                }
                break;
            case R.id.guardianship:
                if (ShapeUtil.INSTANCE.getUser(this) != null) {
                    click(1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram, new GuardianshipFragment()).commit();
                }
                break;
            case R.id.druglist:
                if (ShapeUtil.INSTANCE.getUser(this) != null) {
                    click(2);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram, new DruglistFragment()).commit();
                }
                break;
            case R.id.person:
                click(3);
                getSupportFragmentManager().beginTransaction().replace(R.id.fram, new PersonFragment()).commit();
                break;
        }
    }

    private void click(int index) {
        drugImage.setImageResource(index == 0 ? R.mipmap.tixing2 : R.mipmap.tixing1);
        guardianshipImage.setImageResource(index == 1 ? R.mipmap.jianhu2 : R.mipmap.jianhu1);
        druglistImage.setImageResource(index == 2 ? R.mipmap.list2 : R.mipmap.list1);
        personImage.setImageResource(index == 3 ? R.mipmap.geren2 : R.mipmap.geren1);
    }
}