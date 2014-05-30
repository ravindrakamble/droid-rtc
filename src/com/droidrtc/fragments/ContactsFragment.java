package com.droidrtc.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.droidrtc.R;
import com.droidrtc.adapters.ContanctAdapter;
import com.droidrtc.data.ContactData;

public class ContactsFragment extends Fragment implements OnItemClickListener{
	private ListView listView;
	private List<ContactData> list = new ArrayList<ContactData>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contacts, container, false);
		listView = (ListView)rootView.findViewById(R.id.list);
		listView.setOnItemClickListener(this);

		Cursor phones = getActivity().getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {

			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			ContactData contactData = new ContactData();
			contactData.setName(name);
			contactData.setPhoneNo(phoneNumber);
			list.add(contactData);

		}
		phones.close();

		ContanctAdapter objAdapter = new ContanctAdapter(getActivity(), R.layout.contact_row, list);
		listView.setAdapter(objAdapter);

		if (null != list && list.size() != 0) {
			Collections.sort(list, new Comparator<ContactData>() {

				@Override
				public int compare(ContactData lhs, ContactData rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});

		} else {
			showToast("No Contacts Found!!!");
		}
		return rootView;
	}
	private void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	public void onItemClick(AdapterView<?> listview, View v, int position,
			long id) {
		ContactData bean = (ContactData) listview.getItemAtPosition(position);
		showCallDialog(bean.getName(), bean.getPhoneNo());
	}

	private void showCallDialog(String name, final String phoneNo) {
	}
}
