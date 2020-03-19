package id.ub.sch.privateassignment.vokasi024.quiz_basdat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.`object`.UserModelClass
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_register.*
import java.sql.Types.NULL

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        toLogin.setOnClickListener {
            val inte = Intent(this, Login::class.java)
            startActivity(inte)
        }
    }

    fun register(view: View) {
        val regName = regist_name!!.text.toString()
        val regEmail = regist_email!!.text.toString()
        val regPass = regist_pass!!.text.toString()
        val regValPass = regist_val_pass!!.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //validasi apakan ada field yg terkosongi
        if(regName != "" && regEmail != "" && regPass != "" && regValPass != ""){
            //validasi apakan email telah terpakai atau belum
            if (!databaseHandler!!.checkEmail(regEmail.trim())){
                //validasi apakan inputan password sesuai dengan validasi password
                if (regPass == regValPass){
                    var regist = UserModelClass(userId = NULL,
                        userName = regName.trim(),
                        userEmail = regEmail.trim(),
                        userPass = regPass.trim())

                    //store inputan register ke tabel
                    databaseHandler!!.addUser(regist)

                    Toast.makeText(applicationContext,"Register user $regName berhasil!", Toast.LENGTH_LONG).show()

                    val inte = Intent(this, Login::class.java)
                    startActivity(inte)
                } else{
                    Toast.makeText(applicationContext,"Validasi password tidak sesuai!", Toast.LENGTH_LONG).show()
                }
            } else{
                Toast.makeText(applicationContext,"Email '$regEmail' sudah terpakai", Toast.LENGTH_LONG).show()
            }
        } else{
            Toast.makeText(applicationContext,"Field tidak boleh ada yang kosong!", Toast.LENGTH_LONG).show()
        }
    }
}
