/*
 * Copyright (C) 2012 Kieu Anh Tuan (passkey1510@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.passkey1510.openwidget.demo;

import java.util.ArrayList;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.passkey1510.openwidget.listview.RefreshAndLoadMoreListView;
import com.passkey1510.openwidget.listview.RefreshAndLoadMoreListView.OnLoadMoreListener;
import com.passkey1510.openwidget.listview.RefreshAndLoadMoreListView.OnRefreshListener;

public class RefreshAndLoadMoreDemoActivity extends Activity {
	private final static String TAG = RefreshAndLoadMoreDemoActivity.class.getSimpleName();
	private ArrayList<String> mItems;
	private RefreshAndLoadMoreListView mListView;
	private ArrayAdapter<String> mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refresh_and_load_more);

		mItems = new ArrayList<String>();
		for (int i = 1; i<10; i++) {
			mItems.add("Item " + i);
		}

		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mItems);

		mListView = (RefreshAndLoadMoreListView) findViewById(R.id.listview);
		mListView.setAdapter(mAdapter);

		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				Log.i(TAG, "Refreshing");
				new RefreshDataTask().execute();
			}
		});

		mListView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				Log.i(TAG, "Load more");
				new LoadMoreDataTask().execute();
			}
		});
	}

	private class LoadMoreDataTask extends
			AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			ArrayList<String> arrayList = new ArrayList<String>();
			for (int i = 0; i < 5; i++) {
				arrayList.add("A new list item after load more " + i);
			}
			return arrayList;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			mItems.addAll(result);
			mListView.completeLoadMore();
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	private class RefreshDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}

			return "A new list item after refresh";
		}

		@Override
		protected void onPostExecute(String result) {
			mItems.add(0, result);
			mListView.completeRefreshing();
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}
}