package com.example.lectordep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
 Button bt1,bt2,bt3,bt4,btscaner;
 TextView f1;
 EditText id2,n2,d2,c2,precio_c2,precio_v2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            btscaner = (Button) findViewById(R.id.btscaner);
            bt1 = (Button) findViewById(R.id.bt1);
            bt2 = (Button) findViewById(R.id.bt2);
            bt3 = (Button) findViewById(R.id.bt3);
            bt4 = (Button) findViewById(R.id.bt4);

            id2 = (EditText) findViewById(R.id.id2);
            n2 = (EditText) findViewById(R.id.n2);
            d2 = (EditText) findViewById(R.id.d2);
            c2 = (EditText) findViewById(R.id.c2);
            precio_c2 = (EditText) findViewById(R.id.precio_c2);
            precio_v2 = (EditText) findViewById(R.id.precio_v2);


            btscaner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    escanner();
                }
            });
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buscar(view);
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertar(view);
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eliminar(view);
                }
            });
            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizar(view);
                }
            });
        }
        public void  escanner(){
            IntentIntegrator intregador= new IntentIntegrator( MainActivity.this);
            intregador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intregador.setPrompt("81");
            intregador.setCameraId(0);
            intregador.setBeepEnabled(true);
            intregador.setBarcodeImageEnabled(true);
            intregador.initiateScan();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            IntentResult resultado=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(resultado != null){
                if(resultado.getContents()==null){
                    Toast.makeText(this,"lesctura canselar ",Toast.LENGTH_LONG).show();
                }
                else{
                    id2.setText(resultado.getContents());
                }

            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    public void insertar(View view ){

        sqllite risto=new sqllite(this,null,null,1);
        SQLiteDatabase basededatos=risto.getWritableDatabase();
        String id=id2.getText().toString();
        String nombre=n2.getText().toString();
        String descripcion=d2.getText().toString();
        String cantidad=c2.getText().toString();
        String precioc=precio_c2.getText().toString();
        String preciov=precio_v2.getText().toString();

        if(id.isEmpty() && nombre.isEmpty() && descripcion.isEmpty()&& cantidad.isEmpty()&& precioc.isEmpty()&& preciov.isEmpty()){
            Toast.makeText(MainActivity.this,"campos vacios",Toast.LENGTH_SHORT).show();
        }else{
            ContentValues r1=new ContentValues();
            r1.put("id",id);
            r1.put("nombre",nombre);
            r1.put("descripcion",descripcion);
            r1.put("cantidad",cantidad);
            r1.put("precioc",precioc);
            r1.put("preciov",preciov);
            if(basededatos!=null){
                try{
                    basededatos.insertOrThrow("produc",null,r1);
                    basededatos.close();
                    Toast.makeText(MainActivity.this,"regristo guardado",Toast.LENGTH_SHORT).show();
                }catch (SQLException e){
                    Toast.makeText(MainActivity.this,"error"+e,Toast.LENGTH_SHORT).show();
                }
                id2.setText("");
                n2.setText("");
                d2.setText("");
                c2.setText("");
                precio_c2.setText("");
                precio_v2.setText("");
            }else{
                Toast.makeText(MainActivity.this,"todos lo campos guardados corectamente",Toast.LENGTH_SHORT).show();

            }
        }
    }
    public void buscar(View view ){
        sqllite busca=new sqllite(this,null,null,1);
        SQLiteDatabase basededatos=busca.getWritableDatabase();
        String id=id2.getText().toString();
        if(!id.isEmpty()){
            try {
                Cursor fila=basededatos.rawQuery("SELECT nombre,descripcion,cantidad,precioc,preciov FROM produc WHERE id='"+id+"'",null);
                if(fila.moveToFirst()){
                    Toast.makeText(MainActivity.this,"registro encontado",Toast.LENGTH_SHORT).show();
                    n2.setText(fila.getString(0));
                    d2.setText(fila.getString(1));
                    c2.setText(fila.getString(2));
                    precio_c2.setText(fila.getString(3));
                    precio_v2.setText(fila.getString(4));
                    basededatos.close();
                }else {
                    Toast.makeText(MainActivity.this,"registro no encontado",Toast.LENGTH_SHORT).show();
                    basededatos.close();

                }
            }catch (SQLException e){
                Toast.makeText(MainActivity.this,"error"+e,Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this,"escribe el id a buscar",Toast.LENGTH_SHORT).show();
        }
    }
    public void eliminar(View view ){
        sqllite eliminar=new sqllite(this,null,null,1);
        SQLiteDatabase basededatos=eliminar.getWritableDatabase();
        String id=id2.getText().toString();
        if(!id.isEmpty()){
            int cantidad=basededatos.delete("produc","id="+id,null);
            basededatos.close();
            if(cantidad>=1){
                Toast.makeText(MainActivity.this,"se elimino corectamente",Toast.LENGTH_SHORT).show();
                id2.setText("");
                n2.setText("");
                d2.setText("");
                c2.setText("");
                precio_c2.setText("");
                precio_v2.setText("");
            }else{
                Toast.makeText(MainActivity.this,"no se elimino",Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(MainActivity.this,"escribe el id a eliminar",Toast.LENGTH_SHORT).show();
        }
    }
    public void actualizar(View view ){

        sqllite risto=new sqllite(this,null,null,1);
        SQLiteDatabase basededatos=risto.getWritableDatabase();
        String id=id2.getText().toString();
        String nombre=n2.getText().toString();
        String descripcion=d2.getText().toString();
        String cantidad=c2.getText().toString();
        String precioc=precio_c2.getText().toString();
        String preciov=precio_v2.getText().toString();

        if(id.isEmpty() && nombre.isEmpty() && descripcion.isEmpty()&& cantidad.isEmpty()&& precioc.isEmpty()&& preciov.isEmpty()){
            Toast.makeText(MainActivity.this,"campos vacios",Toast.LENGTH_SHORT).show();
        }else{
            ContentValues r1=new ContentValues();
            r1.put("id",id);
            r1.put("nombre",nombre);
            r1.put("descripcion",descripcion);
            r1.put("cantidad",cantidad);
            r1.put("precioc",precioc);
            r1.put("preciov",preciov);

            if(basededatos!=null){
                try{
                    basededatos.update("regis",r1,"id="+id,null);
                    basededatos.close();
                    Toast.makeText(MainActivity.this,"regristo actualizado",Toast.LENGTH_SHORT).show();
                }catch (SQLException e){
                    Toast.makeText(MainActivity.this,"error"+e,Toast.LENGTH_SHORT).show();
                }
                id2.setText("");
                n2.setText("");
                d2.setText("");
                c2.setText("");
                precio_c2.setText("");
                precio_v2.setText("");
            }else{
                Toast.makeText(MainActivity.this,"todos lo campos guardados corectamente",Toast.LENGTH_SHORT).show();
            }

        }
    }
}