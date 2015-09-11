package com.innoweaver.TyreTest.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.innoweaver.TyreTest.R;

public class PressureIntroductionFragment extends DialogFragment {
	String str = "<h2>胎压常识</h2><p>据统计，在众多的交通事故中，因轮胎爆胎引发的交通事故占20%。高速公路46%的交通事故是由于轮胎发生故障引起的，其中爆胎一项就占事故总量的70%。</p> <p>由于轮胎压力而导致的爆胎主要分为两种。一是胎压不足，二是胎压过高。 </p><p>胎压不足的情况下，轮胎与地面接触的部分会由于车身自重而受到挤压，导致轮胎侧壁发生变形，而轮胎转到离开与地面接触的位置时，会因为胎内气压发生变化而重新被拉伸。轮胎转动的时候，整个轮胎侧壁在随转动不停的重复挤压、拉伸的过程中，很容易发生爆胎。另外胎压不足时轮胎与地面的摩擦系数加大，油耗自然也加大。</p><p>当胎压过高时，会减小轮胎与地面的接触面积，而此时轮胎所承受的压力相对提高，轮胎的抓地力会受到影响，刹车距离变长，影响行车安全。另外，当车辆经过沟坎或颠簸路面时，轮胎内没有足够空间吸收震动，除了影响行驶的稳定性和乘坐舒适性外，还会加大对悬挂系统的冲击力度，爆胎的隐患由此也会相应增加。</p>";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_FRAME;
		setStyle(style, 0);
	}

	@Override
	public void onResume() {
		super.onResume();
		int width = getResources().getDimensionPixelSize(R.dimen.dilog_width);
		int height = getResources().getDimensionPixelSize(R.dimen.dilog_height);

		getDialog().getWindow().setLayout(width, height);
		getDialog().getWindow().setGravity(Gravity.CENTER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pressure_indrodution, container, false);
		TextView txt = (TextView) view.findViewById(R.id.indroduction_txt);

		txt.setText(Html.fromHtml(str));

		Button b = (Button) view.findViewById(R.id.off_btn);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		return view;
	}
}
