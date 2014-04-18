package core;

import java.util.HashMap;
import java.util.Map;

public class TrainManager {
	private Map<Integer, Train> trains;
	private static TrainManager instance;
	
	public static TrainManager getInstance(){
		if(instance == null){
			instance = new TrainManager();
		}
		return instance;
	}
	private TrainManager(){
		trains = new HashMap<>();
	}
	public void add(Train train){
		trains.put(train.getTrainId(), train);
	}
	
	public Train get(Integer trainId){
		Train train = null;
		if(trains.containsKey(trainId)){
			train = trains.get(trainId);
		}
		return train;
	}
	public boolean contains(Integer trainId){
		return trains.containsKey(trainId);
	}

}
