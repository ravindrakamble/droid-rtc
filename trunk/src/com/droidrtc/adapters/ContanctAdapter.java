package com.droidrtc.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.data.ContactData;

public class ContanctAdapter extends ArrayAdapter<ContactData> {

	private Activity activity;
	private List<ContactData> items;
	private int row;
	private ContactData contactData;

	public ContanctAdapter(Activity act, int row, List<ContactData> items) {
		super(act, row, items);

		this.activity = act;
		this.row = row;
		this.items = items;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		contactData = items.get(position);

		holder.tvname = (TextView) view.findViewById(R.id.nameID);
		holder.tvPhoneNo = (TextView) view.findViewById(R.id.phoneNoID);

		if (holder.tvname != null && null != contactData.getName()
				&& contactData.getName().trim().length() > 0) {
			holder.tvname.setText(Html.fromHtml(contactData.getName()));
		}
		if (holder.tvPhoneNo != null && null != contactData.getPhoneNo()
				&& contactData.getPhoneNo().trim().length() > 0) {
			holder.tvPhoneNo.setText(Html.fromHtml(contactData.getPhoneNo()));
		}
		return view;
	}

	public class ViewHolder {
		public TextView tvname, tvPhoneNo;
	}

}
