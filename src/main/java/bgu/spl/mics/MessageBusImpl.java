package main.java.bgu.spl.mics;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	public static class MessageBusImplHolder{
		private static MessageBusImpl instance =new MessageBusImpl();
	}

	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> qmap;   // hashMap(MicroService, his relevant queue)

	private ConcurrentHashMap<Class<? extends Message>,ConcurrentLinkedQueue<MicroService>> messageTypeMap; // hashMap(type of message, queue of microServices that subscribe to it)

	private ConcurrentHashMap<Event,Future> futureMap; // hashmap (some Event (event<T>),its future (future<T>)

	private Object  lockUnregisterSendEvent, lockUnregisterSendBroadcast;



	private MessageBusImpl(){
		qmap= new ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>>();
		messageTypeMap = new ConcurrentHashMap<Class<? extends Message>,ConcurrentLinkedQueue<MicroService>>();
		futureMap = new ConcurrentHashMap<Event,Future>();
	}

	public static MessageBusImpl getInstance() { // not sure if needed synchronized
		return MessageBusImplHolder.instance;
	}
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {    //check if the type exists
		// sync - because : two MS subscribe to same type of event - makes two identical event types in map
		synchronized ((type)) {
			ConcurrentLinkedQueue Q = messageTypeMap.get(type);
			if (Q == null)
				messageTypeMap.put(type, new ConcurrentLinkedQueue<MicroService>());

			messageTypeMap.get(type).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// sync - because : two MS subscribe to same type of event - makes two identical event types in map
		synchronized ((type)) {
			ConcurrentLinkedQueue Q = messageTypeMap.get(type);
			if (Q == null)
				messageTypeMap.put(type, new ConcurrentLinkedQueue<MicroService>());

			messageTypeMap.get(type).add(m);
		}
    }
	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future f = futureMap.get(e);
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {     //sync - becouse : inside for - get can bring null becouse m was deleted from qmap in unregister

		ConcurrentLinkedQueue<MicroService> Q = messageTypeMap.get(b.getClass());
		if(Q!=null) {
			synchronized (lockSubBroadcast){
			for(MicroService m :Q) {
				qmap.get(m).add(b);
				}
			}
		}
	}

	
	@Override
	public  <T> Future<T>  sendEvent(Event<T> e) { //enque to the right queue (round robin) , create future object , put in futuremap
		// sync - because : we can pass first if and then try poll from an empty Q because unregister by another thread :::::BUT - in our implementation this cant happen. sync or  not ?
		//		  because : if two MS send the same type of event and there is one MS in that Q(in messageTtpeMap) :::BUT - cant happen in our system

		synchronized (lockUnregisterSendEvent) {
			ConcurrentLinkedQueue<MicroService> Q = messageTypeMap.get(e.getClass());
			if (Q == null || Q.isEmpty())
				return null;

			MicroService firstM = Q.poll();
			qmap.get(firstM).add(e);
			messageTypeMap.get(e.getClass()).add(firstM); // for round robin

			Future<T> f = new Future<>();
			futureMap.put(e, f);
			//notifyAll(); //no need becouse blockingQueue notifies alone.
			return f;
		}
	}

	@Override
	public void register(MicroService m) {    // add to qlist, add name to qmap

		qmap.put(m,new LinkedBlockingQueue<Message>());
	}



	@Override
	public void unregister(MicroService m) {
		// delete from qmap
		//search in messageTypeMap and delete microservice from it
		synchronized (lockUnregisterSendBroadcast){
		synchronized ((lockUnregisterSendEvent)) {

			qmap.remove(m);
			for (Class<? extends Message> type : messageTypeMap.keySet()) {
				for (MicroService curr : messageTypeMap.get(type)) {
					if (curr.equals(m))
						messageTypeMap.get(type).remove(curr);
				}
			}
			}
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

			return qmap.get(m).take(); //blocking queue, waits if queue is empty
	}

	/*private void restart(){ // to clear everything from messegebus
		instance= new MessageBusImpl();
	}*/
}
