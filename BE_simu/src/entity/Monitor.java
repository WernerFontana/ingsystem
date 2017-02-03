package entity;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import Initializer.Builder;
import config.DataManager;
import config.Prop;
import engine.impl.BasicSimEngine;
import engine.impl.Checker;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import logger.impl.SysOutLogger;

public class Monitor {



	public static void main(String [] args) throws IOException, WriteException {
		final LocalDateTime debut = LocalDateTime.now();
		int i=0,k=0;

		final LocalDateTime startTime = LocalDateTime.of(2017, 1, 1, 0, 0);

		final Duration duration = Duration.ofHours(24);
		
		long[][] tpsMoy = new long[7][7];
		long[] freqMoy = new long[4];
		
		for(i=0;i<5;i++)
		{
			BasicSimEngine engine = new BasicSimEngine();
			engine.getLoggerHub().addLogger(new SysOutLogger());
			engine.initialize(startTime,duration);

			DataManager bdd = new DataManager();

			Environment env = new Environment();
			Builder builder = new Builder(bdd,env,engine);
			builder.build();

			engine.processEventsUntil(startTime.plus(duration));
			engine.getLoggerHub().terminate();

			Checker c = new Checker(engine,env);
			if(!c.check()) {
				k++;
			}

			/*System.out.println("Date de fin de simulation : "+engine.getCurrentTime());

		System.out.println("Dur�e de simulation : "+Duration.between(debut, LocalDateTime.now()));*/
			System.err.println(k+"/"+(i+1));
			//env.getLines().forEach((i,l) -> System.out.println(i+" : "+l.getCars()));

			/*env.getNodes().forEach((i,n) -> {
			if(n instanceof Cross){
				System.out.println(((Cross)n).countObservers());
				for(int j = 0;j<((Cross)n).getIsOccupied().length;j++){
					System.out.println(((Cross)n).getIsOccupied()[j]);

				}
			}
		});*/

			//Fermeture du fichier de conf
			Prop.self.close();
			
			for (int m = 0; m < env.tpsTrajet[0].length; m++) {
				for (int n = 0; n < env.tpsTrajet[0][0].length; n++) {
					if (env.tpsTrajet[1][m][n] != 0) {
						tpsMoy[m][n] += env.tpsTrajet[0][m][n] / env.tpsTrajet[1][m][n];
					}
				}
			}
			
			for (int m = 0; m < env.freqCross.length; m++) {
				freqMoy[m] += env.freqCross[m];
			}
		}
		
		WritableWorkbook fic = null;
		fic = Workbook.createWorkbook(new File("log.xls"));
		WritableSheet tps = fic.createSheet("Temps de trajet", 0);
		WritableSheet freq = fic.createSheet("Fréquentation des croisements", 1);
		
		for (int p = 0; p < tpsMoy.length; p++) {
			for (int q = 0; q < tpsMoy[0].length; q++) {
				Label l = new Label(p, q, Long.toString(tpsMoy[p][q] / 5));
				tps.addCell(l);
			}
		}
		
		for (int p = 0; p < freqMoy.length; p++) {
			Label l = new Label(p, 0, Long.toString(freqMoy[p] / 5));
			freq.addCell(l);
		}
		fic.write();
		fic.close();
	}

}
