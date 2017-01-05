package data;

import java.time.LocalDateTime;

public enum Freq {
	P1(40,300,20,100,20),
	P2(50,200,30,150,30),
	P3(30,100,20,300,15),
	P4(20,100,30,200,50),
	P5(30,400,20,150,20),
	P6(20,50,30,100,10),
	P7(30,30,10,100,10);
	
	int f1,f2,f3,f4,f5;
	LocalDateTime begin;
	LocalDateTime end;
	Freq(int f1, int f2, int f3, int f4, int f5)
	{
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;
	}
	
	public int valueTime(LocalDateTime t)
	{
		return 0;
	}
	
	
}
