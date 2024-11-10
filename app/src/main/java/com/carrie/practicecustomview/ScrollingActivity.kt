package com.carrie.practicecustomview

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.carrie.practicecustomview.databinding.ActivityScrollingBinding
import com.carrie.practicecustomview.databinding.ContentScrollingBinding

class ScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var listView: ListView
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        list = arrayListOf(
            "Item 1",
            "Item 2",
            "Item 3",
            "Item 4",
            "Item 5",
            "Item 6",
            "Item 7",
            "Item 8",
            "Item 9",
            "Item 10"
        )

        setRecyclerView()

    }

    private fun setRecyclerView() {
        recyclerView = binding.layout.avatarRecyclerView!!
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter(this, list)
        recyclerView.adapter = adapter

    }

    private class MyAdapter(private val context: Context, private val list: ArrayList<String>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_list_logo, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = list[position] + list[position]
        }

        private class MyViewHolder(val itemView: View) : ViewHolder(itemView){
            val imageView: ImageView = itemView.findViewById(R.id.avatar_image_view)
            val textView: TextView = itemView.findViewById(R.id.logo_text)
        }
    }

    private fun setListView() {
        val adapter = ListAdapter(this, list)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(parent.context, list[position], Toast.LENGTH_SHORT).show()
        }
    }

    private class ListAdapter(private val context: Context, private val list: ArrayList<String>) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Any {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val viewHolder: ViewHolder

            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_logo, parent, false)
                viewHolder = ViewHolder(view.findViewById(R.id.avatar_image_view), view.findViewById(R.id.logo_text))
                view.tag = viewHolder  // 使用 tag 存储 viewHolder，避免重复调用 findViewById
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }

            viewHolder.textView.text = list[position]
            return view
        }

        private data class ViewHolder(val imageView: ImageView, val textView: TextView)

    }


}