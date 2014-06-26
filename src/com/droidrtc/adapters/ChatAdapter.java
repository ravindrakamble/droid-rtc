package com.droidrtc.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.data.OneComment;
import com.droidrtc.util.Fonts;

public class ChatAdapter extends ArrayAdapter<OneComment> {

	private TextView chatMsg;
	private List<OneComment> chat = new ArrayList<OneComment>();
	private LinearLayout wrapper;

	@Override
	public void add(OneComment object) {
		chat.add(object);
		super.add(object);
	}

	public ChatAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public int getCount() {
		return this.chat.size();
	}

	public OneComment getItem(int index) {
		return this.chat.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem_discuss, parent, false);
		}
		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		OneComment coment = getItem(position);
		chatMsg = (TextView) row.findViewById(R.id.comment);
		chatMsg.setText(coment.comment);
		chatMsg.setTypeface(Fonts.BOOK_ANTIQUA);
		chatMsg.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
		wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}