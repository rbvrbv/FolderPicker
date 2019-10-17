package su.rbv.folderpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import folderpicker.R;

public class FolderPicker extends Activity {

    //Folders and Files have separate lists because we show all folders first then files
    ArrayList<FilePojo> foldersList;
    ArrayList<FilePojo> folderAndFileList;
    ArrayList<FilePojo> filesList;

    TextView tv_title;
    TextView tv_location;

    String location = Environment.getExternalStorageDirectory().getAbsolutePath();
    boolean pickFiles = false;
    boolean showFiles = false;
    boolean pictureFilesShowPreview = false;
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            receivedIntent = getIntent();
            // get and define customized theme
            if (receivedIntent.hasExtra("theme")) {
                setTheme(receivedIntent.getExtras().getInt("theme"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_main_layout);

        if (!isExternalStorageReadable()) {
            Toast.makeText(this, "Storage access permission not given", Toast.LENGTH_LONG).show();
            finish();
        }

        tv_title = findViewById(R.id.fp_tv_title);
        tv_location = findViewById(R.id.fp_tv_location);

        // get parameters
        try {

            if (receivedIntent.hasExtra("title")) {
                String receivedTitle = receivedIntent.getExtras().getString("title");
                if (receivedTitle != null) tv_title.setText(receivedTitle);
            }

            if (receivedIntent.hasExtra("location")) {
                String reqLocation = receivedIntent.getExtras().getString("location");
                if (reqLocation != null) {
                    File requestedFolder = new File(reqLocation);
                    if (requestedFolder.exists()) location = reqLocation;
                }
            }

            if (receivedIntent.hasExtra("pickFiles")) {
                pickFiles = receivedIntent.getExtras().getBoolean("pickFiles");
                if (pickFiles) {
                    findViewById(R.id.fp_btn_select).setVisibility(View.GONE);
                    findViewById(R.id.fp_btn_new).setVisibility(View.GONE);
                }
            }

            if (receivedIntent.hasExtra("showFiles"))
                showFiles = receivedIntent.getExtras().getBoolean("showFiles");

            if (receivedIntent.hasExtra("pictureFilesShowPreview"))
                pictureFilesShowPreview = receivedIntent.getExtras().getBoolean("pictureFilesShowPreview");


        } catch (Exception e) {
            e.printStackTrace();
        }

        loadLists(location);

    }

    /* Checks if external storage is available to at least read */
    boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    void loadLists(String location) {
        try {

            File folder = new File(location);

            if (!folder.isDirectory())
                exit();

            tv_location.setText(folder.getAbsolutePath());
            File[] files = folder.listFiles();

            foldersList = new ArrayList<>();
            filesList = new ArrayList<>();

            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), true);
                    foldersList.add(filePojo);
                } else {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), false);
                    filesList.add(filePojo);
                }
            }

            // sort & add to final List - as we show folders first add folders first to the final list
            Collections.sort(foldersList, comparatorAscending);
            folderAndFileList = new ArrayList<>();
            folderAndFileList.addAll(foldersList);

            //if we have to show files, then add files also to the final list
            if (pickFiles || showFiles) {
                Collections.sort( filesList, comparatorAscending );
                folderAndFileList.addAll(filesList);
            }

            showList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    } // load List


    Comparator<FilePojo> comparatorAscending = new Comparator<FilePojo>() {
        @Override
        public int compare(FilePojo f1, FilePojo f2) {
            return f1.getName().compareTo(f2.getName());
        }
    };


    void showList() {
        try {
            FolderAdapter FolderAdapter = new FolderAdapter(this, folderAndFileList, pictureFilesShowPreview, location);
            ListView listView = findViewById(R.id.fp_listView);
            listView.setAdapter(FolderAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    listClick(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void listClick(int position) {
        if (pickFiles && !folderAndFileList.get(position).isFolder()) {
            String data = location + File.separator + folderAndFileList.get(position).getName();
            receivedIntent.putExtra("data", data);
            setResult(RESULT_OK, receivedIntent);
            finish();
        } else {
            if (folderAndFileList.get(position).isFolder()) {
                location = location + File.separator + folderAndFileList.get(position).getName();
                loadLists(location);
            }
        }
    }

    @Override
    public void onBackPressed(){
        goBack(null);
    }

    public void goBack(View v) {
        if (location != null && !location.equals("") && !location.equals("/")) {
            int start = location.lastIndexOf('/');
            String newLocation = location.substring(0, start);
            location = newLocation;
            loadLists(location);
        } else {
            exit();
        }
    }

    public void select(View v) {
        if (pickFiles) {
            Toast.makeText(this, "You have to select a file", Toast.LENGTH_LONG).show();
        } else if (receivedIntent != null) {
            receivedIntent.putExtra("data", location);
            setResult(RESULT_OK, receivedIntent);
            finish();
        }
    }



    void exit() {
        setResult(RESULT_CANCELED, receivedIntent);
        finish();
    }

    void createNewFolder(String filename) {
        try {
            File file = new File(location + File.separator + filename);
            file.mkdirs();
            loadLists(location);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error:" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void newFolderDialog(View v) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getResources().getString(R.string.create_folder_title));

        final EditText et = new EditText(this);
        dialog.setView(et);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getText(R.string.create),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        createNewFolder(et.getText().toString());
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        dialog.show();

    }

    public void cancel(View v) {
        exit();
    }


} // class
