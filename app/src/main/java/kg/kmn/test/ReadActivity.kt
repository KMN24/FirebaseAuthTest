package kg.kmn.test

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener as ValueEventListener

private lateinit var mDataBase : DatabaseReference
private const val USER_KEY = "User"

class ReadActivity : AppCompatActivity() {

    private lateinit var mListView : ListView
    private lateinit var mArrayAdapter: ArrayAdapter<String>
    private lateinit var mArrayListData : ArrayList<String>
    private lateinit var mArrayListUser : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_layout)
        init()
        getDataFromDB()
        setOnClickItem()
    }

    private fun init(){
        mListView = findViewById(R.id.listView)
        mArrayListData = ArrayList()
        mArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mArrayListData)
        mListView.adapter = mArrayAdapter
        mArrayListUser = ArrayList()

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY)

    }

    private fun getDataFromDB(){
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // getting data from database
                // children - Это наши объекты User, и мы получаем все user ы из Firebase

                if (mArrayListData.size > 0) mArrayListData.clear()
                if (mArrayListUser.size > 0) mArrayListUser.clear()

                for( ds in dataSnapshot.children) { // children
                    val user: User? = ds.getValue(User::class.java)
                    mArrayListData.add(user?.name!!)
                    mArrayListUser.add(user)
                }
                mArrayAdapter.notifyDataSetChanged() // update array adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mDataBase.addValueEventListener(valueEventListener) // added listener to database
    }

    private fun setOnClickItem(){
        mListView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val user = mArrayListUser.get(position) // возьмем того юзера на которога нажмем
                val intent = Intent(this@ReadActivity, ShowInfoAboutUserAct::class.java)
                intent.putExtra(Constant.NAME, user.name)
                intent.putExtra(Constant.SECOND_NAME, user.secName)
                intent.putExtra(Constant.EMAIL, user.email)
                intent.putExtra(Constant.USER_IMAGE_ID, user.imageId)
                startActivity(intent)
            }
    }
}