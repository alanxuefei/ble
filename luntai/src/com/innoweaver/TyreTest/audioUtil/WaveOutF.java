package com.innoweaver.TyreTest.audioUtil;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class WaveOutF {
	// private ExecutorService pool = Executors.newSingleThreadExecutor();
	private short max_iAmp = Short.MAX_VALUE;// 方波幅度<-32767 ~ 32767>
	private short min_iAmp = Short.MIN_VALUE;
	private byte[] m_date;// 得到的数据
	private int m_lenght;// 得到数据的有效长度
	private int m_iFangBo = 58;
	private int sampleRateInHz = 44100; // 采样率
	private int m_channel = AudioFormat.CHANNEL_CONFIGURATION_MONO;// 声道 ：单声道
	private int m_sampBit = AudioFormat.ENCODING_PCM_16BIT;// 采样精度 :16bit
	private AudioTrack audioTrackF;
	private short[] m_bitDateF;
	// AudioTrack创建所需的缓冲区大小
	final int bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, m_channel, m_sampBit);

	public WaveOutF() {
		System.out.println(bufferSize + "－－－－－－－－－－－－－－－");
		audioTrackF = new AudioTrack(AudioManager.STREAM_SYSTEM, sampleRateInHz, m_channel, m_sampBit, bufferSize, AudioTrack.MODE_STREAM);
		// audioTrackF.setStereoVolume(0.0f, 1.0f);// 设置左右声道音量
		// audioTrackF.play();
	}

	public void sendByteDate(byte[] byteDate, int len) {
		if (audioTrackF == null) {
			Log.i("Fangbo", " null");
		}
		this.m_date = byteDate;
		this.m_lenght = len;
		m_bitDateF = null;
		m_bitDateF = getShortDate();// audioTrack 播放时的数据

		Thread audioTrackFThread = new Thread(new AudioTrackFThread());
		// pool.execute(audioTrackThread);
		audioTrackFThread.start();
		this.m_date = null;
		this.m_lenght = 0;

	}

	// 通过byteDate转为short型 的声音数据
	private short[] getShortDate() {
		int j = 0;
		String strBinary = getstrBinary(this.m_date, this.m_lenght);
		int len = strBinary.length();
		int m_bitDateSize = len * m_iFangBo;
		short[] m_pRightDate = new short[m_bitDateSize];
		for (int i = 0; i < len; i++) {
			int ct = m_iFangBo;
			if (strBinary.charAt(i) == '1') {
				while (ct-- > 0) {
					// m_pRightDate[j++] = min_iAmp;
					m_pRightDate[j++] = max_iAmp;
				}
			} else {
				while (ct-- > 0) {
					// m_pRightDate[j++] = max_iAmp;
					m_pRightDate[j++] = min_iAmp;
				}
			}
		}
		return m_pRightDate;
	}

	// 将一个字节编码转为2进制字符串
	private String getstrBinary(byte[] date, int lenght) {
		StringBuffer strDate = new StringBuffer(lenght * 8);
		for (int i = 0; i < lenght; i++) {
			String str = Integer.toBinaryString(date[i]);
			// System.out.println("str:"+str);
			while (str.length() < 8) {
				str = '0' + str;
			}
			strDate.append(str);
		}
		Log.i("strDate: ", strDate + "");
		return strDate.toString();
	}

	private void playWaveF(short[] m_bitDateF) {
		audioTrackF.play();
		audioTrackF.write(m_bitDateF, 0, m_bitDateF.length);// 该方法是阻塞的
	}

	public void closeAudioTrack() {
		if (audioTrackF != null) {
			audioTrackF.stop();
			audioTrackF.release();
		}
	}

	class AudioTrackFThread implements Runnable {
		@Override
		public void run() {
			playWaveF(m_bitDateF);
			audioTrackF.flush();
			// Log.i("Thread","run over");
		}
	}
}
