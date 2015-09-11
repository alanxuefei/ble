package com.innoweaver.TyreTest.audioUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.graphics.Paint;
import android.media.AudioRecord;
import android.util.Log;
import android.view.SurfaceView;

public class ClsOscilloscope {
	private final static String TAG = ClsOscilloscope.class.getSimpleName();

	private ArrayList<short[]> inBuf = new ArrayList<short[]>();
	private boolean isRecording = false;// 线程控制标记
	public boolean run_record = false;
	static int first;
	static int count = 1;
	/**
	 * X轴缩小的比例
	 */
	public int rateX = 4;
	/**
	 * Y轴缩小的比例
	 */
	public int rateY = 4;
	/**
	 * Y轴基线
	 */
	public int baseLine = 0;

	/**
	 * to show the test result
	 * 
	 * @author dig
	 */
	private String value;

	/**
	 * 初始化
	 */
	public void initOscilloscope(int baseLine) {

		this.baseLine = baseLine;
	}

	/**
	 * 开始
	 * 
	 * @param recBufSize
	 *            AudioRecord的MinBufferSize
	 */
	public void Start(AudioRecord audioRecord, int recBufSize, SurfaceView sfv,
			Paint mPaint) {
		Log.e(TAG, "start");
		isRecording = true;
		run_record = true;
		new RecordThread(audioRecord, recBufSize).start();// ��ʼ¼���߳�
		new DrawThread(sfv, mPaint).start();// ��ʼ�����߳�
	}

	/**
	 * 停止ֹ
	 */
	public void Stop() {
		isRecording = false;
		run_record = false;
		inBuf.clear();// 清除
	}

	/**
	 * 负责从MIC保存数据到inBuf
	 * 
	 * @author GV
	 * 
	 */
	class RecordThread extends Thread {
		private int recBufSize;
		private AudioRecord audioRecord;

		public RecordThread(AudioRecord audioRecord, int recBufSize) {
			this.audioRecord = audioRecord;
			this.recBufSize = recBufSize;
		}

		public void run() {
			try {
				short[] buffer = new short[recBufSize];
				audioRecord.startRecording();// 开始录制
				while (isRecording) {
					while (run_record) {
						// 从MIC保存数据到缓冲区
						int bufferReadResult = audioRecord.read(buffer, 0,
								recBufSize);
						short[] tmpBuf = new short[bufferReadResult / rateX];
						for (int i = 0, ii = 0; i < tmpBuf.length; i++, ii = i
								* rateX) {
							tmpBuf[i] = buffer[ii];
						}
						synchronized (inBuf) {//
							inBuf.add(tmpBuf);// 添加数据
						}
					}
					Thread.sleep(10);
				}
				audioRecord.stop();
			} catch (Throwable t) {
			}
		}
	};

	/**
	 * 负责绘制inBuf中的数据
	 * 
	 * @author GV
	 * 
	 */
	class DrawThread extends Thread {
		private int oldX = 0;// 上次绘制的X坐标
		private int oldY = 0;// 上次绘制的Y坐标
		// private SurfaceView sfv;// 画板
		private int X_index = 0;// 当前画图所在屏幕X轴的坐标

		// private Paint mPaint;// 画笔

		public DrawThread(SurfaceView sfv, Paint mPaint) {
			// this.sfv = sfv;
			// this.mPaint = mPaint;
		}

		public void run() {
			while (isRecording) {
				while (run_record) {
					ArrayList<short[]> buf = new ArrayList<short[]>();
					synchronized (inBuf) {
						if (inBuf.size() == 0)
							continue;
						buf = (ArrayList<short[]>) inBuf.clone();// ����
						inBuf.clear();// ���
					}
					for (int i = 0; i < buf.size(); i++) {
						short[] tmpBuf = buf.get(i);
						// Log.e("sh", "buf size: " + buf.size());
						/**
						 * by kid
						 */
						SimpleDraw(X_index, tmpBuf, rateY, baseLine);
						// �ѻ�������ݻ�����
						if (tmpBuf == null) {
							break;
						}
						X_index = X_index + tmpBuf.length;

						// if (X_index > sfv.getWidth()) {
						// X_index = 0;
						// }

					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/**
		 * 绘制指定区域
		 * 
		 * @param start
		 *            X轴开始的位置(全屏)
		 * @param buffer
		 *            缓冲区
		 * @param rate
		 *            Y轴数据缩小的比例
		 * @param baseLine
		 *            Y轴基线
		 */
		void SimpleDraw(int start, short[] buffer, int rate, int baseLine) {
			if (start == 0)
				oldX = 0;

			int y;
			long tmp = 0;

			/**
			 * by kid
			 */
			if (buffer == null) {
				return;
			}
			for (int i = 0; i < buffer.length; i++) {// 有多少画多少
				int x = i + start;

				tmp += Math.abs(buffer[i]) * Math.abs(buffer[i]);

			}
			// Log.e("zhou", tmp + "");

			// Canvas canvas = sfv.getHolder().lockCanvas();
			// canvas.drawColor(Color.BLACK);// 清除背景
			// Paint p = new Paint();
			// p.setTextAlign(Paint.Align.CENTER);
			// p.setColor(Color.RED);
			// p.setTextSize(56.0f);
			//
			// canvas.drawText(String.valueOf(new BigDecimal(Math.sqrt(tmp
			// / buffer.length)).setScale(0, BigDecimal.ROUND_HALF_UP)),
			// 150, sfv.getHeight() / 2, p);

			/**
			 * by kid
			 */
			if (tmp == 0) {
				return;
			}
			// Log.e("SH",
			// "value"
			// + String.valueOf(new BigDecimal(Math.sqrt(tmp
			// / buffer.length)).setScale(0,
			// BigDecimal.ROUND_HALF_UP)));

			value = String.valueOf(new BigDecimal(Math
					.sqrt(tmp / buffer.length)).setScale(0,
					BigDecimal.ROUND_HALF_UP));
			 Log.e("scope", "simpleDraw value: " + value);

			// sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像

		}
	}

	public String getValue() {
		// TODO
		// Log.e("scope", "value: " + value);
		return value;
	}
}
