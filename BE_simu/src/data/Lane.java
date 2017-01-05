package data;

public enum Lane {
	lane_1(0,7,0,100),
	lane_2(7,0,0,100),
	lane_3(7,8,0,100),
	lane_4(8,7,0,100),
	lane_5(8,1,0,100),
	lane_6(1,8,0,100),
	lane_7(8,2,0,100),
	lane_8(2,8,0,100),
	lane_9(7,10,0,100),
	lane_10(10,7,0,100),
	lane_11(4,10,0,100),
	lane_12(10,4,0,100),
	lane_13(5,10,0,100),
	lane_14(10,5,0,100),
	lane_15(9,10,0,100),
	lane_16(10,9,0,100),
	lane_17(6,9,0,100),
	lane_18(9,6,0,100),
	lane_19(3,9,0,100),
	lane_20(9,3,0,100);
	
	int begin;
	int end;
	int ID;
	double longueur;
	Lane(int ID, int begin, int end, double longueur)
	{
		this.ID=ID;
		this.begin=begin;
		this.end=end;
		this.longueur=longueur;
	}
	public int getBegin() {
		return begin;
	}
	public int getEnd() {
		return end;
	}
	public int getID() {
		return ID;
	}
	public double getLongueur() {
		return longueur;
	}
	
	
}
