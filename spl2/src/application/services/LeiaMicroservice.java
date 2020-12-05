package application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import application.messages.AttackEvent;
import application.messages.DeactivationEvent;
import bgu.spl.mics.MicroService;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private Future[] futures;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		futures = new Future[attacks.length];
    }

    @Override
    protected void initialize() {
    	//countDownLaunch -- lea doesnt send attacks before all sevices subscribe to busssss
        // subscribe event if needed
        // send all attack events
        // wait until all futures are resolved
        // send deactivatoion event
        for(int i=0;i<attacks.length;i++){
            futures[i]=sendEvent(new AttackEvent(attacks[i]));
        }
        boolean stop=false;
        while(!stop)
            stop=finishAllAttacks();
        sendEvent(new DeactivationEvent());
    }

    private boolean finishAllAttacks(){
        for(int i=0;i<futures.length;i++){
            if(!futures[i].isDone())
                return false;
        }
        return true;
    }
}
