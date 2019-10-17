# FolderPicker

A lightweight android library for picking folders and files

[![Bintray](https://badges.weareopensource.me/bintray/v/rbv/libs/folderpicker.svg)](https://bintray.com/rbv/libs/folderpicker)

## Dependency

Add this in your root build.gradle file

``````````````
repositories {
    google()
    jcenter()
}

dependencies {
    implementation 'su.rbv:folderpicker:1.0.0'
}  

``````````````

## Features

* Select local filesystem folder
* Select file
* Create subfolders
* Possibility to show image file previews
* View customization

## Demo

![](demo/demo_folder_picker.gif)

## Usage

#### styles.xml
`````
    <style name="SampleFolderPickerTheme" parent="FolderPickerTheme">
        <item name="android:navigationBarColor">@color/colorNavigationBar</item>
        <item name="android:windowBackground">@drawable/fp_main_gradient</item>
        <item name="android:colorBackground">@color/colorBackground</item>
        <item name="android:windowAnimationStyle">@null</item>
        <item name="android:windowActionBar">false</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:textColor">#D0D0D0</item>
        <item name="android:textColorPrimary">#009090</item>
        <item name="fp_buttons_background">@drawable/fp_button</item>
        <item name="fp_title_color">@color/colorTitle</item>
        <item name="fp_location_color">@color/colorLocation</item>
        <item name="fp_drawable_file">@drawable/fp_file</item>
        <item name="fp_drawable_folder">@drawable/fp_folder</item>
        <item name="fp_drawable_ic_action_add">@drawable/fp_ic_action_add</item>
        <item name="fp_drawable_ic_action_back">@drawable/fp_ic_action_back</item>
        <item name="fp_drawable_ic_action_cancel">@drawable/fp_ic_action_cancel</item>
        <item name="fp_drawable_ic_action_up">@drawable/fp_ic_action_up</item>
    </style>

`````

#### MainActivity.java
``````````````````````
    private void launchActivity(Intent intent) {
        Intent intent = new Intent(this, FolderPicker.class);
        intent.putExtra("title", "Choose file");
        intent.putExtra("pickFiles", true);
        intent.putExtra("showFiles", true);
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
``````````````````````

See [Example](https://github.com/rbvrbv/FolderPicker/tree/master/sample) for all of that.


## License

````````````````````
Copyright 2019 Boris Ryaposov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````````````````````````````
