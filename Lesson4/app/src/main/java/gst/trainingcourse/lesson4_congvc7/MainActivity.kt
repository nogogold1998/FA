package gst.trainingcourse.lesson4_congvc7

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val adapter = UserAdapter {
        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val users = listOf(
            User("CongVC7", "Cong", "Vu Chi", 23),
            User("NinVB", "Nin", "Vu Ba", 22),
            User("VinhNT38", "Vinh", "Nguyen Thanh", 21),
            User("ChinhLD4", "Chinh", "Le Duc", 23),
            User("LucDV1", "Luc", "Doan Van", 22),
            User("HuanNV6", "Huan", "Nguyen Van", 23),
            User("LongNT45", "Long", "Nguyen Thanh", 22),
        )

        recyclerView = findViewById(R.id.recyclerUsers)
        recyclerView.adapter = adapter
        adapter.submitList(users)
    }
}
