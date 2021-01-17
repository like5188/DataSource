package com.like.datasource.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.like.common.util.Logger
import com.like.datasource.sample.data.db.Db
import com.like.datasource.sample.paging.view.PagingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startPagingActivity(view: View) {
        startActivity(Intent(this, PagingActivity::class.java))
    }

}
