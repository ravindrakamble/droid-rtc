package com.droidrtc.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.data.ContactData;

public class ContactAdapter extends ArrayAdapter<ContactData> {

	private Activity activity;
	private List<ContactData> items;
	private int row;
	private ContactData contactData;

	public ContactAdapter(Activity act, int row, List<ContactData> items) {
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
		holder.tvPresence = (TextView) view.findViewById(R.id.presenceID);

		if (holder.tvname != null && null != contactData.getName()
				&& contactData.getName().trim().length() > 0) {
			String name = contactData.getName().split("\\@")[0];

			holder.tvname.setText(name);
		}
		if (holder.tvPresence != null && null != contactData.getPresence()
				&& contactData.getPresence().toString().trim().length() > 0) {
			holder.tvPresence.setText(contactData.getPresence().toString());
		}
		return view;
	}

	public class ViewHolder {
		public TextView tvname, tvPresence;
	}

}
