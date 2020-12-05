import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private Vector<Vector<Message>> qlist; // list of all queues
	//private Vector<Object[]> qmap; //hashmap (microservice name (String),index in qlist (int))
	private ConcurrentHashMap<Integer,String> qmap;

	private Vector<Vector<String>> eventTypeList; // for each type pf message a vector of names
	//private Vector<Object[]> eventTypeMap; // hashmap (event type (Event.class),index in eventTypeList (int)) .getClass??
	private ConcurrentHashMap<Integer,Class<? extends Event>> eventTypeMap; //

	//private Vector<Object[]> futureMap; // hashmap (some Event (event<T>),its future (future<T>)
	private ConcurrentHashMap<Event,Future> futureMap;

	private static MessageBusImpl instance =null;


	private MessageBusImpl(){
		qlist = new Vector<Vector<Message>>();
		qmap= new ConcurrentHashMap<Integer,String>();
		eventTypeList = new Vector<Vector<String>>(); //Vector<String> will act as a queue for round robin implementation
		eventTypeMap = new ConcurrentHashMap<Integer,Class<? extends Event>>();
		futureMap = new ConcurrentHashMap<Event,Future>();
	}

	public static synchronized MessageBusImpl getInstance() { // not sure if needed synchronized
		if (instance == null)
			instance = new MessageBusImpl();
		return instance;
	}
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {    //check if the type exists
		if(eventTypeMap.contains(type)){ // when subscribing send event.class
			for(int i:eventTypeMap.keySet()){
				if(eventTypeMap.get(i).equals(type))
					eventTypeList.elementAt(i).add(m.getName());
			}
		}
		else{ // make new vector for this specific type and add the name of m to the vector
			eventTypeList.add(new Vector<String>());
			int lastIndex= eventTypeList.size()-1;
			eventTypeList.elementAt(lastIndex).add(m.getName());
			eventTypeMap.put(lastIndex,type); // add to eventTypeMap
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }
	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future f = futureMap.get(e);
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public synchronized  <T> Future<T>  sendEvent(Event<T> e) { //enque to the right queue (round robin) , create future object , put in futuremap
		boolean found=false;
		for(int i:eventTypeMap.keySet()) {
			if (eventTypeMap.get(i).equals(e.getClass()) && !eventTypeList.elementAt(i).isEmpty()){
				String name= eventTypeList.elementAt(i).elementAt(0);
				for(int j:qmap.keySet()){ //search the index that suits for the name in qmap
					if(qmap.get(j).equals(name)){
						qlist.elementAt(j).add(e);
						break;
					}
				}
				String currentFirstName= eventTypeList.elementAt(i).firstElement(); // the first name is pushed to the end
				eventTypeList.elementAt(i).remove(0);
				eventTypeList.elementAt(i).add(currentFirstName);
				found=true;
				break; // we want stop the loop , check if works
			}
		}
		if (!found)
			return null;

		Future<T> f= new Future<>();
		futureMap.put(e,f);
		notifyAll();
		return f;
	}

	@Override
	public void register(MicroService m) {    // add to qlist, add name to qmap
		qlist.add(new Vector<Message>());
		qmap.put(qlist.size()-1,m.getName());
	}

	@Override
	public void unregister(MicroService m) {
		// find the index of the name in qmap, save and delete
		// go to the index in qlist and delete the queue
		// delete from eventTypeList (pass on all the vectors and find name.
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

			int index = qmap.get(m.getName()) // change the location of key/value if it to work
			if( qlist.elementAt(index).isEmpty())
				return null;
			Message toReturn = qlist.elementAt(index).firstElement();
			qlist.elementAt(index).remove(0);
			return toReturn;

	}

	private void restart(){ // to clear everything from messegebus
		instance= new MessageBusImpl();
	}
}
