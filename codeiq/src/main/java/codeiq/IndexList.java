package codeiq;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * インデックスも渡せるforEach.
 * 
 * RubyみたいにCollectionに標準でforEachWithIndexつけて欲しい.
 * 結構必要性あるって。
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