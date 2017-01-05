package data;

public enum Direction {
	P1(0,5,10,10,5,35,35),
	P2(10,0,5,20,20,25,20),
	P3(15,15,0,20,20,20,10),
	P4(15,10,10,0,20,40,5),
	P5(10,30,10,10,0,10,30),
	P6(20,10,40,10,10,0,10),
	P7(20,20,20,20,10,10,0);
	
	int d1,d2,d3,d4,d5,d6,d7;
	
	Direction(int d1, int d2, int d3, int d4, int d5, int d6, int d7)
	{
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
		this.d4 = d4;
		this.d5 = d5;
		this.d6 = d6;
		this.d7 = d7;
	}
}
