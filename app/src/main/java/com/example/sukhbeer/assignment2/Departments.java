package com.example.sukhbeer.assignment2;

/**
 * Created by Sukhbeer on 2015-12-01.
 */
public class Departments {
    public String Department;
    public String Commodity_family;
    public String Commodity_group;
    public String Commodity_cat;
    public String Commodity_subCat;
    public String Year;
    public String Quarter;
    public String Period;
    public String Amount;

    public Departments(String department, String commodity_family, String commodity_group, String commodity_cat, String commodity_subCat, String year, String quarter, String period, String amount) {
        Department = department;
        Commodity_family = commodity_family;
        Commodity_group = commodity_group;
        Commodity_cat = commodity_cat;
        Commodity_subCat = commodity_subCat;
        Year = year;
        Quarter = quarter;
        Period = period;
        Amount = amount;
    }
}
