package enstabretagne.base.utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoriesGenerator {
	
	double borneBasse;
	double borneHaute;
	int nbCategories;
	int arrondi;
	
	List<Segment> segments;
	
	public class Segment {
		double low;
		double high;
	
		public Segment(double low, double high)
		{
			this.low=low;
			this.high=high;
		}
		
		public boolean isIn(double i){
			return (i<high & i>=low);
		}

		public boolean isIn(int i){
			return (i<high & i>=low);			
		}
		@Override
		public String toString() {
			String lowBound;
			String maxBound;
			if(low!=Double.MIN_VALUE)
				lowBound = df.format(low);
			else
				lowBound = "-Infini";
			
			if(high!=Double.MAX_VALUE)
				maxBound = df.format(high);
			else
				maxBound = "+Infini";
			
			return "["+lowBound+";"+maxBound+"[";
		}
}
	DecimalFormat df ;
	
	public CategoriesGenerator(double borneBasse, double borneHaute, int nbCategories,int minimumintegerDigit,int minimumFractionDigit) {
		super();
		this.borneBasse = borneBasse;
		this.borneHaute = borneHaute;
		this.arrondi=minimumFractionDigit;
		
		df = new DecimalFormat();
		
		if(nbCategories==0)
			nbCategories=1;
		if(nbCategories<0)
			nbCategories = - nbCategories;

		this.nbCategories = nbCategories;
		
		segments = new ArrayList<>();
		
		segments.add(new Segment(Double.MIN_VALUE, borneBasse));
		double d = (borneHaute - borneBasse) / nbCategories;
		
		df.setMaximumFractionDigits(minimumFractionDigit);
		df.setMinimumFractionDigits(minimumFractionDigit);
		df.setMinimumIntegerDigits(minimumintegerDigit);
		
		double previousLowBorne;
		previousLowBorne = borneBasse;
		for(int i = 0;i<nbCategories;i++) {			
			segments.add(new Segment(previousLowBorne,previousLowBorne+d));
			previousLowBorne=previousLowBorne+d;
		}
		
		segments.add(new Segment(borneHaute,Double.MAX_VALUE));
		
	}
	
	public Segment getSegmentOf(double i){
		for(Segment s : segments)
		{
			if(s.isIn(i))
				return s;			
		}
		return null;
	}
	
	@Override
	public String toString() {
		String res="";
		
		for(Segment s:segments)
			res+=s.toString()+"\n";
		return res;
	}
	
	public static void main(String[] args) {
		
		CategoriesGenerator cg = new CategoriesGenerator(1.2, 3.6,10,2,3);
		System.out.println(cg);
		
		System.out.println(cg.getSegmentOf(0.365));
		
	}

}
