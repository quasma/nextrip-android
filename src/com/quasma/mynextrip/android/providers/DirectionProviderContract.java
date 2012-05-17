package com.quasma.mynextrip.android.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DirectionProviderContract 
{
		public static final String AUTHORITY = "com.quasma.mynextrip.provider";

		public static final class DirectionTable implements BaseColumns 
		{
			public static final String ROUTE 		= "route";
			public static final String DIRECTION	= "direction";
			public static final String DESC  		= "desc";
			public static final String CREATED		= "timestamp";
			public static final String URL      	= "url";

			public static final String TABLE_NAME 	= "direction";

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
						DirectionTable._ID, 
						DirectionTable.ROUTE,
						DirectionTable.DIRECTION,
						DirectionTable.DESC,
						DirectionTable.URL,
						DirectionTable.CREATED				
				};
				
				DISPLAY_COLUMNS = new String[] { 
						DirectionTable._ID, 
						DirectionTable.DIRECTION,
						DirectionTable.ROUTE,
						DirectionTable.URL,
						DirectionTable.DESC,
				};
			}
			

			
			// Prevent instantiation of this class
			private DirectionTable() {}
		}

		private DirectionProviderContract() {}
}
