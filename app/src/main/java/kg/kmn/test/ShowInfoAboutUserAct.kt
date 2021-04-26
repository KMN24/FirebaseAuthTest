package kg.kmn.test

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ShowInfoAboutUserAct : AppCompatActivity(){

    private lateinit var mNameTv: TextView
    private lateinit var mSecNameTv : TextView
    private lateinit var mEmailTv : TextView
    private lateinit var mImageFromDB : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_info_about_user_layout)
        init()
        getIntentUser()
    }

    private fun init(){
        mNameTv = findViewById(R.id.tv_name)
        mSecNameTv = findViewById(R.id.tv_secName)
        mEmailTv = findViewById(R.id.tv_email)
        mImageFromDB = findViewById(R.id.iv_fromDB)
    }

    private fun getIntentUser(){
        val intent : Intent = intent
        if(intent != null){
            Picasso.get().load("${intent.getStringExtra(Constant.USER_IMAGE_ID)}").into(mImageFromDB);
            mNameTv.text = intent.getStringExtra(Constant.NAME).toString()
            mSecNameTv.text = intent.getStringExtra(Constant.SECOND_NAME).toString()
            mEmailTv.text = intent.getStringExtra(Constant.EMAIL).toString()
        }
    }

}