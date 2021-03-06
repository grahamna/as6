package sea;

import java.util.LinkedList;

import abs.Container;
import abs.TransportSection;
import local.*;

public class CabinSection extends TransportSection {

    private int rows;
    private char layChar;
    private SeaLayout layout;
    private LinkedList<Cabin> list;

    public CabinSection(String name, Ship f, int row, char c, SeatClass sc, double cost) {
        super("CabinSection", name, f, sc, cost);
        this.rows = row;
        this.layChar = c;
        this.layout = new SeaLayout(this.layChar,this.rows);
        this.sectionCapasity = layout.getList();
    }

    public int getRows() {
        return rows;
    }

    public SeaLayout getLayout() {
        return layout;
    }

    public boolean hasAvalableCabins() {
        return super.hasAvailableContainers();
    }

    public LinkedList<Cabin> getCabins() {
        return list;
    }

    public Cabin findCabin(int row, char col) {
        for (Container s : this.getSectionCapasity()) {
            Cabin fs = (Cabin) s;
            if(fs.getCol()==col && fs.getRow()==row){
                return fs;
            }
        }
        return null;
    }

    public boolean isCabinAvailable(int row, char col){
        Cabin fs = findCabin(row, col);
        if(fs==null){
            System.out.println("No Cabin found matching request.");
            return false;
        }
        else{
            return (!(fs.isBooked()));
        }
    }

    public Cabin findAsile(){
        for (Cabin fs : list) {
            if (fs.isAsile()){
                return fs;
            }
        }
        return null;
    }

    public Cabin findWindow(){
        for (Cabin fs : list) {
            if (fs.isWindow()){
                return fs;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        String res = "["+super.toString()+this.layChar+":"+this.rows+"], ";
        return res;
    }

}
