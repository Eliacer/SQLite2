package com.example.toshiba.sqlitev1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    Sqlite BD;
    EditText edtx1,edtx2,Usuario,Password,Buscar;
    ListView lista;
    String [] data;
    String [] id_data;
    Context contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BD = new   Sqlite(this,"BDusuario",null,1);//ABRIMOS LA CONEXION A NUESTRA BASE DE DATOS
        edtx1=(EditText) findViewById(R.id.usuario);
        edtx2=(EditText) findViewById(R.id.clave);
        Usuario=(EditText) findViewById(R.id.user);
        Password=(EditText) findViewById(R.id.pass);
        Buscar = (EditText)findViewById(R.id.consulta);
        lista=(ListView) findViewById(R.id.lista);
        read_us();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contexto);
            alertDialogBuilder.setMessage("Mensaje: ¿Desea Eliminar a: " + data[position] + "?")
                    .setCancelable(false)
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eliminar(id_data[position]);
                            Toast.makeText(getApplicationContext(), "Se elimino!!!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), "!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            }
        });
    }

    public void insert_us( View v){
        if(BD.getWritableDatabase()!=null){
            BD.getWritableDatabase().execSQL("INSERT INTO usuario(campo1,campo2) VALUES('" + edtx1.getText().toString() + "','" + edtx2.getText().toString() + "')");
            Toast.makeText(getApplicationContext(), "INSERTADO", Toast.LENGTH_SHORT).show();
            limpiar();
            read_us();
        }
    }

    public  void read_us() {

        Cursor cu = BD.getReadableDatabase().rawQuery("SELECT id_usuario,campo1||' '||campo2 FROM usuario", null);
        data = new String[cu.getCount()];
        id_data = new String[cu.getCount()];
        int n=0;
        if (cu.moveToFirst()) {
            do {
                id_data[n]=cu.getString(0).toString();
                data[n]=cu.getString(1).toString();
                n++;
            } while (cu.moveToNext());
        }
        lista.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, data));

    }

     public void eliminar(String campo){
         BD.getWritableDatabase().delete("usuario","id_usuario="+campo,null);
         BD.close();
         read_us();
     }

    public  void limpiar(){
        edtx1.setText("");
        edtx2.setText("");
    }

    public void Buscar (View v){
        Cursor fila =BD.getWritableDatabase().rawQuery("select id_usuario,campo1,campo2 from usuario " +
                "where upper(campo1)=upper('" +Buscar.getText().toString() + "')", null);

        if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
            Usuario.setText(fila.getString(1));
            Password.setText(fila.getString(2));
        } else
            Toast.makeText(this, "No existe el usuario" ,
                    Toast.LENGTH_SHORT).show();
        BD.close();

    }

}
