package codeiq.q797;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * ビットマップデータ .
 */
public class BitMap {

	private List<List<Integer>> map = new ArrayList<>();
	
	public BitMap(List<List<Integer>> map) {
		this.map = map;
	}
	
	/**
	 * フラグが立っている箇所だけ処理 .
	 * @param consumer
	 */
	public void forEachOn(Consumer<Position> consumer){
		for(int y = 0; y < map.size(); y++){
			List<Integer> xList = map.get(y);
			for(int x = 0; x < xList.size(); x++){
				Position position = new Position(y,x);
				if(position.isOn()){
					consumer.accept(position);
				}
			}
		}
	}

	/**
	 * 地図上での位置
	 */
	public class Position{
		
		private int x;
		private int y;
		
		public Position(int y, int x){
			this.x = x;
			this.y = y;
		}
		
		public Position right() {
			return new Position(y,x+1);
		}
		
		public Position under(){
			return new Position(y+1,x);
		}
		
		public boolean isOn(){
			return y == map.size() || x == map.get(y).size() ? false :  map.get(y).get(x) == 1;
		}
		
		@Override
		public String toString(){
			return String.join(",",String.valueOf(y),String.valueOf(x));
		}
	}
}
