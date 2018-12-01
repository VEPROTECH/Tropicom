package com.tropi.dvjl.tropicom.Adapter.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Verbeck DEGBESSE on 30/04/2018.
 */

public class FindDay {

    private String dat1,dat2;


public FindDay()
{

}

  public String getDay(){
      Date date=new Date();
      SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
      String datj=formatter.format(date);
      return datj;
  }

  public String getDiff(String dat1, String dat2){
      SimpleDateFormat format= new SimpleDateFormat("dd-MM-yyyy");
      Date d1= null;
      Date d2= null;

      Calendar cal0=Calendar.getInstance();
      Calendar cal1=Calendar.getInstance();

      String resulta="";
      int nbre=0;
      try {
          d1 = format.parse(dat1);
          d2 = format.parse(dat2);

          cal0.setTime(d1);
          cal1.setTime(d2);


          long diff= cal1.getTimeInMillis() - cal0.getTimeInMillis();
          long nbrDay = diff/86400000;
           nbre = (int) nbrDay;

          if(nbre == 0){
              resulta = "Aujourd'hui";
          }else{
              if(nbre == 1){
                  resulta = "Hier";
              }else{
                  if(nbre == 2){
                      resulta = "Avant Hier";
                  }else{
                      if(nbre == 3){
                          resulta = "Il y a 3 jours";
                      }else {
                          if(nbre == 4){
                              resulta = "Il y a 4 jours";
                          }else{
                              if(nbre == 5){
                                  resulta = "Il y a 5 jours";
                              }else{
                                  if(nbre == 6){
                                      resulta = "Il y a 6 jours";
                                  }else{
                                      if(nbre == 7){
                                          resulta = "Il y a une semaine";
                                      }else{
                                          if(nbre > 7 && nbre <= 14){
                                              resulta = "Il y a deux semaines";
                                          }else{
                                              if(nbre > 14 && nbre <= 28){
                                                  resulta = "Il y a trois semaines";
                                              }else{
                                                  if (nbre > 28 && nbre <=31){
                                                      resulta = "Il y a 1 mois";
                                                  }else{
                                                      resulta = "Le "+dat1;
                                                  }
                                              }
                                          }
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
          }
      } catch (ParseException e) {
          e.printStackTrace();
      }

      return resulta;

  }

}
