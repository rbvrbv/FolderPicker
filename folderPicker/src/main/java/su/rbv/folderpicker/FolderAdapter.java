package su.rbv.folderpicker;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import folderpicker.R;

public class FolderAdapter extends ArrayAdapter<FilePojo> {

	private Activity context;
	private ArrayList<FilePojo> dataList;
	private boolean pictureFilesShowPreview;
	private String filePath;

	public FolderAdapter(Activity context, ArrayList<FilePojo> dataList, boolean pictureFilesShowPreview, String filePath) {

		super(context, R.layout.fp_list_row, dataList);
		this.context = context;
		this.dataList = dataList;
		this.pictureFilesShowPreview = pictureFilesShowPreview;
		this.filePath = filePath;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(R.layout.fp_list_row, parent, false);

		ImageView imageView = convertView.findViewById(R.id.fp_iv_icon);
		TextView name = convertView.findViewById(R.id.fp_tv_name);
		String fileName = dataList.get(position).getName();

		TypedValue value = new TypedValue();
		if(dataList.get(position).isFolder()) {
			getContext().getTheme().resolveAttribute(R.attr.fp_drawable_folder, value, true);
			imageView.setImageResource(value.resourceId);
		} else {
			File imgFile = new File(filePath + File.separator + fileName);
			if (pictureFilesShowPreview && isImageFile(fileName) && imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				imageView.setImageBitmap(myBitmap);
			} else {
				getContext().getTheme().resolveAttribute(R.attr.fp_drawable_file, value, true);
				imageView.setImageResource(value.resourceId);
			}
		}
		name.setText(fileName);
		return convertView;
	}

	private boolean isImageFile(String fileName) {
		final String[] imageFileExtensions =  { ".jpg", ".png", ".gif", ".jpeg" };
		for (String extension : imageFileExtensions) {
			if (fileName.toLowerCase().endsWith(extension)) return true;
		}
		return false;
	}

}