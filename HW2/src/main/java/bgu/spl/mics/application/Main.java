package main.java.bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.internal.util.xml.impl.Input;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.passiveObjects.Diary;
import main.java.bgu.spl.mics.application.passiveObjects.Ewoks;
import main.java.bgu.spl.mics.application.services.*;
import java.io.FileWriter;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		//read input
		try{
			Input json = JsonInputReader.getInputFromJson(main.java.bgu.spl.mics.application);
			System.out.println(json);

			Ewoks ewoks = Ewoks.getInstance(json.getEwoks());
			Diary diary = Diary.getInstance();

			MicroService HanSolo = new HanSoloMicroservice() ;
			MicroService C3PO = new C3POMicroservice() ;
			MicroService R2D2 = new R2D2Microservice(json.getR2D2()) ;
			MicroService Leia = new LeiaMicroservice(json.getattacks()) ;
			MicroService Lando = new LandoMicroservice(json.getLando()) ;

			Thread t1= new Thread(HanSolo);
			Thread t2= new Thread(C3PO);
			Thread t3= new Thread(R2D2);
			Thread t4= new Thread(Leia);
			Thread t5= new Thread(Lando);



		}
		catch (){

		}
		//output part
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		fileWriter writer = new FileWriter(output);
		gson.tojson(diary,writer);
		writer.flush();
		writer.close();


	}
}
