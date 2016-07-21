package com.viewpagerindicator;

/**
 * Get icon representing the page at {@code index} in the adapter.
 */
public interface IconPagerAdapter {
    int getIconResId(int index);
    // From PagerAdapter
    int getCount();
}
