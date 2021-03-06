package com.prj666_183a06.xbudget.ExpenseRoom;

import android.util.Log;

import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ExpenseListInterface {
    private List<ExpenseObj> listE;
    private List<String> titles;

    private ExpenseObj temp;
    ExpenseViewModel vm;
    PlanViewModel pvm;

    public ExpenseListInterface(ExpenseViewModel e) {
        listE = new ArrayList<>();
        vm = e;
        listE = vm.getAll();
        titles = new ArrayList<>();
    }

    public ExpenseListInterface(ExpenseViewModel e, PlanViewModel p) {
        listE = new ArrayList<>();

        vm = e;
        pvm = p;
        listE = vm.getAll();
        titles = pvm.getTitleList();
    }

    public String print() {
        String ret = "";
        for (ExpenseObj r : listE) {
            ret = ret + r.getItem() + " " + r.getStore() + " " + r.getDate() + " " + r.getCost() + " " + r.getCategory() + " " + r.getDescription() + "\n";
        }
        return ret;
    }

    public Date convertDate(String s, Date d) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            d = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);

        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;

        return month + "/" + day + "/" + year;
    }

    public String getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);

        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;

        return month + "/" + day + "/" + year;
    }

    public double costTotal() {

        double ret = 0;

        Date currentDate = new Date();
        Date lastDate = new Date();
        Date objDate = new Date();

        currentDate = convertDate(getCurrentDate(), currentDate);
        lastDate = convertDate(getLastDay(), lastDate);


        for (ExpenseObj c : listE) {
            objDate = convertDate(c.getDate(), objDate);
            if(objDate.after(lastDate) && objDate.before(currentDate)){
                ret += c.getCost();
            }
        }

        return ret;
    }

    public double costTotalAll() {
        double ret = 0;
        for (ExpenseObj c : listE) {
            ret += c.getCost();
        }
        return ret;
    }

    public List<Float> costTotalByCategory() {
        List<Float> tempList = new ArrayList<>();
        float temp = 0;
        if (!titles.isEmpty()) {
            for (String r : titles) {
                for (ExpenseObj c : listE) {
                    if (r.equals(c.getCategory())) {
                        temp += (float) c.getCost();
                    }
                }
                tempList.add(temp);
                temp = 0;
            }
        }
        return tempList;
    }

    public HashMap<String, Float> getCostByDaily() {
        HashMap<String, Float> tempList = new HashMap<>();

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        float temp;


        for (int i = 0; i < 30; i++) {
            temp = 0;
            for (ExpenseObj c : listE) {
                if (c.getDate().equals(month + "/" + day + "/" + year)) {
                    temp += (float) c.getCost();
                }
            }

            if (day < 10) {
                tempList.put(month + "/0" + day + "/" + year, temp);
            } else {
                tempList.put(month + "/" + day + "/" + year, temp);
            }

            System.out.println(month + "/" + day + "/" + year + ": " + temp);
            if (day != 1) {
                day--;
            } else {
                if (month != 1) {
                    month--;
                    day = 31;
                } else {
                    year--;
                    month = 12;
                    day = 31;
                }
            }
        }
        return tempList;
    }
}
