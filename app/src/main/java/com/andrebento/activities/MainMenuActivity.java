package com.andrebento.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.andrebento.snake.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void onButtonClick(View view) {
        if (view.getId() == R.id.btn_play) {
            startActivity(new Intent(this, GameActivity.class));
        }
    }
}
