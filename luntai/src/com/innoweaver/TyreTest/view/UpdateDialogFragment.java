package com.innoweaver.TyreTest.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.innoweaver.TyreTest.R;
import com.innoweaver.TyreTest.util.UpdateUtil;

public class UpdateDialogFragment extends DialogFragment {
	private ProgressBar progressBar;

	private boolean interceptted;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.download_frag, container, false);
		progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);

		String url = "http://10.8.0.211:8080/TestDownload/tyre.apk";

		// String apkPath = Environment.getExternalStoragePublicDirectory(
		// Environment.DIRECTORY_DOWNLOADS).toString();
		// apkPaht = getActivity().open
		// Log.e("frag", "apkPath: " + apkPath);
		// String name = apkPath + "/hi.mp3";
		String apkName = "hello.apk";

		/**
		 * by even
		 */
		// new DownLoadTask().execute(url, apkName);

		new UpdateUtil(getActivity()).update();

		return root;
	}

	private class DownLoadTask extends AsyncTask<String, Integer, Boolean> {
		private String apkName;

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;

			String apkUrl = params[0];
			apkName = params[1];

			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				// TODO check if the Activity is null
				File file = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				if (!file.exists()) {
					file.mkdir();
				}
				File dir = new File(file, apkName);
				Log.e("update", dir.toString());
				if (dir.exists()) {
					dir.delete();
				}
				FileOutputStream fos = new FileOutputStream(dir);

				int count = 0;
				byte[] buff = new byte[1024];

				do {
					int numRead = is.read(buff);
					count += numRead;

					int progress = (int) (((float) count / length) * 100);
					publishProgress(progress);

					if (numRead <= 0) {
						result = true;

						break;
					}
					fos.write(buff, 0, numRead);
				} while (!isCancelled());

				fos.close();
				is.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result && (!isCancelled()) && getActivity() != null) {
				Toast.makeText(getActivity(), "finished", Toast.LENGTH_LONG)
						.show();
				// File apkFile = getActivity().getFileStreamPath(apkName);
				File path = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				File apkFile = new File(path, apkName);
				Log.e("UPDATE", apkFile.toString());
				Log.e("update", (apkFile.exists()) + "");
				if (apkFile.exists()) {
					Log.e("frag", "file: " + apkFile.toString());
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(apkFile),
							"application/vnd.android.package-archive");
					getActivity().startActivity(intent);
				}

			}
		}
	}

}
