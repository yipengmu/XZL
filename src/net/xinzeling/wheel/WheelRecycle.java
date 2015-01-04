package net.xinzeling.wheel;
import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.widget.LinearLayout;

public class WheelRecycle {
	private List<View> items;
	
	private List<View> emptyItems;
	
	private WheelView wheel;
	
	/**
	 * Constructor
	 * @param wheel the wheel view
	 */
	public WheelRecycle(WheelView wheel) {
		this.wheel = wheel;
	}

	/**
	 * Recycles items from specified layout.
	 * There are saved only items not included to specified range.
	 * All the cached items are removed from original layout.
	 * 
	 * @param layout the layout containing items to be cached
	 * @param firstItem the number of first item in layout
	 * @param range the range of current wheel items 
	 * @return the new value of first item number
	 */
	public int recycleItems(LinearLayout layout, int firstItem, ItemsRange range) {
		int index = firstItem;
		for (int i = 0; i < layout.getChildCount();) {
			if (!range.contains(index)) {
				recycleView(layout.getChildAt(i), index);
				layout.removeViewAt(i);
				if (i == 0) { // first item
					firstItem++;
				}
			} else {
				i++; // go to next item
			}
			index++;
		}
		return firstItem;
	}
	
	/**
	 * Gets item view
	 */
	public View getItem() {
		return getCachedView(items);
	}

	/**
	 * Gets empty item view
	 */
	public View getEmptyItem() {
		return getCachedView(emptyItems);
	}
	
	/**
	 * Clears all views 
	 */
	public void clearAll() {
		if (items != null) {
			items.clear();
		}
		if (emptyItems != null) {
			emptyItems.clear();
		}
	}

	/**
	 * Adds view to specified cache. Creates a cache list if it is null.
	 */
	private List<View> addView(View view, List<View> cache) {
		if (cache == null) {
			cache = new LinkedList<View>();
		}
		cache.add(view);
		return cache;
	}

	/**
	 * Adds view to cache. Determines view type (item view or empty one) by index.
	 */
	private void recycleView(View view, int index) {
		int count = wheel.getViewAdapter().getItemsCount();

		if ((index < 0 || index >= count) && !wheel.isCyclic()) {
			emptyItems = addView(view, emptyItems);
		} else {
			while (index < 0) {
				index = count + index;
			}
			index %= count;
			items = addView(view, items);
		}
	}
	
	/**
	 * Gets view from specified cache.
	 */
	private View getCachedView(List<View> cache) {
		if (cache != null && cache.size() > 0) {
			View view = cache.get(0);
			cache.remove(0);
			return view;
		}
		return null;
	}
}