package id.ub.sch.privateassignment.vokasi024.quiz_basdat

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.`object`.EmpModelClass
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.helper.MyAdapter
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Types.NULL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewRecord()

        val intentObject = intent
        val header = intentObject.getStringExtra("EMAIL")
        headerUser.text = "$header"
    }

    // fungsi untuk membaca data dari database dan menampilkannya dari listview
    private fun viewRecord() {
        // membuat instanisasi databasehandler
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        // memamnggil fungsi viewemployee dari databsehandler untuk mengambil data
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayName = Array<String>(emp.size){"null"}
        val empArrayEmail = Array<String>(emp.size){"null"}
        val empArrayAlamat = Array<String>(emp.size){"null"}
        var index = 0

        // setiap data yang didapatkan dari database akan dimasukkan ke array
        for(e in emp){
            empArrayId[index] = e.empId.toString()
            empArrayName[index] = e.empName
            empArrayEmail[index] = e.empEmail
            empArrayAlamat[index] = e.empAlamat
            index++
        }

        // membuat customadapter untuk view UI
        val myListAdapter = MyAdapter(this,empArrayId,empArrayName,empArrayEmail,empArrayAlamat)
        listView.adapter = myListAdapter

        //  Proses melempar isi listView ke dialog
        listView.setOnItemLongClickListener { parent, view, position, id ->

            //Memasukkan nilai data tiap posisi dari array ke variabel
            var x_id = empArrayId[position]
            var x_name = empArrayName[position]
            var x_email = empArrayEmail[position]
            var x_alamat = empArrayAlamat[position]

            //Melempar data melalui parameter ke fungsi actionRecord untuk ditampilkan
            actionRecord(x_id,x_name,x_email,x_alamat)
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            //Memasukkan nilai data tiap posisi dari array ke variabel
            var x_id = empArrayId[position]
            var x_name = empArrayName[position]
            var x_email = empArrayEmail[position]
            var x_alamat = empArrayAlamat[position]

            //Melempar data melalui parameter ke fungsi record untuk ditampilkan
            showDetailRecord(x_id,x_name,x_email,x_alamat)
        }

    }

    //Menampilkan dialog berisi detail record
    fun showDetailRecord(id: String, name: String, email: String, alamat: String){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.detail_dialog, null)
        dialogBuilder.setView(dialogView)

        var detId = dialogView.findViewById(R.id.detailId) as TextView
        var detName = dialogView.findViewById(R.id.detailName) as TextView
        var detEmail = dialogView.findViewById(R.id.detailEmail) as TextView
        var detAlamat = dialogView.findViewById(R.id.detailAlamat) as TextView

        detId.text = id
        detName.text = name
        detEmail.text = email
        detAlamat.text = alamat

        dialogBuilder.setTitle("Detail ")

        //Tutup dialog
        dialogBuilder.setNegativeButton("Close", DialogInterface.OnClickListener { dialog, which ->
            // tidak melakukan apa2 :)
        })

        val b = dialogBuilder.create()
        b.show()
    }

    //Menampilkan dialog berisi update dan delete
    fun actionRecord(id:String, name:String, email:String, alamat:String): Boolean{
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.action_dialog, null)
        dialogBuilder.setView(dialogView)

        var edtId = dialogView.findViewById(R.id.updateId) as EditText
        var edtName = dialogView.findViewById(R.id.updateName) as EditText
        var edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText
        var edtAlamat = dialogView.findViewById(R.id.updateAlamat) as EditText

        edtId.setText(id)
        edtName.setText(name)
        edtEmail.setText(email)
        edtAlamat.setText(alamat)

        dialogBuilder.setTitle("Record ID $id")
        dialogBuilder.setMessage("Update & Delete")

        //Proses Update data
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = edtId.text.toString()
            val updateName = edtName.text.toString()
            val updateEmail = edtEmail.text.toString()
            val updateAlamat = edtAlamat.text.toString()

            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!="" && updateAlamat.trim()!=""){

                val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail, updateAlamat))
                if(status > -1){
                    Toast.makeText(applicationContext,"data terupdate",Toast.LENGTH_LONG).show()
                    viewRecord()
                }
            }else{
                Toast.makeText(applicationContext,"id, nama, email dan alamat tidak diperbolehkan kosong",Toast.LENGTH_LONG).show()
            }

        })

        //Cancel atau tutup dialog
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            // tidak melakukan apa2 :)
        })

        //Proses Delete data
        dialogBuilder.setNeutralButton("Delete", DialogInterface.OnClickListener{_, _ ->

            val deleteId = edtId.text.toString()

            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(deleteId.trim()!=""){

                val status = databaseHandler.deleteEmployee(EmpModelClass(Integer.parseInt(deleteId),"","", ""))
                if(status > -1){
                    Toast.makeText(applicationContext,"data terhapus",Toast.LENGTH_LONG).show()
                    viewRecord()
                }
            }else{
                Toast.makeText(applicationContext,"id tidak boleh kosong",Toast.LENGTH_LONG).show()
            }
        })

        val b = dialogBuilder.create()
        b.show()

        return true
    }

    // fungsi untuk menyimpan data ke database
    fun saveRecord(view: View){
        val name = u_name.text.toString()
        val email = u_email.text.toString()
        val alamat = u_alamat.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(name.trim()!="" && email.trim()!="" && alamat.trim()!=""){
            val status = databaseHandler.addEmployee(EmpModelClass(NULL,name, email, alamat))
            if(status > -1){
                Toast.makeText(applicationContext,"record save", Toast.LENGTH_LONG).show()
                u_name.text.clear()
                u_email.text.clear()
                u_alamat.text.clear()

                viewRecord()
            }
        }else{
            Toast.makeText(applicationContext,"nama, email dan alamat tidak diperbolehkan kosong",
                Toast.LENGTH_LONG).show()
        }

    }

}
