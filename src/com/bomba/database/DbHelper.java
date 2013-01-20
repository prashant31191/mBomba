package com.bomba.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.widget.Toast;

public class DbHelper {
	static String TAG = "DATABASE";
	// Creating the datamodel for our class

	// name of the database
	public static String DATABASE_NAME = "bomba_content";
	public static int DATABASE_VERSION = 14;

	// create the table names
	public static final String PLAYLIST_TABLE = "tbl_playlist";
	public static final String PLAYLIST_DATA = "tbl_playlist_data";
	public static final String TRACKS_TABLE = "tbl_tracks";
	public static final String Bomba_Artists = "tbl_artists";
	public static final String Bomba_Songs = "tbl_songs";
	public static final String Bomba_master_songs = "tbl_master";

	// name the columns for the artist tables
	public static final String ARTISTS_ID = "_id";
	public static final String ARTIST_NAME = "artist_name";
	public static final String ARTIST_AVATAR = "artist_avatar";
	public static final String ARTIST_ADD_DATE = "artist_add_date";

	// create the columns for the songs table
	public static final String SONGS_ID = "_id";
	public static final String SONGS_NAME = "songs_name";
	public static final String SONGS_ARTIST_ID = "artist_name";
	public static final String SONGS_LINK = "song_stream_link";
	public static final String SONGS_LENGTH = "song_length";
	public static final String ADD_SONG_DATE = "add_song_date";

	// create the colums for the music master table
	public static final String ITEM_ID = "_id";
	public static final String TRACK_TITLE = "track_title";
	public static final String A_LEGAL_NAME = "a_legal_name";
	public static final String A_STAGE_NAME = "a_stage_name";
	public static final String FEATURED_A = "featured_a";
	public static final String ALBUM_TITLE = "album_title";
	public static final String TRACK_NUMBER = "track_number";
	public static final String GENRE = "genre";
	public static final String CUT = "cut";
	public static final String PRODUCER = "producer";
	public static final String STUDIO = "studio";
	public static final String MANAGEMENT = "management";
	public static final String LABEL = "label";
	public static final String IMAGE_file = "image_label";
	public static final String TRACK_file = "image_file";

	// name the rows in the playlist table
	public static final String PLAYLIST_ROW_ID = "_id";
	public static final String PLAYLIST_NAME = "playlist_name";

	// name the rows in the playlist data song
	public static final String PLAYLIST_DATA_PLAYLIST_ID = "p_id";
	public static final String PLAYLIST_DATA_TRACK_ID = "t_id";
	public static final String PLAYLIST_DATA_TRACK_NAME = "playlist_name";

	// name the rows in the tracks table

	public static final String TRACKS_ID = "_id";
	public static final String TRACKS_NAME = "tbl_tracks_name";
	public static final String TRACKS_PERFORMERS = "tbl_tracks_performer";
	public static final String TRACKS_LINK = "tracks_url";

	// setting variables necessary for this database
	private databaseHelper bombaHelper;
	private final Context ourContext;
	private SQLiteDatabase bombaDatabase;

	// creating a helper for database transactions

	private static class databaseHelper extends SQLiteOpenHelper {

		public databaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		// takes in SQL statements and makes our database yaaay finally to the
		// hardcore part

		@Override
		public void onCreate(SQLiteDatabase database) {

			database.execSQL("CREATE TABLE " + PLAYLIST_DATA + " ("
					+ PLAYLIST_DATA_TRACK_ID + " TEXT NOT NULL, "
					+ PLAYLIST_DATA_PLAYLIST_ID + " TEXT NOT NULL,"
					+ PLAYLIST_DATA_TRACK_NAME + " TEXT NOT NULL);");
			database.execSQL("CREATE TABLE " + PLAYLIST_TABLE + " ("
					+ PLAYLIST_ROW_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ PLAYLIST_NAME + " TEXT NOT NULL);");
			database.execSQL("CREATE TABLE " + TRACKS_TABLE + " (" + TRACKS_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ TRACKS_LINK + " TEXT NOT NULL UNIQUE, " + TRACKS_NAME
					+ " TEXT NOT NULL," + TRACKS_PERFORMERS
					+ " TEXT NOT NULL);");
			database.execSQL("CREATE TABLE " + Bomba_Artists + " ("
					+ ARTISTS_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ ARTIST_NAME + " TEXT NOT NULL UNIQUE, " + ARTIST_AVATAR
					+ " TEXT NOT NULL, " + ARTIST_ADD_DATE
					+ " INTEGER NOT NULL);");
			database.execSQL("CREATE TABLE " + Bomba_Songs + " (" + SONGS_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ SONGS_NAME + " TEXT, " + ADD_SONG_DATE + " INTEGER, "
					+ SONGS_ARTIST_ID + " INTEGER, " + SONGS_LINK + " TEXT, "
					+ SONGS_LENGTH + " INTEGER);");
			database.execSQL("CREATE TABLE " + Bomba_master_songs + "("
					+ ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ TRACK_TITLE + " TEXT, " + A_LEGAL_NAME + " TEXT, "
					+ A_STAGE_NAME + " TEXT, " + FEATURED_A + " TEXT, "
					+ ALBUM_TITLE + " TEXT, " + TRACK_NUMBER + " INTEGER, "
					+ GENRE + " TEXT, " + CUT + " TEXT, " + PRODUCER
					+ " TEXT, " + STUDIO + " TEXT, " + MANAGEMENT + " TEXT, "
					+ LABEL + " TEXT, " + IMAGE_file + " TEXT, " + TRACK_file
					+ " TEXT);");
			Log.v(TAG, "db CREATED");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_DATA);
			db.execSQL("DROP TABLE IF EXISTS " + TRACKS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + Bomba_Songs);
			db.execSQL("DROP TABLE IF EXISTS " + Bomba_Artists);
			db.execSQL("DROP TABLE IF EXISTS " + Bomba_master_songs);
			onCreate(db);
		}

	}

	public DbHelper(Context c) {
		ourContext = c;
	}

	// constructor to open our database from another clas
	public DbHelper open() throws SQLException {

		bombaHelper = new databaseHelper(ourContext);
		bombaDatabase = bombaHelper.getWritableDatabase();
		return this;

	}

	// to close our datbase when we are done with data entry into it
	public void close() {
		bombaHelper.close();
	}

	// method to creae playlists
	public long CreatePlayList(String pname) {
		// creating name value pairs for inserting to the database
		ContentValues playlistcv = new ContentValues();
		// loading the nave value pair
		playlistcv.put(PLAYLIST_NAME, pname);
		// loading the content of the name value pair
		Log.v(TAG, "item added to database table " + pname);
		return bombaDatabase.insert(PLAYLIST_TABLE, null, playlistcv);

	}

	public long AddSongsToLocal(int song_a_id, String song_name,
			String song_link) {
		ContentValues songValues = new ContentValues();
		songValues.put(SONGS_ARTIST_ID, song_a_id);
		songValues.put(SONGS_NAME, song_name);
		songValues.put(SONGS_LINK, song_link);
		Log.v(TAG, "Value Added");
		return bombaDatabase.insert(Bomba_Songs, null, songValues);
	}

	public Cursor getSearched(String s_name) {
		String[] columns = new String[] { SONGS_ARTIST_ID, SONGS_NAME,
				SONGS_LINK, SONGS_ID };
		Cursor getSearched = bombaDatabase.query(Bomba_Songs, columns,
				SONGS_NAME + " LIKE \"%" + s_name + "%\"", null, null, null,
				null, null);
		return getSearched;
	}

	// get all the playlists
	public String[] getPlayLists() {
		String[] columns = new String[] { PLAYLIST_ROW_ID, PLAYLIST_NAME };
		Cursor c = bombaDatabase.query(PLAYLIST_TABLE, columns, null, null,
				null, null, null);

		// int ip_row = c.getColumnIndex(PLAYLIST_ROW_ID);
		int ip_name = c.getColumnIndex(PLAYLIST_NAME);
		String[] result = new String[c.getCount()];
		if (c.moveToFirst()) {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

				result[c.getPosition()] = c.getString(ip_name);

			}
		} else {
			result = new String[0];
		}
		return result;

	}

	// check if the playlist exists in tracks table
	// use a boolean value if there are
	public boolean DoesPlaylistExist(String p_name) {
		String[] listC = new String[] { PLAYLIST_DATA_PLAYLIST_ID,
				PLAYLIST_DATA_TRACK_ID, PLAYLIST_DATA_TRACK_NAME };
		Cursor p_table_c = bombaDatabase.query(PLAYLIST_DATA, listC,
				PLAYLIST_DATA_TRACK_NAME + "=\"" + p_name + "\"", null, null,
				null, null);
		Log.v(TAG, "getting if the playlist exists");
		if (p_table_c.moveToFirst()) {
			Log.v(TAG, "true");
			return true;
		}

		Log.v(TAG, "false");
		return false;
	}

	public ArrayList<HashMap<String, String>> getTracks(String p_name) {
		ArrayList<HashMap<String, String>> tracksMap = new ArrayList<HashMap<String, String>>();
		// find which playlist is being queried
		String[] listC = new String[] { PLAYLIST_ROW_ID, PLAYLIST_NAME };

		Cursor p_table_c = bombaDatabase.query(PLAYLIST_TABLE, listC,
				PLAYLIST_NAME + "=\"" + p_name + "\"", null, null, null, null);
		Log.v(TAG, "getting playlist ID");

		// id of the playlist we are looking for PLAYLIST_NAME + "=" + p_name
		String playlist_id = null;
		if (p_table_c.moveToFirst()) {
			playlist_id = p_table_c.getString(p_table_c
					.getColumnIndex(PLAYLIST_ROW_ID));
			Log.v(TAG, playlist_id);

		} else {
			tracksMap = null;
			Log.v(TAG, "no such items in the tracks table");

		}

		// get the tracks associated with this playlist
		String[] track_ids = null;
		String[] playlist_tracks_data = new String[] {
				PLAYLIST_DATA_PLAYLIST_ID, PLAYLIST_DATA_TRACK_ID };
		Cursor tracksInPL = bombaDatabase.query(TRACKS_TABLE,
				playlist_tracks_data, PLAYLIST_DATA_PLAYLIST_ID + " = "
						+ playlist_id, null, null, null, null);
		if (tracksInPL.moveToFirst()) {
			track_ids = new String[tracksInPL.getCount()];

			for (tracksInPL.moveToFirst(); !tracksInPL.isAfterLast(); tracksInPL
					.moveToNext()) {
				track_ids[tracksInPL.getPosition()] = tracksInPL
						.getString(tracksInPL
								.getColumnIndex(PLAYLIST_DATA_TRACK_ID));
				Log.v(TAG, track_ids[tracksInPL.getPosition()]);

			}
		} else {
			tracksMap = null;
		}

		// since we have a list of track ids now we can load our hashmap with
		// this tracks and their metadata
		String[] track_meta = new String[] { TRACKS_NAME, TRACKS_PERFORMERS,
				TRACKS_LINK };
		if (track_ids.length == 0) {
			tracksMap = null;
			Log.v(TAG, "no tracks for this playlist");

		} else {
			for (int i = 0; i < track_ids.length; i++) {
				Cursor track_meta_cursor = bombaDatabase.query(TRACKS_TABLE,
						track_meta, track_ids[i] + " = " + TRACKS_ID, null,
						null, null, null);
				int i_trackurl = track_meta_cursor.getColumnIndex(TRACKS_LINK);
				int i_trackname = track_meta_cursor.getColumnIndex(TRACKS_NAME);
				int i_trackper = track_meta_cursor
						.getColumnIndex(TRACKS_PERFORMERS);
				if (track_meta_cursor.moveToFirst()) {
					HashMap<String, String> metaSPec = new HashMap<String, String>();
					metaSPec.put("url", track_meta_cursor.getString(i_trackurl));
					metaSPec.put("name",
							track_meta_cursor.getString(i_trackname));
					metaSPec.put("per", track_meta_cursor.getString(i_trackper));
				}

			}

		}

		close();

		return tracksMap;

	}
}
