package kg.kmn.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var openFireBaseBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFireBaseBtn = findViewById(R.id.btn_openFireBaseActivity)
        Log.d("TAG", "found id")

        openFireBaseBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            Log.d("TAG", "started activity")
        })


    }
}