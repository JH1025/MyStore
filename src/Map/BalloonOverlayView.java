package Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;


import com.MyStore.R;

/**
 * A view representing a MapView marker information balloon.
 * <P>
 * This class has a number of Android resource dependencies:
 * </P>
<UL>
 * <LI>drawable/balloon_overlay_bg_selector.xml</LI>
 * <LI>drawable/balloon_overlay_close.png</LI>
 * <LI>drawable/balloon_overlay_focused.9.png</LI>
 * <LI>drawable/balloon_overlay_unfocused.9.png</LI>
 * <LI>layout/balloon_map_overlay.xml</LI>
 * </UL>
 * <P></P>
 * 
 * @author Jeff Gilfelt
 *
 */
public class BalloonOverlayView extends FrameLayout {

	private LinearLayout layout;
	private TextView title;
	private TextView snippet;
	//test!!
	private ImageView iv;

	/**
	 * Create a new BalloonOverlayView.
	 * 
	 * @param context - The activity context.
	 * @param balloonBottomOffset - The bottom padding (in pixels) to be applied
	 * when rendering this view.
	 */
	public BalloonOverlayView( Context context, int balloonBottomOffset) {

		super(context);

		setPadding(10, 0, 10, balloonBottomOffset);
		layout = new LinearLayout(context);
		layout.setVisibility(VISIBLE);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.custom_balloon_overlay, layout);
		title = (TextView) v.findViewById(R.id.balloon_item_title);
		snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
		//test!!!!!!!!!!!11
		iv = (ImageView)v.findViewById(R.id.balloon_item_image);
		iv.setScaleType(ScaleType.FIT_XY);
		


		ImageView close = (ImageView) v.findViewById(R.id.balloon_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layout.setVisibility(GONE);
			}
		});

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.NO_GRAVITY;

		addView(layout, params);

	}
	
	/**
	 * Sets the view data from a given overlay item.
	 * 
	 * @param item - The overlay item containing the relevant view data 
	 * (title and snippet). 
	 */
	public void setData(MyOverlayItem item) {
		
		
		layout.setVisibility(VISIBLE);
		//test!!!!!!!!!!!
		iv.setImageBitmap(item.getProductImage());
		
		if (item.getTitle() != null) {
			title.setVisibility(VISIBLE);
			title.setText(item.getTitle());
		} else {
			title.setVisibility(GONE);
		}
		if (item.getSnippet() != null) {
			snippet.setVisibility(VISIBLE);
			snippet.setText(item.getSnippet());
		} else {
			snippet.setVisibility(GONE);
		}
		
	}

}
