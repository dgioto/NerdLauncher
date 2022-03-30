package com.dgioto.nerdlauncher

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//page 508

private const val TAG = "NerdLauncherActivity"

class NerdLauncherActivity : AppCompatActivity() {

    private lateinit var recycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerd_launcher)

        recycleView = findViewById(R.id.app_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this)

        setupAdapter()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setupAdapter() {
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        //Получаем информацию у PackageManager
        val activities = packageManager.queryIntentActivities(startupIntent, 0)

        //Алфавитнясортировка
        activities.sortWith(Comparator {a, b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(packageManager).toString(),
                b.loadLabel(packageManager).toString()
            )
        })

        Log.i(TAG, "Found ${activities.size} activities")
        recycleView.adapter = ActivityAdapter(activities)
    }

    private class ActivityHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val nameTextView = itemView as TextView
        private lateinit var resolveInfo: ResolveInfo

        init{
            nameTextView.setOnClickListener(this)
        }

        fun bindActivity(resolveInfo: ResolveInfo){
            this.resolveInfo = resolveInfo
            val packageManager = itemView.context.packageManager
            val appName = resolveInfo.loadLabel(packageManager).toString()
            nameTextView.text = appName
        }

        //запуск выбранной активи
        override fun onClick(view: View) {
            val activityInfo = resolveInfo.activityInfo

            val intent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(
                    activityInfo.applicationInfo.packageName,
                    activityInfo.name
                )
            }

            val context  = view.context
            context.startActivity(intent)
        }
    }

    private class ActivityAdapter(val activity: List<ResolveInfo>) :
            RecyclerView.Adapter<ActivityHolder>(){

        override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ActivityHolder {
            val layoutInflater = LayoutInflater.from(container.context)
            val view = layoutInflater.inflate(
                android.R.layout.simple_expandable_list_item_1,
                container,
                false
            )
            return ActivityHolder(view)
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val resolveInfo = activity[position]
            holder.bindActivity(resolveInfo)
        }

        override fun getItemCount(): Int {
            return activity.size
        }

    }
}