package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import live.ditto.DittoDocument
import live.ditto.DittoLiveQueryMove

class TasksAdapter: RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    private val tasks = mutableListOf<DittoDocument>()

    var onItemClick: ((DittoDocument) -> Unit)? = null

    class TaskViewHolder(v: View): RecyclerView.ViewHolder(v){
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val taskTextView: TextView = itemView.findViewById(R.id.taskTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTextView.text = task["body"].stringValue
        holder.taskCheckBox.isChecked = task["isCompleted"].booleanValue
        holder.itemView.setOnClickListener {
            // NOTE: Cannot use position as this is not accurate based on async updates
            onItemClick?.invoke(tasks[holder.adapterPosition])
        }
    }

    override fun getItemCount() = this.tasks.size

    fun tasks(): List<DittoDocument> {
        return this.tasks.toList()
    }

    fun set(tasks: List<DittoDocument>): Int {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        return this.tasks.size
    }

    fun inserts(indexes: List<Int>): Int {
        for (index in indexes) {
            this.notifyItemRangeInserted(index, 1)
        }
        return this.tasks.size
    }

    fun deletes(indexes: List<Int>): Int {
        for (index in indexes) {
            this.notifyItemRangeRemoved(index, 1)
        }
        return this.tasks.size
    }

    fun updates(indexes: List<Int>): Int {
        for (index in indexes) {
            this.notifyItemRangeChanged(index, 1)
        }
        return this.tasks.size
    }

    fun moves(moves: List<DittoLiveQueryMove>) {
        for (move in moves) {
            this.notifyItemMoved(move.from, move.to)
        }
    }

    fun setInitial(tasks: List<DittoDocument>): Int {
        this.tasks.addAll(tasks)
        this.notifyDataSetChanged()
        return this.tasks.size
    }
}
