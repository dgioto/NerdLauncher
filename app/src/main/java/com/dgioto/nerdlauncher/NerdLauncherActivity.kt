package com.dgioto.nerdlauncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NerdLauncherActivity : AppCompatActivity() {

    private lateinit var recycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerd_launcher)

        recycleView = findViewById(R.id.app_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this)
    }
}