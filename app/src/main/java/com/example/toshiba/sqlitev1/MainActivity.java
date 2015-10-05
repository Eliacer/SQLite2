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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    String id;
    Sqlite BD;
    EditText campo1,campo2;
    Button btn;
    ListView lista;
    String [] data;
    String [] id_data;
    Context contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BD = new   Sqlite(this,"BDusuario",null,1);//ABRIMOS LA CONEXION A NUESTRA BASE DE DATOS
        campo1=(EditText) findViewById(R.id.usuario);
        campo2=(EditText) findViewById(R.id.clave);

        lista=(ListView) findViewById(R.id.lista);
        btn=(Button)findViewById(R.id.btn);
        read_us();

        //Nos aseguramos que la funcion del boton...
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn.getText().toString().equals("Registrar")){
                    insert_us();
                }
                else if(btn.getText().toString().equals("Actualizar")){
                    Actualizar();
                }
            }
        });

        //Acciones a realizar cuando hacemos click en la seleccion de la lista...
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                /*
                //Creamos un cuadro de dialogo en el cual pregunta si desea actualizar o eliminar...
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contexto);
                alertDialogBuilder.setMessage("¿Que desea realizar con: " + data[position] + "?")
                        .setCancelable(false)
                        //Si es positivo vamos a Eliminar...
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminar(id_data[position]);
                                Toast.makeText(getApplicationContext(), "Se elimino!!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //Si es negativo vamos a editar...
                        .setNegativeButton("Actualizar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Llama a la funcion editar enviandole en Id para que haga la consulta respectiva...
                                editar(id_data[position]);
                            }
                        });
                alertDialogBuilder.create().show();*/

                //Creamos un cuadro de dialogo en el cual pregunta si desea actualizar o eliminar...
                AlertDialog.Builder Builder = new AlertDialog.Builder(contexto);
                Builder.setMessage("¿Que desea realizar con: " + data[position] + "?")
                        .setItems(R.array.Opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which+1){
                                    case 1:
                                        break;
                                    case 2:break;
                                }
                            }
                        });
                Builder.create().show();


            }
        });
    }

    public void insert_us(){
        if(BD.getWritableDatabase()!=null){
            BD.getWritableDatabase().execSQL("INSERT INTO usuario(campo1,campo2) VALUES('" + campo1.getText().toString() + "','" + campo2.getText().toString() + "')");
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

     public void eliminar(String id){
         BD.getWritableDatabase().delete("usuario","id_usuario="+id,null);
         BD.close();
         read_us();
     }

    public void editar (String id_usuario){
        id = id_usuario;
        Buscar();
    }
    public void Actualizar(){

        if (btn.getText().toString().equals("Actualizar") && !id.equals("")) {
            BD.getWritableDatabase().execSQL("update usuario set campo1='" + campo1.getText().toString() + "'," +
                    "campo2='" + campo2.getText().toString() + "' where id_usuario=" + id);
            Toast.makeText(getApplicationContext(), "Se actualizo correctamente", Toast.LENGTH_SHORT).show();
            BD.close();
        }else{

            Toast.makeText(getApplicationContext(),"No se pudo actualizar",Toast.LENGTH_SHORT).show();
        }
        btn.setText("Registrar");
        limpiar();
        read_us();
    }

    public  void limpiar(){
        campo1.setText("");
        campo2.setText("");
    }

    public void Buscar (){
        Cursor fila =BD.getWritableDatabase().rawQuery("select id_usuario,campo1,campo2 from usuario " +
                "where id_usuario="+id, null);

        if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
            id=fila.getString(0);
            campo1.setText(fila.getString(1));
            campo2.setText(fila.getString(2));
            btn.setText("Actualizar");
        } else {
            Toast.makeText(this, "No existe el usuario",
                    Toast.LENGTH_SHORT).show();
        }
        //BD.close();

        read_us();
    }
}
