package com.poct.android.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class PageSeraisReport {

    public PageSeraisReport(int pageMax)
    {
        this.pageMax = pageMax;
    }

    public HashMap<String,ArrayList<SeriesReport>> map = new HashMap<String,ArrayList<SeriesReport>>();
    public int totalcount = 0;
    public int pageCount = 1;
    public int pageMax;
    public int showPage = 1;

    public void check()
    {
        ArrayList<SeriesReport> seriesReports = map.get(String.valueOf(pageCount));
        if(seriesReports == null)
        {
            pageCount--;
        }
        else
        {
            if(seriesReports.size() == 0)
            {
                map.remove(String.valueOf(pageCount));
                pageCount--;
            }
        }
        if(pageCount == 0)
        {
            showPage = 0;
        }
    }

    public ArrayList<SeriesReport> getShow() {
        if(totalcount > 0)
        {
            return map.get(String.valueOf(showPage));
        }
        else
        {
            return new ArrayList<SeriesReport>();
        }
    }

    public void setBegin() {
        if(pageCount == 0)
        {
            showPage = 0;
        }
        else
        {
            showPage = 1;
        }
    }

    public void setEnd() {
        if(pageCount == 0)
        {
            showPage = 0;
        }
        else
        {
            showPage = pageCount;
        }
    }

    public boolean setNext() {
        if(pageCount == 0)
        {
            showPage = 0;
            return false;
        }
        else
        {
            if(showPage < pageCount)
            {
                showPage++;
                return true;
            }
            else
            {
                return false;
            }

        }
    }

    public boolean setPre() {
        if(pageCount == 0)
        {
            showPage = 0;
            return false;
        }
        else
        {
            if(showPage > 1)
            {
                showPage--;
                return true;
            }
            else
            {
                return false;
            }

        }
    }
}
