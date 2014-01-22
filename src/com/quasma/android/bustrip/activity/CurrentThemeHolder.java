package com.quasma.android.bustrip.activity;

public class CurrentThemeHolder 
{
    private CurrentThemeHolder() 
    {
    }
    private static CurrentThemeHolder instance;
    
    public static CurrentThemeHolder getInstance() 
    {
        if(instance == null)
            return new CurrentThemeHolder();
        else
            return instance;
    }
    private int mTheme; //identifier of the theme
    
    public int getTheme() 
    {
        return mTheme;
    }
    public void setTheme(int newTheme)
    {
        mTheme = newTheme;
    }
}