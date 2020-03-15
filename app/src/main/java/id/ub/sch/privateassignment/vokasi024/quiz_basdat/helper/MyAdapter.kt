package id.ub.sch.privateassignment.vokasi024.quiz_basdat.helper

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.R

class MyAdapter(private val context: Activity, private val id: Array<String>,
                private val name: Array<String>, private val email: Array<String>, private val alamat: Array<String>)
                : ArrayAdapter<String>(context, R.layout.custom_list, name)  {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val idText = rowView.findViewById(R.id.textViewId) as TextView
        val nameText = rowView.findViewById(R.id.textViewName) as TextView
        val emailText = rowView.findViewById(R.id.textViewEmail) as TextView
        val alamatText = rowView.findViewById(R.id.textViewAlamat) as TextView

        idText.text = "${id[position]}"
        nameText.text = "${name[position]}"
        emailText.text = "${email[position]}"
        alamatText.text = "${alamat[position]}"
        return rowView
    }

}
