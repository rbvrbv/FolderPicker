package su.rbv.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import su.rbv.folderpicker.FolderPicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);

    }

    public void onClick1(View V) {
        Intent intent = new Intent(this, FolderPicker.class);
        intent.putExtra("title", "Choose folder");
        intent.putExtra("pickFiles", false);
        intent.putExtra("showFiles", false);
        launchActivity(intent);
    }

    public void onClick2(View V) {
        Intent intent = new Intent(this, FolderPicker.class);
        intent.putExtra("title", "Choose folder");
        intent.putExtra("pickFiles", false);
        intent.putExtra("showFiles", true);
        launchActivity(intent);
    }

    public void onClick3(View V) {
        Intent intent = new Intent(this, FolderPicker.class);
        intent.putExtra("title", "Choose file");
        intent.putExtra("pickFiles", true);
        intent.putExtra("showFiles", true);
        launchActivity(intent);
    }


    private void launchActivity(Intent intent) {
        intent.putExtra("pictureFilesShowPreview", true);
        intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        intent.putExtra("theme", R.style.SampleFolderPickerTheme);
        startActivityForResult(intent, 7777);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7777 && resultCode == RESULT_OK && data != null) {
            Toast.makeText(getApplicationContext(),
                    "result: " + data.getStringExtra("data"), Toast.LENGTH_SHORT).show();
        }
    }

}
