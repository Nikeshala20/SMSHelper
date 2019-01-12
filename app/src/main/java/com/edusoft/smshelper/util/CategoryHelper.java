package com.edusoft.smshelper.util;

import android.app.Activity;

import com.edusoft.smshelper.R;

public class CategoryHelper {
    public static int getCatName(int id)
    {
        switch (id)
        {
            case 0:
                return R.string.ministers;
            case 1:
                return R.string.list_canditates;
            case 2:
                return R.string.young_summit;
            case 3:
                return R.string.women_summit;
            case 4:
                return R.string.branch_summits;
            case 5:
                return R.string.business;
            case 6:
                return R.string.three_wheel;
            case 7:
                return R.string.self_employees;
            case 8:
                return R.string.dham_teachers;
            case 9:
                return R.string.pre_school;
            case 10:
                return R.string.farmers;
            case 11:
                return R.string.agri_offices;
            case 12:
                return R.string.samurdhi;
            case 13:
                return R.string.pension;
            case 14:
                return R.string.villedge_officer;
            case 15:
                return R.string.other;
        }
        return 0;
    }

}
