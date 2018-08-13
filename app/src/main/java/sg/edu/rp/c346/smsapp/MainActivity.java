package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etTo;
    EditText etMsg;
    Button btnSend;
    Button btnMsg;
    BroadcastReceiver br = new MessageReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etMsg = findViewById(R.id.editTextMsg);
        etTo = findViewById(R.id.editTextTo);
        btnSend = findViewById(R.id.buttonSend);
        btnMsg = findViewById(R.id.buttonMsg);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br,filter);
        checkPermission();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = etTo.getText().toString();
                String[] hello = msg.split(",");
                for (int i = 0;i<hello.length;i++){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(hello[i].trim(),null,etMsg.getText().toString(),null,null);

                }
                Toast my_toast = Toast.makeText(getBaseContext(),"Message Sent",Toast.LENGTH_LONG);
                my_toast.show();
                etMsg.setText("");
            }
        });
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri smsUri = Uri.parse("sms:"+etTo.getText().toString());

                Intent intent = new Intent(Intent.ACTION_VIEW,smsUri);
                intent.putExtra("sms_body", etMsg.getText().toString());

                startActivity(intent);
            }
        });

    }
    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
    }
}
