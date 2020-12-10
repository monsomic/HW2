package main.java.bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.passiveObjects.Diary;
import main.java.bgu.spl.mics.application.passiveObjects.Ewoks;
import main.java.bgu.spl.mics.application.passiveObjects.Input;
import main.java.bgu.spl.mics.application.services.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.CountDownLatch;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch countDownLatch = new CountDownLatch(4);

	public static void main(String[] args) throws IOException {


		//read input
			Gson gson = new Gson();
			try {
				Reader reader = new FileReader("input.json");
				System.out.println("habaita");
				Input json = gson.fromJson(reader, Input.class);
				Diary diary = Diary.getInstance();
				battle(json);
				output(diary, args[1]);
			} catch (Exception e) {
				System.out.println("problem accured");
			}
		}

			public static void battle(Input json ){
				Ewoks ewoks = Ewoks.getInstance(json.getEwoks());

				MicroService HanSolo = new HanSoloMicroservice();
				MicroService C3PO = new C3POMicroservice();
				MicroService R2D2 = new R2D2Microservice(json.getR2D2());
				MicroService Lando = new LandoMicroservice(json.getLando());
				MicroService Leia = new LeiaMicroservice(json.getAttacks());

				Thread t1 = new Thread(HanSolo);
				Thread t2 = new Thread(C3PO);
				Thread t3 = new Thread(R2D2);
				Thread t4 = new Thread(Leia);
				Thread t5 = new Thread(Lando);

				t1.start();
				t2.start();
				t3.start();
				t5.start();
				try {
					countDownLatch.await();
					t4.start();
				} catch (InterruptedException e) {System.out.println("lia is dead");
				}

				try{
					t1.join();					// Main thread waits for thread t1 to finish it's actions before continuing to the next row.
					t2.join();
					t3.join();
					t4.join();
					t5.join();
				}catch(Exception e) {System.out.println("problemo");}		// In order to get a right answer at outputConfig.
			}





	public static void output(Diary diary, String outPath){
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		try{
			FileWriter writer = new FileWriter(outPath);
			g.toJson(diary,writer);
			writer.flush();
			writer.close();
		} catch(Exception e) {System.out.println("problema");}
	}
}

