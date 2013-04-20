package com.honkimi.hanoi;

import org.taptwo.android.widget.TitleProvider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PagerAdapter extends BaseAdapter implements TitleProvider {

        private static final int VIEW1 = 0;
        private static final int VIEW2 = 1;
        private static final int VIEW3 = 2;
        private static final int VIEW_MAX_COUNT = VIEW3 + 1;
    	private final String[] names = {"The Hanoi","Sample Source", "Info"};

    private LayoutInflater mInflater;

    public PagerAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return VIEW_MAX_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int view = getItemViewType(position);
        if (convertView == null) {
            switch (view) {
                case VIEW1:
                    convertView = mInflater.inflate(R.layout.hanoi, null);
                    break;
                case VIEW2:
                    convertView = mInflater.inflate(R.layout.source, null);
                    break;
                case VIEW3:
                    convertView = mInflater.inflate(R.layout.info, null);
                    break;
            }
        }
        return convertView;
    }



    /* (non-Javadoc)
	 * @see org.taptwo.android.widget.TitleProvider#getTitle(int)
	 */
	public String getTitle(int position) {
		return names[position];
	}

}
