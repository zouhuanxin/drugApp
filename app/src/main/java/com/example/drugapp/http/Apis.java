package com.example.drugapp.http;

public class Apis {
    public static final String sendSMS = "/sendSMS";
    public static final String verSMS = "/verificationSMS";
    public static final String register = "/register";
    public static final String login = "/login";
    public static final String getAllDrugData = "/getAllDrugData";
    public static final String addDrugData = "/addDrugData";
    public static final String updateDrug = "/updateDrug";
    public static final String removeDrug = "/removeDrug";
    public static final String addGuardianship = "/addGuardianship";
    public static final String removeGuardianship = "/removeGuardianship";
    public static final String ByaccountGruandianships = "/ByaccountGruandianships";
    public static final String BybeaccountGruandianships = "/BybeaccountGruandianships";
    public static final String updatePass = "/updatePass";
    public static final String updateUsername = "/updateUsername";
    public static final String updateEmail = "/updateEmail";
    public static final String getByAccountDruglist1 = "/getByAccountDruglist1"; //account
    public static final String getByIdDruglist2 = "/getByIdDruglist2"; //id
    public static final String deleteByIdDruglist1 = "/deleteByIdDruglist1"; //id
    public static final String deleteByIdDruglist2 = "/deleteByIdDruglist2"; //id
    public static final String addDruglist1 = "/addDruglist1"; //id name account
    public static final String addDruglist2 = "/addDruglist2"; //id2 drugimage drugname drugdesc drugcreatedtime
    public static final String updateDruglist1 = "/updateDruglist1";
    public static final String updateDruglist2 = "/updateDruglist2";
}
