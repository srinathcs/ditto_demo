package com.example.test


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class NewTaskDialogFragment: DialogFragment() {

    interface NewTaskDialogListener {
        fun onDialogSave(dialog: DialogFragment, task: String)
        fun onDialogCancel(dialog: DialogFragment)
    }

    var newTaskDialogListener: NewTaskDialogListener? = null

    companion object {
        fun newInstance(title: Int): NewTaskDialogFragment {
            val newTaskDialogFragment = NewTaskDialogFragment()
            val args = Bundle()
            args.putInt("dialog_title", title)
            newTaskDialogFragment.arguments = args
            return newTaskDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { // 5
        val title = requireArguments().getInt("dialog_title")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)

        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_new_task, null)
        val task = dialogView.findViewById<TextView>(R.id.editText)

        builder.setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ -> newTaskDialogListener?.onDialogSave(this, task.text.toString()) }
            .setNegativeButton(android.R.string.cancel) { _, _ -> newTaskDialogListener?.onDialogCancel(this) }
        return builder.create()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) { // 6
        super.onAttach(activity)
        try {
            newTaskDialogListener = activity as NewTaskDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement NewTaskDialogListener")
        }
    }
}
