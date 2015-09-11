package com.innoweaver.TyreTest.view;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.innoweaver.TyreTest.R;

public class GuidanceFragment extends DialogFragment {
	private Bitmap[] bmps;
	private int count = 5;

	private TextView guidanceTxt, pageTxt;

	private String[] guidance, pageNumber;

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
		View root = inflater.inflate(R.layout.guidance_frag, container, false);
		initData();
		setupPager(root);
		setupBtn(root);
		guidanceTxt = (TextView) root.findViewById(R.id.guidance_txt);
		pageTxt = (TextView) root.findViewById(R.id.page_txt);

		return root;
	}

	private void initData() {
		bmps = new Bitmap[count];
		for (int i = 0; i < count; i++) {
			try {
				bmps[i] = BitmapFactory.decodeStream(getActivity().getAssets().open("p" + (i + 1) + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		guidance = getResources().getStringArray(R.array.guidance);
		pageNumber = getResources().getStringArray(R.array.guidance_page_number);
	}

	private void setupPager(View root) {
		ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
		pager.setAdapter(new ImageAdapter());
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				guidanceTxt.setText(guidance[position]);
				pageTxt.setText(pageNumber[position]);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	private void setupBtn(View root) {
		Button b = (Button) root.findViewById(R.id.off_btn);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	private class ImageAdapter extends PagerAdapter {
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView img = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.pager_img, container, false);
			img.setImageBitmap(bmps[position]);
			container.addView(img);
			return img;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
