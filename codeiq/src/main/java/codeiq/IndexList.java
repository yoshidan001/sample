package codeiq;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * �C���f�b�N�X���n����forEach.
 * 
 * Ruby�݂�����Collection�ɕW����forEachWithIndex���ė~����.
 * ���\�K�v��������āB
 *
 * @param <E>
 */
public class IndexList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1L;
	
	public IndexList(){
	}
 	
	public IndexList(List<E> elements){
		super(elements);
	}
	
	public E append(E element){
		super.add(element);
		return element;
	}

	public void forEachWithIndex(BiConsumer<E,Integer> consumer){
		for(int i=0; i < size(); i++){
			consumer.accept(get(i), i);
		}
	}

}