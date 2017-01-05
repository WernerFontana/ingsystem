package test;

public enum LaneNode {
	P1("P1",0),
	P2("P2",1),
	P3("P3",2),
	P4("P4",3),
	P5("P5",4),
	P6("P6",5),
	P7("P7",6),
	I1("I1",7),
	I2("I2",8),
	I3("I3",9),
	I4("I4",10);
	
	String name;
	int position;
	LaneNode(String name,int position)
	{
		this.name=name;
		this.position=position;
	}
}
