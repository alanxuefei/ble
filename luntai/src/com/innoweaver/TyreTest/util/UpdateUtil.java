package com.innoweaver.TyreTest.util;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class UpdateUtil {
	private Context context;

	public UpdateUtil(Context context) {
		super();
		this.context = context;
	}

	public void update() {
		File dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			File apkFile = new File(dir, "tyre.apk");
			if (apkFile.exists()) {
				apkFile.delete();
			}
		}
		DownloadManager dm = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new Request(
				Uri.parse("http://10.8.0.211:8080/TestDownload/tyre.apk"));
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, "tyre.apk");
		request.setTitle("������̥ѹ��app");
		long downId = dm.enqueue(request);
	}

	public void install(long id) {
		File file = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!file.exists()) {
			file.mkdir();
		}
		File apkFile = new File(file, "tyre.apk");
		if (apkFile.exists()) {
			Log.e("frag", "file: " + apkFile.toString());
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(apkFile),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}

}
