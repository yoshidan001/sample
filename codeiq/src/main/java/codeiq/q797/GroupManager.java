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
 * �����O���[�v���Ǘ�����
 */
public class GroupManager {

	/** �O���[�v��� */
	private List<Set<Position>> data = new ArrayList<>();
	
	/** �������p�̃C���f�b�N�X�i�����o�ꗗ�j�@*/
	private Map<String,Integer> index = new HashMap<String,Integer>();
	
	/**
	 * ���ʕ\��
	 */
	public void show(){
		List<Integer> pointers = index.values().stream().distinct().collect(Collectors.toList());		
		System.out.println("count = " + pointers.size());
		pointers.forEach(i->System.out.println(data.get(i)));
		
	}
	
	/**
	 * �����O���[�v����������B�Ȃ���ΐV�K�O���[�v���쐬����B
	 * @param key
	 * @return
	 */
	public Group findOrCreateGroup(Position key){
		//�C���f�b�N�X����
		Integer pointer = index.get(key.toString());

		//�V�K�ł���΃C���f�b�N�X�쐬
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
	 * �ʃO���[�v�ɏ������Ă��邩���Ȃ�
	 * @param key
	 * @return
	 */
	public boolean isJoinedOtherGroup(Position key){
		return index.containsKey(key.toString());
	}
	
	/**
	 * �����O���[�v�ƑΏۃO���[�v�����̂�����
	 * @param key
	 * @param current
	 */
	public void mergeGroup(Position key,Group current){			
		
		current.data.add(key);
		
		//��������
		int pointer = index.get(key.toString());
		
		Set<Position> src = current.data;
		Set<Position> dst = data.get(pointer);			
		dst.addAll(src);	
		
		//�����������l�^�̓C���f�b�N�X�ō쐬
		for(Position e : src){
			index.put(e.toString(),pointer);
		}
	}	
	
	/**
	 * �O���[�v
	 */
	public class Group {
		private Integer pointer;
		private Set<Position> data;
		
		/**
		 * �Q������
		 * @param position
		 * @return
		 */
		public void join(Position position){
			this.data.add(position);
			index.put(position.toString(), pointer);
		}
	}
	
}
