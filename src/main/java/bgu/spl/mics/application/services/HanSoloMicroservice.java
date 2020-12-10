package main.java.bgu.spl.mics.application.services;
import java.util.List;
import main.java.bgu.spl.mics.application.messages.AttackEvent;
import main.java.bgu.spl.mics.application.messages.DestroyPlanetBroadcast;
import main.java.bgu.spl.mics.application.messages.NoMoreAttacksBroadcast;
import main.java.bgu.spl.mics.application.passiveObjects.Ewoks;
import main.java.bgu.spl.mics.MicroService;


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
    private Ewoks crew;

    public HanSoloMicroservice() {
        super("Han");
        crew = Ewoks.getInstance();
    }


    @Override
    protected void initialize() {

        subscribeEvent(AttackEvent.class, (AttackEvent a) -> {
            List<Integer> serials = a.getSerials();
            int duration = a.getDuration();
            boolean succeed = false;
            while (!succeed) {
                if (crew.recruit(serials) == true)
                    succeed = true;
                else {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                    ;
                }
            }
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            crew.discharge(serials);
            complete(a, true);
            diary.setTotalAttacks();
        });

        subscribeBroadcast(DestroyPlanetBroadcast.class, (DestroyPlanetBroadcast d) -> {
            terminate();
        });

        subscribeBroadcast(NoMoreAttacksBroadcast.class, (NoMoreAttacksBroadcast n) -> {
            diary.setHanSoloFinish(System.currentTimeMillis());
        });

    }


    protected void writeDiaryTerminate() {
        diary.setHanSoloTerminate(System.currentTimeMillis());
    }
}