package com.likesby.bludoc.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ScreenSize
{
    public static int[]  getDimensions(Context mContext)
    {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int dimm[] = new int[2];
        dimm[0] = size.x;
        dimm[1] = size.y;
         return dimm;
    }



}
