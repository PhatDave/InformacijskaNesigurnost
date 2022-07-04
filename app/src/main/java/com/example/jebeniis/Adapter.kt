package com.example.jebeniis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class Adapter(context: Context?, items: List<String>, questions: HashMap<String, String>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var inflater: LayoutInflater
    private var items: List<String>
    private var questions: HashMap<String, String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item
        holder.itemView.setOnClickListener { v: View ->
            val activity = v.context as AppCompatActivity
            val myFragment: Fragment = ItemFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, myFragment).addToBackStack(null).commit()
            /*Toast.makeText(
                v.context,
                questions[item],
                Toast.LENGTH_LONG
            ).show()*/
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView

        init {
            itemName = itemView.findViewById(R.id.itemName)
        }
    }

    init {
        inflater = LayoutInflater.from(context)
        this.items = items
        this.questions = questions
    }
}