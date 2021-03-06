/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ahs.udacity.popularmovies.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Field and table name constants for
 * {@link MovieProvider}.
 */
public class MovieContract {
    private MovieContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.ahs.udacity.popularmovies";

    /**
     * Base URI. (content://com.example.android.network.sync.basicsyncadapter)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_ENTRIES = "entries";

    /**
     * Columns supported by "entries" records.
     */
    public static class Entry implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.popularmovies.entries";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.popularmovies.entry";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        /**
         * Table name where records are stored for "entry" resources.
         */
        public static final String TABLE_NAME = "movie";
        /**
         * Atom ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_MOVIE_ENTRY_ID = "movie_id";
        /**
         * Article title
         */
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        /**
         movie poster link
         */
        public static final String COLUMN_POSTER_LINK = "poster_link";
        /**
         * movie thumbnail link
         *
         */

        public static final String COLUMN_THUMBNAIL_LINK = "thumbnail_link";
        /**
         * Movie overview
         */
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_MOVIE_POPULARITY = "popularity";

        public static final String COLUMN_MOVIE_RATING = "rating";

        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        public static final String COLUMN_YOUTUBE_VIDEO_KEY = "youtube_video_key";

        public static final String COLUMN_GENRE = "movie_genre_ids";
        public static final String COLUMN_IS_POPULAR = "is_popular";
        public static final String COLUMN_IS_TOP_RATED = "is_top_rated";
        public static final String COLUMN_IS_UPCOMING = "is_upoming";
        public static final String COLUMN_IS_NOW_PLAYING = "is_now_playing";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";

    }
}