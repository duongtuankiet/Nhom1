package com.project.Nhom1project;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class DBHelper {
    String DATABASE_NAME = "data.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase db = null;
    Context context;
    public DBHelper(Context context){
        this.context = context;
        processSQLite();
    }
    private void processSQLite(){
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists()){
            try{
                CopyDatabaseFromAsset();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    private void CopyDatabaseFromAsset(){
        try{
            InputStream databaseInputStream = context.getAssets().open(DATABASE_NAME);
            String outputStream = getPathDatabaseSystem();
            File file = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!file.exists()){
                file.mkdir();
            }
            OutputStream databaseOutputStream = new FileOutputStream(outputStream);
            byte[] buffer = new byte[1024];
            int length;
            while(( length = databaseInputStream.read(buffer) )>0){
                databaseOutputStream.write(buffer, 0, length);
            }
            databaseOutputStream.flush();
            databaseOutputStream.close();
            databaseInputStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private String getPathDatabaseSystem(){
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
    public void insertAccount(Account account){
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", account.getUsername());
        contentValues.put("password", account.getPassword());
        db.insert("Account",null,contentValues);
    }
    public boolean getAccount(String username, String password){
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null);
        String sql = "SELECT * FROM Account";
        boolean a = false;
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String uname = cursor.getString(1);
            String upass = cursor.getString(2);
            if(username.equals(uname) && password.equals(upass))
            {
                a = true;
            }
        }
        return a;
    }
    public ArrayList<Manga> exportManga(){
        ArrayList<Manga> arrayList;
        arrayList = new ArrayList<>();
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        String sql = "SELECT * FROM Manga";
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            String pic = cursor.getString(2);
            String s = pic.replace("R.drawable.","");
            int picid = context.getResources().getIdentifier(s,"drawable",context.getPackageName());
            int rank = cursor.getInt(3);
            arrayList.add(new Manga(0,name,picid,rank));
        }
        return arrayList;
    }
    public ArrayList<Manga> exportRankedManga(int ranked){
        ArrayList<Manga> arrayList;
        arrayList = new ArrayList<>();
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        String sql = "SELECT * FROM Manga WHERE rank =  '"+ranked+"'";
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            String pic = cursor.getString(2);
            String s = pic.replace("R.drawable.","");
            int picid = context.getResources().getIdentifier(s,"drawable",context.getPackageName());
            int rank = cursor.getInt(3);
            arrayList.add(new Manga(0,name,picid,rank));
        }
        return arrayList;
    }
    public ArrayList<Manga> exportSearchManga(String named){
        ArrayList<Manga> arrayList;
        arrayList = new ArrayList<>();
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        String sql = "SELECT * FROM Manga WHERE name LIKE '%"+named+"%'";
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            String pic = cursor.getString(2);
            String s = pic.replace("R.drawable.","");
            int picid = context.getResources().getIdentifier(s,"drawable",context.getPackageName());
            int rank = cursor.getInt(3);
            arrayList.add(new Manga(0,name,picid,rank));
        }
        return arrayList;
    }
}

