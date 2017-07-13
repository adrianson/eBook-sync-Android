package com.skytree.simpledemo;


import com.skytree.epub.Caret;
import com.skytree.epub.Highlight;
import com.skytree.epub.HighlightListener;
import com.skytree.epub.Highlights;
import com.skytree.epub.NavPoint;
import com.skytree.epub.NavPoints;
import com.skytree.epub.PageInformation;
import com.skytree.epub.PageMovedListener;
import com.skytree.epub.PageTransition;
import com.skytree.epub.ReflowableControl;
import com.skytree.epub.SearchListener;
import com.skytree.epub.SearchResult;
import com.skytree.epub.SelectionListener;
import com.skytree.epub.Setting;
import com.skytree.epub.SkyProvider;
import com.skytree.epub.State;
import com.skytree.epub.StateListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class BookViewActivity extends Activity {
	ReflowableControl rv;
	RelativeLayout ePubView;
	Button debugButton1;
	Button markButton;

	final private String TAG = "EPub";
	Highlights highlights;
	int temp = 20;
	
	public void makeLayout() {		
		highlights = new Highlights();
		String fileName = new String();
		fileName = "Alice.epub";
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;

		ePubView = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		ePubView.setLayoutParams(rlp);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		rv = new ReflowableControl(this);
		
		String baseDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/books";
		rv.setBaseDirectory(baseDirectory);
		rv.setBookName(fileName);
		rv.setLicenseKey("0000-0000-0000-0000");
		
		rv.setDoublePagedForLandscape(true);
		rv.setFont("TimesRoman", 20);
		rv.setLineSpacing(135); // the value is supposed to be percent(%).
		rv.setHorizontalGapRatio(0.25);
		rv.setVerticalGapRatio(0.1);
		rv.setHighlightListener(new HighlightDelegate());		
		rv.setPageMovedListener(new PageMovedDelegate());
		rv.setSelectionListener(new SelectionDelegate());
		rv.setSearchListener(new SearchDelegate());
		rv.setStateListener(new StateDelegate());
		
		rv.setPageTransition(PageTransition.Slide);
		
		rv.setLayoutParams(params);
		
		SkyProvider skyProvider = new SkyProvider();
		rv.setContentProvider(skyProvider);	
		
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.width = LayoutParams.FILL_PARENT; // 400;
		params.height = LayoutParams.FILL_PARENT; // 600;
		rv.setStartPositionInBook(0);
		rv.useDOMForHighlight(false);
		rv.setNavigationAreaWidthRatio(0.4f); // both left and right side.
		ePubView.addView(rv);

		RelativeLayout.LayoutParams debugButton1Param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		debugButton1 = new Button(this);
		debugButton1.setText("Exit");
		debugButton1Param.leftMargin = (int) (20 * density);
		debugButton1Param.topMargin = (int) (5 * density);
		debugButton1Param.width = (int) (70 * density);
		debugButton1Param.height = (int) (35 * density);
		debugButton1.setLayoutParams(debugButton1Param);
		debugButton1.setId(8081);
		debugButton1.setOnClickListener(listener);
		ePubView.addView(debugButton1);

		RelativeLayout.LayoutParams markButtonParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		markButton = new Button(this);
		markButton.setText("Highlight");
		markButtonParam.leftMargin = (int) (240 * density);
		markButtonParam.topMargin = (int) (5 * density);
		markButtonParam.width = (int) (70 * density);
		markButtonParam.height = (int) (35 * density);
		markButton.setLayoutParams(markButtonParam);
		markButton.setId(8083);
		markButton.setOnClickListener(listener);
		markButton.setVisibility(View.INVISIBLE);
		ePubView.addView(markButton);

		setContentView(ePubView);
	}

	public void onCreate(Bundle savedInstanceState) {
		debug("onCreate");
		super.onCreate(savedInstanceState);
		this.makeLayout();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View arg) {
			if (arg.getId() == 8080) {
				displayNavPoints();
			} else if (arg.getId() == 8081) {
				finish();
			} else if (arg.getId() == 8082) {
				rv.debug2("5454");
			} else if (arg.getId() == 8083) {
				hideButton();
				mark();
			}
		}
	};
	
	private void displayNavPoints() {
		NavPoints nps = rv.getNavPoints();
		for (int i=0; i<nps.getSize(); i++) {
			NavPoint np = nps.getNavPoint(i);
			debug(""+i+":"+np.text);
		}
		
		// modify one NavPoint object at will
		NavPoint onp = nps.getNavPoint(1);
		onp.text = "preface - it is modified";
		
		for (int i=0; i<nps.getSize(); i++) {
			NavPoint np = nps.getNavPoint(i);
			debug(""+i+":"+np.text);
		}	
	}
	
	private void mark() {
		rv.markSelection(0x66FFFF00,"");
		
	}

	private void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	class StateDelegate implements StateListener {
		@Override
		public void onStateChanged(State arg0) {
			// TODO Auto-generated method stub
			
		}		
	}

	class HighlightDelegate implements HighlightListener {
		@Override
		public Highlights getHighlightsForChapter(int chapterIndex) {
			// TODO Auto-generated method stub
			Highlights results = new Highlights();
			for (int index = 0; index < highlights.getSize(); index++) {
				Highlight highlight = highlights.getHighlight(index);
				if (highlight.chapterIndex == chapterIndex) {
					results.addHighlight(highlight);
				}
			}
			return results;
		}

		@Override
		public void onHighlightDeleted(Highlight highlight) {
			// TODO Auto-generated method stub
			for (int index = 0; index < highlights.getSize(); index++) {
				Highlight temp = highlights.getHighlight(index);				
				if (temp.chapterIndex == highlight.chapterIndex
						&& temp.startIndex == highlight.startIndex
						&& temp.endIndex == highlight.endIndex
						&& temp.startOffset == highlight.startOffset
						&& temp.endOffset == highlight.endOffset) {
					highlights.removeHighlight(index);
				}
			}
		}

		@Override
		public void onHighlightInserted(Highlight highlight) {
			// TODO Auto-generated method stub
			highlights.addHighlight(highlight);			
		}
		
		@Override
		public void onNoteIconHit(Highlight highlight) {
			debug(highlight.text);			
		}


		@Override
		public Bitmap getNoteIconBitmapForColor(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Rect getNoteIconRect(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onDrawCaret(Canvas arg0, Caret arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDrawHighlightRect(Canvas arg0, Highlight arg1, Rect arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onHighlightHit(Highlight arg0, int arg1, int arg2,
				Rect arg3, Rect arg4) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onHighlightUpdated(Highlight arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	class PageMovedDelegate implements PageMovedListener {
		public void onPageMoved(PageInformation pi) {

		}

		@Override
		public void onChapterLoaded(int chapterIndex) {
			// TODO Auto-generated method stub
			
		}
	}

	class SearchDelegate implements SearchListener {
		public void onKeySearched(SearchResult searchResult) {
			debug("pageIndex:" + searchResult.pageIndex + "startOffset:"
					+ searchResult.startOffset + "tag:" + searchResult.nodeName
					+ "text:" + searchResult.text);
		}

		public void onSearchFinishedForChapter(SearchResult searchResult) {
			rv.pauseSearch();
		}

		public void onSearchFinished(SearchResult searchResult) {
		}
	}

	private void moveButton(int x, int y) {
		RelativeLayout.LayoutParams markButtonParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		markButtonParam.leftMargin = x;
		markButtonParam.topMargin = y;
		markButton.setLayoutParams(markButtonParam);
	}

	private void showButton() {
		markButton.setVisibility(View.VISIBLE);
	}

	private void hideButton() {
		markButton.setVisibility(View.INVISIBLE);
		markButton.setVisibility(View.GONE);
	}

	class SelectionDelegate implements SelectionListener {
		@Override
		public void selectionChanged(Highlight highlight, Rect arg1, Rect arg2) {
			// TODO Auto-generated method stub			
			hideButton();
		}
		
		@Override
		public void selectionEnded(Highlight highlight, Rect rect1, Rect rect2) {
			// TODO Auto-generated method stub
			int startX = rect1.left;
			int startY = rect1.top;
			int endX = rect1.right;
			int endY = rect1.bottom;
			Log.w("EPub", "selectionEnded");
			if ((endY + 30 + markButton.getHeight()) < ePubView.getHeight())
				moveButton(endX, endY + 30);
			else
				moveButton(startX, startY - 30 - markButton.getHeight());
			showButton();
		}
		@Override
		public void selectionStarted(Highlight highlight, Rect arg1, Rect arg2) {
			// TODO Auto-generated method stub
			Log.w("EPub", "selectionStarted");
			hideButton();
		}

		@Override
		public void selectionCancelled() {
			// TODO Auto-generated method stub
			Log.w("EPub", "selectionCancelled");
			hideButton();			
		}
	}

	public void debug(String msg) {
		if (Setting.isDebug()) {
			Log.d(Setting.getTag(), msg);
		}
	}

}