/**
* Classe ExcelDataloggerImpl.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.base.utility.loggerimpl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.temporal.Temporal;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.utility.ILogger;
import enstabretagne.base.utility.IRecordable;
import enstabretagne.base.utility.LogLevels;
import enstabretagne.base.utility.LoggerParamsNames;
import enstabretagne.simulation.components.ScenarioId;

public class ExcelDataloggerImpl implements ILogger{
	
	class Logs implements IRecordable{
		String[] s;

		public Logs(String[] s){
			this.s=s;
		}

		@Override
		public String[] getTitles() {
			String[] h={"Scenario","Replique","Temps Reel", "Temps Logique", "Niveau de Log", "Nom Objet",
					"Fonction", "Message"};
			return h;
		}

		@Override
		public String[] getRecords() {
			return s;
		}

		@Override
		public String getClassement() {
			return "Logs";
		}
	}
	
	class DataLogs implements IRecordable{
		IRecordable s;
		ScenarioId scenarioId;
		String tempsReel;
		String tempslogique;
		String nomObj;
		
		public DataLogs(ScenarioId scenarioId,String tempsReel,String tempslogique,String nomObj,IRecordable s){
			this.s=s;
			this.scenarioId=scenarioId;
			this.tempsReel=tempsReel;
			this.tempslogique=tempslogique;
			this.nomObj=nomObj;
		}

		@Override
		public String[] getTitles() {
			String[] h={"Scenario","Replique","Temps Reel", "Temps Logique", "Nom Objet"};			
			return join(h,s.getTitles());
		}

		@Override
		public String[] getRecords() {
			String[] r={scenarioId.getScenarioId(),Long.toString(scenarioId.getRepliqueNumber()),tempsReel, tempslogique, nomObj};
			return  join(r,s.getRecords());
		}
		

		@Override
		public String getClassement() {
			// TODO Auto-generated method stub
			return s.getClassement();
		}
	}
	
	 private String[] join(String [] ... parms) {
		    // calculate size of target array
		    int size = 0;
		    for (String[] array : parms) {
		      size += array.length;
		    }

		    String[] result = new String[size];

		    int j = 0;
		    for (String[] array : parms) {
		      for (String s : array) {
		        result[j++] = s;
		      }
		    }
		    return result;
		  }

	
	HashMap<String,HSSFRow> dicoLocations;
	HSSFWorkbook wb;
	
	PrintStream ps;
	ByteArrayOutputStream baos;
	FileOutputStream fileOut;
	@Override
	public boolean open(HashMap<String,Object> initParams) {
		wb = new HSSFWorkbook();
		dicoLocations = new HashMap<>();
		baos = new ByteArrayOutputStream();
	    ps = new PrintStream(baos);

	    boolean success=true;
		String dirName;
		String fileName;
			
			if(initParams.containsKey(LoggerParamsNames.DirectoryName.toString()))
				dirName =initParams.get(LoggerParamsNames.DirectoryName.toString()).toString(); 
			else
				dirName = System.getProperty("user.dir");
			if(initParams.containsKey(LoggerParamsNames.FileName.toString()))
				fileName =initParams.get(LoggerParamsNames.FileName.toString()).toString();
			else
				fileName = "monfichier.xls";
			try {
			fileOut = new FileOutputStream(dirName+"\\"+fileName);
			
		} catch (FileNotFoundException e) {
			success=false;
			System.err.println("Logger "+this.getClass().getCanonicalName()+" n'a pu être créé.)");
			System.err.println(dirName+"\\"+fileName+" est sans doute ouvert ou n'existe pas (chemin non existant au préalable par exemple)");
		}

	    return success;
	}

	@Override
	public void log(StackTraceElement el,ScenarioId scenarioId,Temporal t, LogicalDateTime d, LogLevels level, Object obj,
			String function, String message, Object... args) {
		if(!level.equals(LogLevels.data))
		{
			String elTxt = "("+el.getFileName()+":"+el.getLineNumber()+")>"+el.getMethodName();
			
			ps.printf(message,args);
			String[] s;
			if(d==null){
				if(obj!=null)
					s=new String[]{scenarioId.getScenarioId(),Long.toString(scenarioId.getRepliqueNumber()),t.toString(),"",level.toString(),obj.toString(),elTxt,baos.toString()};
				else
					s=new String[]{scenarioId.getScenarioId(),Long.toString(scenarioId.getRepliqueNumber()),t.toString(),"",level.toString(),"",elTxt,baos.toString()};
			}
			else {
				if(obj!=null)
					s=new String[]{scenarioId.getScenarioId(),Long.toString(scenarioId.getRepliqueNumber()),t.toString(),d.toString(),level.toString(),obj.toString(),elTxt,baos.toString()};
				else
					s=new String[]{scenarioId.getScenarioId(),Long.toString(scenarioId.getRepliqueNumber()),t.toString(),d.toString(),level.toString(),"",elTxt,baos.toString()};
			}
			baos.reset();
			
			Logs l = new Logs(s);
			createRowFrom(l);
		}
		else
		{
			if(IRecordable.class.isAssignableFrom(obj.getClass())){
				DataLogs dl = new DataLogs(scenarioId,t.toString(),d.toString(),obj.toString(), (IRecordable) obj);
				createRowFrom(dl);
			}
		}
	}

	@Override
	public void close() {
		try {
			int nbS=wb.getNumberOfSheets();
			for(int i = 0;i<nbS;i++)
			{
				HSSFSheet s = wb.getSheetAt(i);
				for(int j=0;j<s.getRow(0).getLastCellNum();j++)
				{
					HSSFCell c = s.getRow(0).getCell(j);
					c.getSheet().autoSizeColumn(c.getColumnIndex());
				}
				
				HSSFCell firstCell=s.getRow(0).getCell(0);
				HSSFCell lastCell=s.getRow(0).getCell((int) s.getRow(0).getLastCellNum()-1);
				s.setAutoFilter(new CellRangeAddress(firstCell.getRowIndex(),lastCell.getRowIndex(),lastCell.getRowIndex(),lastCell.getColumnIndex()));
				
			}
			
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void clear(HashMap<String,Object> initParams) {
		
	}

	private void fillRowWith(HSSFRow r,String[] data){
		HSSFCell c=null;
		
		for(String s:data)
		{
			if(c==null)
				c=r.createCell(0);
			else
				c=r.createCell(c.getColumnIndex()+1);
			
			try {
				double d=Double.valueOf(s);
				c.setCellValue(d);
			}
			catch(NumberFormatException e) {
				c.setCellValue(s);				
			}

		}
	}
	
	
	
	private HSSFRow createRowFrom(IRecordable o) {
		HSSFRow lastRow,r;
		String[] data;

		if(dicoLocations.containsKey(o.getClassement()))
			lastRow=dicoLocations.get(o.getClassement());
		else
		{
			HSSFSheet logSheet = wb.createSheet(o.getClassement());
			lastRow=logSheet.createRow(0);
			data=o.getTitles();
			fillRowWith(lastRow, data);
			dicoLocations.put(o.getClassement(), lastRow);
		}
		r = lastRow.getSheet().createRow(lastRow.getRowNum()+1);
		data=o.getRecords();
		fillRowWith(r,data);
		dicoLocations.replace(o.getClassement(), r);
		return r;
	}

}

