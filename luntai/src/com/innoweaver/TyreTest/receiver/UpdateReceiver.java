package com.innoweaver.TyreTest.receiver;

import com.innoweaver.TyreTest.util.UpdateUtil;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		long downId = intent
				.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//		if (downId != -1) {
//
//		}
		new UpdateUtil(context).install(downId);
	}

}
