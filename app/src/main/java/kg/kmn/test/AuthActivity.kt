package kg.kmn.test

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class AuthActivity : AppCompatActivity() {

    private lateinit var mNameEd : EditText
    private lateinit var mSecondNameEd : EditText
    private lateinit var mEmailEd : EditText
    private lateinit var mSaveBtn : Button
    private lateinit var mReadBtn : Button
    private lateinit var mImageIV : ImageView
    private lateinit var mStorageRef: StorageReference

    private lateinit var mDataBase : DatabaseReference
    private lateinit var uploadUri : Uri
    private val USER_KEY = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_layout)

        mNameEd = findViewById(R.id.ed_name)
        mSecondNameEd = findViewById(R.id.ed_secondName)
        mEmailEd = findViewById(R.id.ed_email)
        mImageIV = findViewById(R.id.iv_image)
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY)
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB") // По этому имени будут сохраняться картинки под категориям

    }

    fun onClickSave(view: View) {
        uploadImage() // загрузим картинку в том случае если юзер ввел все данные
    }

    private fun saveUser(){
        var id : String = mDataBase.push().key.toString()
        var name : String = mNameEd.text.toString()
        var secName : String = mSecondNameEd.text.toString()
        var email : String = mEmailEd.text.toString()

        val user = User(id, name, secName, email, uploadUri.toString())
            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(secName) && !TextUtils.isEmpty(email)){
                mDataBase.child(id).setValue(user)
                Toast.makeText(this, "Данные записаны.", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Проверьте все ли поля записаны.", Toast.LENGTH_LONG).show()
            }
    }

    fun onClickRead(view: View) {
        val intent = Intent(this, ReadActivity::class.java)
        startActivity(intent)
    }

    fun onClickChooseImage(view: View){
        getImage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constant.REQUEST_CODE && data != null && data.data != null){
            if(resultCode == Activity.RESULT_OK){ // не было ли ошибок
                // путь картинки
                Log.d("TAG", "Image URI : ${data.data}")
                mImageIV.setImageURI(data.data)
                //uploadImage()

            }
        }
    }

    private fun uploadImage(){
        //Получить URL для загрузки
        //https://firebase.google.com/docs/storage/android/upload-files#get_a_download_url
        val bitMap : Bitmap = mImageIV.drawable.toBitmap()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream) // 100 - качество картинки
        val byteArray = byteArrayOutputStream.toByteArray()
        val myStorageRef = mStorageRef.child("${System.currentTimeMillis()}_my_image") // под категория внутри ImageDB
        val uploadTask = myStorageRef.putBytes(byteArray) // создали таск и теперь нужно его запустить
        val urlTask = uploadTask.continueWithTask{task ->
           if( !task.isSuccessful){
               task.exception?.let {
                   throw it
               }
           }
           myStorageRef.downloadUrl // returning
        }.addOnCompleteListener{task ->
           if(task.isSuccessful){
               Toast.makeText(this, "The image was uploaded successfully", Toast.LENGTH_SHORT).show()
               uploadUri = task.result!!
               saveUser() //// загрузим картинку в том случае если юзер ввел все данные
           }else{
                Log.d("TAG", "task on addOnCompleteListener failed")
           }
        }
    }

    private fun getImage(){
        val intentChooser = Intent()
        intentChooser.type = "image/*"
        intentChooser.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intentChooser, Constant.REQUEST_CODE)

    }


}