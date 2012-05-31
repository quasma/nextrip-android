package com.quasma.android.bustrip.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class StopProviderContract 
{
		public static final String AUTHORITY = "com.quasma.mynextrip.provider";

		public static final class StopTable implements BaseColumns 
		{
			public static final String ROUTE 		= "route";
			public static final String DIRECTION	= "direction";
			public static final String STOP			= "stop";
			public static final String DESC  		= "desc";
			public static final String CREATED		= "timestamp";

			public static final String TABLE_NAME 	= "stop";

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
						StopTable._ID, 
						StopTable.ROUTE,
						StopTable.DIRECTION,
						StopTable.STOP,
						StopTable.DESC,
						StopTable.CREATED				
				};
				
				DISPLAY_COLUMNS = new String[] { 
						StopTable._ID, 
						StopTable.STOP, 
						StopTable.ROUTE,
						StopTable.DIRECTION,
						StopTable.DESC,
				};
			}
			

			
			// Prevent instantiation of this class
			private StopTable() {}
		}

		private StopProviderContract() {}
}
