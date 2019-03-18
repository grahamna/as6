package SystemManager;

import sea.*;

import java.io.PrintStream;

import abs.*;
import local.*;

public class seaSystemManager extends SystemManager {
    public Seaport searchSeaports(Seaport search) {
        return (Seaport) searchPorts(search);
    }
    public Seaport searchSeaports(String search) {
        return (Seaport) Port.searchPorts(search, myPorts);
    }

    public void addSeaport(Seaport ap) {
        myPorts.add(ap);
    }

    public Cruse searchCruse(Cruse search) {
        return (Cruse)searchCompany(search);
    }
    public Cruse searchCruse(String search) {
        return (Cruse) Company.searchCompanies(search, myCompany);
    
    }

    public void addCruse(Cruse al) {
        myCompany.add(al);
    }

    public void createNewPort(String name) {
        createPort(name,"Seaport");
    }
    

    public void createCruse(String name) {
        createCompany(name,"Cruse");
    }

    public void createShip(String aname, String orig, String dest, int year, int month, int day, int hour, int min, String id) {
        System.out.println("Attempting to create Ship "+id+".");
        int minYear = 2018;
        int maxYear = 2019;
        int maxMonth = 12;
        int minDay = 1;
        int maxDay = 31;
        int minMonth = 1;
        int minHour=1;
        int maxHour=12;
        int maxMin = 59;
        int minMin = -0;
        if(orig.equals(dest)) {
            System.out.println("Originating Seaport ("+orig+") cannot be the same as the destination Seaport.\n");
        }
        else if((day<minDay || day>maxDay)||(month<minMonth || month>maxMonth) || (year<minYear || year> maxYear) || (hour>maxHour || hour<minHour) || (min>maxMin || min<minMin)) {
            System.out.println("Invalid date: "+month+"/"+day+"/"+year+".\n");
        }
        else if(searchCruse(aname)==null) {
            System.out.println("Cruse "+aname+" doesn't exist.\n");
        }
        else if(searchSeaports(orig)==null) {
            System.out.println("Seaport "+orig+" doesn't exist.\n");
        }
        else if(searchSeaports(dest)==null) {
            System.out.println("Seaport "+dest+" doesn't exist.\n");
        }
        else {
            Cruse Cruse = searchCruse(aname);
            Seaport origin = searchSeaports(orig);
            Seaport destination = searchSeaports(dest);
            Date date = new Date(month, day, year, hour, min);
            Ship Ship = new Ship("Ship "+id, origin, destination, date, id);
            Cruse.addShip(Ship);
            System.out.println("Created Ship "+id+" from "+orig+" to "+dest+".\n");
        }
    }

    public void createSection(String alName, String flID, int rows, char cols, SeatClass s, double cost) {
        System.out.println("Attempting to create Section for Ship "+flID+".");
        Cruse Cruse = searchCruse(alName);
        int maxRowSection = 101;
        int minRowSection = 0;
        int maxColSection = 11;
        int minColSection = 0;
        if((rows> maxRowSection ||rows< minRowSection) || (cols> maxColSection ||cols< minColSection)) {
            System.out.println("Invalid number of rows/columns: "+rows+" rows, "+cols+" columns.\n");
        }
        else if(Cruse==null) {
            System.out.println("Cruse "+alName+" doesn't exist.\n");
        }
        else if(Cruse.findShipByID(flID)==null){
            System.out.println("Ship "+flID+" doesn't exist.\n");
        }
        else {
            Ship Ship = (Ship) Cruse.findShipByID(flID);
            if (Ship.findCS(Ship, s)==null){
                CabinSection fs = new CabinSection(Ship+" "+s+" class section", Ship, rows, cols, s, cost);
                Ship.addCabinSection(fs, s);
                System.out.println("Added "+fs+" to "+Ship+".\n");
            }
            else {
                System.out.println("An identical Ship section was found.\n");
            }
        }

    }

    public void findAvailableShips(String orig, String dest) {
        System.out.println("Attempting to find Ship from "+orig+" to "+dest+".");
        Seaport from = searchSeaports(orig);
        Seaport to = searchSeaports(dest);
        if (from!=null && to!=null){
            for(Company al : getCompanys()){
                ((Cruse)al).shipPathFinder(from, to);
            }
            System.out.println();
        }
        else{
            System.out.println("Unexpected error occured.\n");
        }
    }

    public void bookSeat(String air, String fl, SeatClass s, int row, char col) {
        System.out.println("Attempting to book seat "+s+" "+row+" "+col+" for Ship "+fl+".");
        Cruse Cruse = searchCruse(air);
        if(Cruse!=null){
            Ship Ship = Cruse.findShipByID(fl);
            if (Ship!=null){
                CabinSection fs = Ship.findCS(Ship, s);
                if (fs!=null && fs.hasAvalableCabins()){
                    Cabin seat = fs.findCabin(row, col);
                    if (seat!=null && fs.isCabinAvailable(row, col)){
                        seat.bookContainer();;
                        System.out.println("Booked "+fs+" Seat "+seat+" on "+ Ship+".\n");
                    }
                    else{
                        System.out.println("Seat not available.\n");
                    }
                }
                else{
                    System.out.println("Ship section not available.\n");
                }
            }
            else{
                System.out.println("Ship not available.\n");
            }
        }
        else{
            System.out.println("Cruse not available.\n");
        }
    }

    public void displaySystemDetails(PrintStream out) {
        String res1 = "[";
        for (Port p : getPorts()) {
            res1 = res1 + ((Seaport)p).toString();
        }
        res1 = res1 + "]";
        out.print(res1);
        String res2 = "{";
        for (Company al : getCompanys()) {
            res2 = res2 + ((Cruse)al).toString();
        }
        res2=res2+"}";
        out.print(res2);
    }
}
