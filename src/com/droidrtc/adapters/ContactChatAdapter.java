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
import com.droidrtc.data.AllChatData;
import com.droidrtc.data.ContactData;
import com.droidrtc.util.Fonts;

public class ContactChatAdapter extends ArrayAdapter<ContactData> {

	private List<ContactData> items;
	private int row;
	private ContactData contactData;
	private LayoutInflater inflater;

	public ContactChatAdapter(Activity act, int row, List<ContactData> items) {
		super(act, row, items);
		inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.row = row;
		this.items = items;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(row, null);
			holder = new ViewHolder();
			holder.tvname = (TextView) view.findViewById(R.id.nameID);
			holder.tvname.setTypeface(Fonts.BOOK_ANTIQUA);
			holder.tvPresence = (TextView) view.findViewById(R.id.presenceID);
			holder.tvPresence.setTypeface(Fonts.BOOK_ANTIQUA);
			holder.txtLastMessage = (TextView) view.findViewById(R.id.txt_last_message);
			holder.txtCount = (TextView) view.findViewById(R.id.txt_count);
			holder.txtLastMessage.setTypeface(Fonts.BOOK_ANTIQUA);
			holder.txtCount.setTypeface(Fonts.BOOK_ANTIQUA);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		contactData = items.get(position);

		if (holder.tvname != null && null != contactData.getName()
				&& contactData.getName().trim().length() > 0) {
			String name = contactData.getName().split("\\@")[0];

			holder.tvname.setText(name);
		}

		if (holder.tvPresence != null && null != contactData.getPresence()
				&& contactData.getPresence().toString().trim().length() > 0) {
			holder.tvPresence.setText(contactData.getPresence().toString());
		}

		if(contactData.getChatHistory() != null && contactData.getChatHistory().size() > 0){
			contactData.setLastMessage(contactData.getChatHistory().get(contactData.getChatHistory().size() - 1).getMessage());
			holder.txtLastMessage.setText(contactData.getLastMessage());
		}else{
			holder.txtLastMessage.setText("");
		}
		view.setTag(holder);

		return view;
	}

	public void refreshChatData(){
		int count = getCount();
		ContactData contact = null;
		for(int i = 0; i < count; i++){
			contact = items.get(i);
			String name = contact.getName().split("\\@")[0];
			contact.setChatHistory(AllChatData.INSTANCE.getChatHistory(name));
		}

	}
	public class ViewHolder {
		public TextView tvname, tvPresence;
		public TextView txtLastMessage;
		public TextView txtCount;
	}

}
