package jjhama.sqliteapp;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText pstrName, pstrSurname, plngMarks, plngId;
    Button btnSave, btnViewAll, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        //initialize fields
        pstrName = (EditText)findViewById(R.id.pstrName);
        pstrSurname = (EditText)findViewById(R.id.pstrSurname);
        plngMarks = (EditText)findViewById(R.id.plngMarks);
        plngId = (EditText)findViewById(R.id.plngId);

        //initialize buttons
        btnSave = (Button) findViewById(R.id.btnSave);
        btnViewAll = (Button) findViewById(R.id.btnViewAll);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        //functions
        addData();
        viewAll();
        updateData();
        deleteData();

    }

    public void addData(){
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        myDb.insertDataPrepared(
                                pstrName.getText().toString(),
                                pstrSurname.getText().toString(),
                                plngMarks.getText().toString());
//                        boolean isInserted = myDb.insertDataPrepared(
//                                pstrName.getText().toString(),
//                                pstrSurname.getText().toString(),
//                                plngMarks.getText().toString());
//
//                        String text = "Data Inserted: Success";
//                        if(!isInserted){
//                            text = "Data Inserted: Failed";
//                        }
//                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll(){
        btnViewAll.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0){
                    //show message
                    showMessage("Error","Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Id:       " + res.getString(0) + "\n");
                    buffer.append("Name:     " + res.getString(1) + "\n");
                    buffer.append("Surname:  " + res.getString(2) + "\n");
                    buffer.append("Marks:    " + res.getString(3) + "\n\n");
                }
                //Show all data
                showMessage("Data",buffer.toString());
            }
        });
    }

    public void updateData() {
        btnUpdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                boolean isUpdated = myDb.updateData(
                        plngId.getText().toString(),
                        pstrName.getText().toString(),
                        pstrSurname.getText().toString(),
                        plngMarks.getText().toString());
                String text = "Data Update: Success";
                if(!isUpdated){
                    text = "Data Update: Failed";
                }
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteData(){
        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Integer deletedRows = myDb.deleteData(plngId.getText().toString());
                String text = "Data Deletion: Failure";
                if(deletedRows > 0){
                    text = "Data Update: Success";
                }
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
