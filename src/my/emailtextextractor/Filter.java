
package my.emailtextextractor;
import sun.util.calendar.BaseCalendar.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author benphillips
 */
public class Filter {
    String filterName;
    Date dateFrom;
    Date dateTo;
    String direction;
    String addresses;
    String emailFolder;
    String subjectText;
    String bodyText;
    
    public Filter(){
        
    }
    
    public Filter(String pFilterName, 
                                   Date pDateFrom,
                                   Date pDateTo,
                                   String pDirection,
                                   String pAddresses,
                                   String pEmailFolder,
                                   String pSubjectText,
                                   String pBodyText){
        
       filterName = pFilterName;
       
       dateFrom = pDateFrom;
       dateTo = pDateTo;
       
       direction = pDirection;
       
       addresses = pAddresses;
       emailFolder = pEmailFolder;
       subjectText = pSubjectText;
       bodyText = pBodyText;
    }
    
    public boolean updateFilter (String pFilterName, 
                                   Date pDateFrom,
                                   Date pDateTo,
                                   String pDirection,
                                   String pAddresses,
                                   String pEmailFolder,
                                   String pSubjectText,
                                   String pBodyText){
       if (pFilterName.length() == 0) {
           //Must have a filter name
           return false;
       } 
       filterName = pFilterName;
       
       dateFrom = pDateFrom;
       dateTo = pDateTo;
       
       if (pDirection.length() == 0){
           //Must have a direction
           return false;
       }
       direction = pDirection;
       
       addresses = pAddresses;
       emailFolder = pEmailFolder;
       subjectText = pSubjectText;
       bodyText = pBodyText;
       return true;
    }
}
