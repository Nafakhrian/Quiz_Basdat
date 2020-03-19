package id.ub.sch.privateassignment.vokasi024.quiz_basdat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        toRegister.setOnClickListener{
            val inte = Intent(this, Register::class.java)
            startActivity(inte)
        }

    }

    fun login(view: View) {
        val logEmail = login_email!!.text.toString()
        val logPass = login_pass!!.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //validasi apakan ada field yg terkosongi
        if (logEmail.trim() != "" && logPass.trim() != ""){
            //validasi apakan email ada dalam tabel user
            if (databaseHandler!!.checkEmail(logEmail.trim { it <= ' ' })) {
                //validasi apakan password sesuai dengan email
                if (databaseHandler!!.checkLogin(logEmail.trim { it <= ' ' }, logPass.trim { it <= ' ' })) {
                    val inte = Intent(this, MainActivity::class.java)
                    inte.putExtra("EMAIL", logEmail.trim { it <= ' ' })
                    login_email.setText(null)
                    login_pass.setText(null)
                    startActivity(inte)

                } else {
                    Toast.makeText(applicationContext, "Password salah", Toast.LENGTH_LONG).show()
                }

            } else{
                Toast.makeText(applicationContext, "Email salah", Toast.LENGTH_LONG).show()
            }

        } else{
            Toast.makeText(applicationContext,"Email dan password tidak boleh kosong!", Toast.LENGTH_LONG).show()
        }
    }
}
