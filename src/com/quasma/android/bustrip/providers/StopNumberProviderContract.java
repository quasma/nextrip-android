package com.quasma.android.bustrip.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class StopNumberProviderContract 
{
		public static final String AUTHORITY = "com.quasma.mynextrip.provider";

		public static final class StopNumberTable implements BaseColumns 
		{
			public static final String STOPNUMBER   = "stopnumber";

			public static final String TABLE_NAME 	= "stopnumber";

			static final String SCHEME = "content://";
			public static final String URI_PREFIX = SCHEME + AUTHORITY;
			private static final String URI_PATH_ALL_ROUTES = "/" + TABLE_NAME;

			private static final String URI_PATH_ROUTE_ID = "/" + TABLE_NAME + "/";
			
			public static final Uri CONTENT_URI = Uri.parse(URI_PREFIX + URI_PATH_ALL_ROUTES);
			
			public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY
					+ URI_PATH_ALL_ROUTES);

			// content://mn.aug.restfulandroid.catpicturesprovider/comments/#
			public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY
					+ URI_PATH_ROUTE_ID + "#");

			public static final String[] ALL_COLUMNS;
			public static final String[] DISPLAY_COLUMNS;

			static {
				ALL_COLUMNS = new String[] { 
						StopNumberTable._ID, 
						StopNumberTable.STOPNUMBER,
				};
				
				DISPLAY_COLUMNS = new String[] { 
						StopNumberTable._ID, 
						StopNumberTable.STOPNUMBER,
				};
			}
			// Prevent instantiation of this class
			private StopNumberTable() {}
		}

		private StopNumberProviderContract() {}
}
