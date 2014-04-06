package codeiq.q797;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import codeiq.q797.BitMap.Position;

/**
 * 所属グループを管理する
 */
public class GroupManager {

	/** グループ情報 */
	private List<Set<Position>> data = new ArrayList<>();
	
	/** 高速化用のインデックス（メンバ一覧）　*/
	private Map<String,Integer> index = new HashMap<String,Integer>();
	
	/**
	 * 結果表示
	 */
	public void show(){
		List<Integer> pointers = index.values().stream().distinct().collect(Collectors.toList());		
		System.out.println("count = " + pointers.size());
		pointers.forEach(i->System.out.println(data.get(i)));
		
	}
	
	/**
	 * 所属グループを検索する。なければ新規グループを作成する。
	 * @param key
	 * @return
	 */
	public Group findOrCreateGroup(Position key){
		//インデックス検索
		Integer pointer = index.get(key.toString());

		//新規であればインデックス作成
		Set<Position> positionList = null;
		if(pointer == null){			
			positionList = new HashSet<>();
			positionList.add(key);						
			data.add(positionList);				
			pointer = data.size() - 1;
			index.put(key.toString(),pointer);								
		}else{
			positionList = data.get(pointer);
		}						
		Group group  = new Group();
		group.pointer = pointer;
		group.data = positionList;
		return group;
	}
	
	/**
	 * 別グループに所属しているかいなか
	 * @param key
	 * @return
	 */
	public boolean isJoinedOtherGroup(Position key){
		return index.containsKey(key.toString());
	}
	
	/**
	 * 既存グループと対象グループを合体させる
	 * @param key
	 * @param current
	 */
	public void mergeGroup(Position key,Group current){			
		
		current.data.add(key);
		
		//合成する
		int pointer = index.get(key.toString());
		
		Set<Position> src = current.data;
		Set<Position> dst = data.get(pointer);			
		dst.addAll(src);	
		
		//合成した元ネタはインデックス最作成
		for(Position e : src){
			index.put(e.toString(),pointer);
		}
	}	
	
	/**
	 * グループ
	 */
	public class Group {
		private Integer pointer;
		private Set<Position> data;
		
		/**
		 * 参加する
		 * @param position
		 * @return
		 */
		public void join(Position position){
			this.data.add(position);
			index.put(position.toString(), pointer);
		}
	}
	
}
