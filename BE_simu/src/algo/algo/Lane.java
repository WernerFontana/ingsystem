package algo.algo;

public enum Lane {
	lane_1(0,7,"",100),
	lane_2(7,0,"",100),
	lane_3(7,8,"",100),
	lane_4(8,7,"",100),
	lane_5(8,1,"",100),
	lane_6(1,8,"",100),
	lane_7(8,2,"",100),
	lane_8(2,8,"",100),
	lane_9(7,10,"",100),
	lane_10(10,7,"",100),
	lane_11(4,10,"",100),
	lane_12(10,4,"",100),
	lane_13(5,10,"",100),
	lane_14(10,5,"",100),
	lane_15(9,10,"",100),
	lane_16(10,9,"",100),
	lane_17(6,9,"",100),
	lane_18(9,6,"",100),
	lane_19(3,9,"",100),
	lane_20(9,3,"",100);
	
	int source;
	int dest;
	String name;
	int poid;
	Lane(int source,int dest,String name, int poid)
	{
		this.source=source;
		this.dest=dest;
		this.name=name;
		this.poid=poid;
	}
}
