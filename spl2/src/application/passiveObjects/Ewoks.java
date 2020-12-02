package application.passiveObjects;


import java.util.Iterator;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private Ewok[] ewokList;
    private static Ewoks instance =null;

    private Ewoks(int num){
        ewokList= new Ewok[num+1];
        for(int i=1;i<=num;i++){
            ewokList[i]=new Ewok(i);
        }
    }

    public static Ewoks getInstance(int num) {
        if (instance == null)
            instance = new Ewoks(num);
        return instance;
    }

    public static synchronized boolean recruit(List<Integer> serials){ // returns false if cant acquire all
        boolean stop= false;
        Iterator<Integer> it=serials.listIterator();
        int curr= it.next();
        while(!stop && it.hasNext()){
            if(instance.ewokList[curr].available==false)
                stop=true;
            curr=it.next();
        }
        if(stop)
            return false;
        else {
            Iterator<Integer> it2=serials.listIterator();
            curr=it2.next();
            while (it2.hasNext()) {
                instance.ewokList[curr].acquire();
                curr=it.next();
            }
            return true;
        }
    }
    public static void discharge(List<Integer> serials){ // needs synchronized??
        Iterator<Integer> it=serials.listIterator();
        int curr= it.next();
        while(it.hasNext()){
            instance.ewokList[curr].release();
            curr= it.next();
        }
    }


}
