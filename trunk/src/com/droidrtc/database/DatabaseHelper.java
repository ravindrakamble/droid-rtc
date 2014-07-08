package com.droidrtc.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.droidrtc.data.AllChatData;
import com.droidrtc.data.ChatData;
import com.droidrtc.util.RtcLogs;

public class DatabaseHelper extends SQLiteOpenHelper {

	SQLiteDatabase db;
	String TAG = "DatabaseHelper";
	private static final int DATABASE_VERSION = 3;

	private static final String DATABASE_NAME = "ChatManager";
	private static final String CHAT_TBL = "chatTable";
	private static final String CONTACT_NAME = "contactName";
	private static final String MESSAGE = "message";
	private static final String DATE = "date";
	private static final String DIRECTION = "direction";

	private static final String CREATE_CHAT_TBL = "CREATE TABLE IF NOT EXISTS " + CHAT_TBL + "(" + CONTACT_NAME + " TEXT," + MESSAGE + " TEXT," + DATE + " LONG," + DIRECTION + " INTEGER" +")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			RtcLogs.e(TAG, " Query " + CREATE_CHAT_TBL);
			// creating required table
			db.execSQL(CREATE_CHAT_TBL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			// on upgrade drop older tables
			db.execSQL("DROP TABLE IF EXISTS " + CHAT_TBL);
			// create new tables
			onCreate(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertChat(ChatData chatData) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			//		openDatabase();
			ContentValues values = new ContentValues();
			values.put(CONTACT_NAME, chatData.getContactName());
			values.put(MESSAGE, chatData.getMessage());
			values.put(DATE, chatData.getDate());
			values.put(DIRECTION, chatData.getDirection());
			// insert row
			db.insert(CHAT_TBL, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ChatData> getChatHistory(String name)
	{
		ArrayList<ChatData> chatHistoryList = new ArrayList<ChatData>();
		try {
			String chatQuery = "SELECT * FROM "
					+ CHAT_TBL
					+ " WHERE "
					+ CONTACT_NAME
					+ "="
					+ "'"
					+ name
					+ "'"
					+ "ORDER BY date asc";
			SQLiteDatabase db = this.getReadableDatabase();
			//		openDatabase();
			Cursor chatCursor = db.rawQuery(chatQuery, null);
			if (chatCursor != null) {
				while (chatCursor.moveToNext()) {
					String contactName = chatCursor.getString(chatCursor
							.getColumnIndex(CONTACT_NAME));
					long date = chatCursor.getLong(chatCursor
							.getColumnIndex(DATE));
					String message = chatCursor.getString(chatCursor
							.getColumnIndex(MESSAGE));
					int direction = chatCursor.getInt(chatCursor
							.getColumnIndex(DIRECTION));

					if (name == null)
						name = "Unknown";

					ChatData chatData = new ChatData(contactName,message,date,direction);
					chatHistoryList.add(chatData);
				}
				chatCursor.close();
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chatHistoryList;
	}

	public void cacheChatHistory(){
		try {
			String chatQuery = "SELECT * FROM "
					+ CHAT_TBL
					+ " ORDER BY date asc";
			SQLiteDatabase db = this.getReadableDatabase();
			//		openDatabase();
			Cursor chatCursor = db.rawQuery(chatQuery, null);
			if (chatCursor != null) {
				while (chatCursor.moveToNext()) {
					String contactName = chatCursor.getString(chatCursor
							.getColumnIndex(CONTACT_NAME));
					long date = chatCursor.getLong(chatCursor
							.getColumnIndex(DATE));
					String message = chatCursor.getString(chatCursor
							.getColumnIndex(MESSAGE));
					int direction = chatCursor.getInt(chatCursor
							.getColumnIndex(DIRECTION));

					ChatData chatData = new ChatData(contactName,message,date,direction);
					AllChatData.INSTANCE.addChatData(contactName, chatData);
				}
				chatCursor.close();
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteTblData()
	{
		try {
			if (db != null && db.isOpen()) {
				db.close();
			}
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM " + CHAT_TBL);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void openDatabase(){
		try {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = this.getReadableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportDB() {

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String  currentDBPath= "//data//" + "com.droidrtc"
						+ "//databases//" + "ChatManager";
				String backupDBPath  = "ChatManager.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				@SuppressWarnings("resource")
				FileChannel src = new FileInputStream(currentDB).getChannel();
				@SuppressWarnings("resource")
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
