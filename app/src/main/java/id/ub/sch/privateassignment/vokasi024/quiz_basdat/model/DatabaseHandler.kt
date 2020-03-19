package id.ub.sch.privateassignment.vokasi024.quiz_basdat.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.`object`.EmpModelClass
import id.ub.sch.privateassignment.vokasi024.quiz_basdat.`object`.UserModelClass

class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"

        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_ALAMAT = "alamat"

        private val TABLE_USER = "UserTable"
        private val USER_ID = "id_user"
        private val USER_NAME = "name_user"
        private val USER_EMAIL = "email_user"
        private val USER_PASS = "pass_user"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // membuat tabel karyawan dan field didalamnya
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_ALAMAT + " TEXT"
                + ");")
        db?.execSQL(CREATE_CONTACTS_TABLE)

        val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_NAME + " TEXT,"
                + USER_EMAIL + " TEXT,"
                + USER_PASS + " TEXT"
                + ");")
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS + ", " + TABLE_USER)
        onCreate(db)
    }

    // fungsi untuk validasi login apakah user yg digunakan tersedia
    fun checkLogin(email: String, password: String): Boolean{
        val db = this.readableDatabase
        val columns = arrayOf(USER_NAME)
        val selection = "$USER_EMAIL = ? AND $USER_PASS =?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_USER,
            columns, //list apa saja yg dipanggil saat select
            selection, //field yg digunakan pada kondisi where
            selectionArgs, //isian field untuk where
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0)
            return true

        return false
    }

    // fungsi untuk validasi register apakah email sudah digunakan atau belum
    fun checkEmail(email: String): Boolean{
        val db = this.readableDatabase
        val columns = arrayOf(USER_ID)
        val selection = "$USER_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.query(TABLE_USER,
            columns, //list apa saja yg ditampilkan pada select
            selection, //field yg digunakan pada kondisi where
            selectionArgs, //isian field untuk where
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0)
            return true

        return false
    }

    // fungsi untuk register data user
    fun addUser(log: UserModelClass){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USER_NAME, log.userName)
        contentValues.put(USER_EMAIL, log.userEmail)
        contentValues.put(USER_PASS, log.userPass)

        // menambahkan data pada tabel
        db.insert(TABLE_USER, null, contentValues)
        db.close()
    }

    // fungsi untuk menambahkan data karyawan
    fun addEmployee(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.empName)
        contentValues.put(KEY_EMAIL,emp.empEmail )
        contentValues.put(KEY_ALAMAT,emp.empAlamat )
        // menambahkan data pada tabel
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    // fungsi untuk menampilkan data dari tabel ke UI
    fun viewEmployee():List<EmpModelClass>{
        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var empId : Int = cursor.getInt(cursor.getColumnIndex("id"))
                var empName: String = cursor.getString(cursor.getColumnIndex("name"))
                var empEmail: String = cursor.getString(cursor.getColumnIndex("email"))
                var empAlamat: String = cursor.getString(cursor.getColumnIndex("alamat"))
                val emp= EmpModelClass(empId = empId, empName = empName, empEmail = empEmail, empAlamat = empAlamat)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    // fungsi untuk memperbarui data pegawai
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.empId)
        contentValues.put(KEY_NAME, emp.empName)
        contentValues.put(KEY_EMAIL,emp.empEmail )
        contentValues.put(KEY_ALAMAT,emp.empAlamat )

        // memperbarui data
        val success = db.update(TABLE_CONTACTS, contentValues,"id=" + emp.empId,null)

        // menutup koneksi ke database
        db.close()
        return success
    }

    // fungsi untuk menghapus data
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        // employee id dari data yang akan dihapus
        contentValues.put(KEY_ID, emp.empId)
        // query untuk menghapus ata
        val success = db.delete(TABLE_CONTACTS,"id="+emp.empId,null)

        // menutup koneksi ke database
        db.close()
        return success
    }
}