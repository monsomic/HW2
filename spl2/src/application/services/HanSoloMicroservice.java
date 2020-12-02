package application.services;
import MicroService;
import java.util.List;
import application.messages.AttackEvent;
import application.passiveObjects.Ewoks;
import bgu.spl.mics.MicroService;


import javax.security.auth.callback.Callback;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {

        subscribeEvent(AttackEvent.class,(AttackEvent a)->{
            List<Integer> serials= a.getSerials();
            int duration=a.getDuration();
            boolean succeed=false;
            while(!succeed){
                if(Ewoks.recruit(serials)==true)
                    succeed=true;
                else{
                    try {
                        this.wait();
                    }
                    catch (InterruptedException){};
                }
            }
            this.sleep(duration);
            Ewoks.discharge(serials);
            complete(a,true);
        });
    }
}
