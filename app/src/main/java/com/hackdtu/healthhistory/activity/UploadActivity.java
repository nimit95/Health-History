package com.hackdtu.healthhistory.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.network.Jsonparsor;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import static com.hackdtu.healthhistory.utils.Constants.UPLOAD_URL;


public class UploadActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText title,description;
    private Button uploadButton;
    String path,name,ba1="";
    int serverResponseCode = 0;
    Jsonparsor jsonparsor;
    JSONObject json;
    private String titleValue,descriptionValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView=(ImageView)findViewById(R.id.image_to_be_uploaded);

         path=getIntent().getStringExtra("path");
         name=getIntent().getStringExtra("name");

        Picasso.with(UploadActivity.this).load(path).into(imageView);

        title=(EditText)findViewById(R.id.photo_title);
        description=(EditText)findViewById(R.id.photo_description);
        uploadButton=(Button)findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().length()>0 && description.getText().toString().length()>0)
                {
                    // Upload start
                    titleValue=title.getText().toString();
                    descriptionValue=description.getText().toString();
                    upload1(path);
                }
                else
                {
                    if(title.getText().toString().length()<=0)
                        title.setError("Please enter a valid title");
                    if(description.getText().toString().length()<=0)
                        description.setError("Please enter a valid description");
                }
            }
        });
    }
    public void upload1(String path) {
        //if (!path.isEmpty()) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

        Log.e("base64", "-----" + ba1);
        // }
        // Upload image to server

        new demo().execute();


    }
    class demo extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String boundary = "*****";
        File sourceFile = new File(path);
        URL url;
        String lineEnd = "\r\n";
        String serverResponseMessage;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String twoHyphens = "--";
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(UploadActivity.this);
            pd.setMessage("Upload Image Please Wait...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String jsonArray) {
            pd.dismiss();

            Toast.makeText(getApplicationContext(),jsonArray,Toast.LENGTH_LONG).show();
            super.onPostExecute(jsonArray);
        }

        @Override
        protected String doInBackground(String... args) {

          try {
                Log.d("Picturepathnew",path);
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(UPLOAD_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("history_pic", path);
                //conn.setRequestProperty("user_id", String.valueOf(2));
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesAvailable = fileInputStream.available();
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                 serverResponseMessage = conn.getResponseMessage();
                Log.d("JsonRES",String.valueOf(serverResponseCode));
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            HashMap<String, String> param = new HashMap<>();

            param.put("image_title",titleValue);
            //param.put("",descriptionValue);

            Log.d("Imagestaring", String.valueOf(ba1));
            json = jsonparsor.makeHttpRequest(UPLOAD_URL,"POST", param);
            String res=json.opt("error_msg").toString();
            Log.d("JsonArray", String.valueOf(json));
            return res;
        }
    }
}
