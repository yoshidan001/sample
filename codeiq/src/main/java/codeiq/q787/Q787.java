package codeiq.q787;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import codeiq.IndexList;


/**
 * ���ʂÂ��̕��@
 *	�_�����������ɁA1, 2, ...�Ə��ʂ����邾���ł����A�������_��2�l�ȏ�̏ꍇ�͓������ʂɂȂ�A�����艺�̓_���͏��ʂ�����Ă����܂��B
 *	���Ƃ��΁A80�_��5�ʂ�2�l�����Ƃ���ƁA79�_�̐l��6�ʂł͂Ȃ�7�ʂɂȂ�܂��B80�_��3�l����΁A8�ʂɂȂ�܂��B
 *
 *�Ecsv�t�@�C���ɂ���
 *	csv�t�@�C���̕����R�[�h��Shift_JIS�A���s�R�[�h��CR+LF�ɂȂ��Ă��܂��B
 *	�o�̓f�[�^�Ɋւ��ẮA�����R�[�h�E���s�R�[�h�̎w��͓��ɂ���܂���B
 *�E�o�̓f�[�^�̍��ږ�
 *	����E���w�E�p��E�Љ�E���Ȃ̏��ʂɉ����āA���v�_�̏��ʂ��o�͂��邱�ƂɂȂ�܂��̂ŁA1�s�ڂ̍��ږ��Ɂu���v�v��ǉ����Ă��������B
 *
 *�E���̑�
 * 	�{��̋L�q�Ɋւ��Ă����܂��ȕ���������Ɣ��f�����ꍇ�́A�ǂ̂悤�ɉ��߁E�Ή����������A�\�[�X�R�[�h���ɃR�����g�Ƃ��ċL�q���Ă��������B
 */
public class Q787 {
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q787/class_3c_input.csv");

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		//���v�v�Z�p�̋^���N���[�W��
		Function<String,IndexList<String>> sum = sum();
	
		//�c���ϊ��i�ȖڒP�ʂɕ�������j
		IndexList<List<String>> dataList = new IndexList<>();
		try(Stream<String> stream = Files.lines(INPUT_FILE,CHARSET)){
			stream.forEach(s->{
				sum.apply(s).forEachWithIndex((e,i)-> {
					List<String> subject = dataList.size() <= i ? dataList.append(new ArrayList<>()) : dataList.get(i);				
					subject.add(e);				
				});		
			});
		}
		
		//���ʕt��(�Ȗڂ��ƂɓƗ��Ȃ̂ŕ��񏈗��\�j
		dataList.parallelStream().skip(1).forEach(i->{
			
			//���ʕێ��p�̋^���N���[�W��
			Function<String,String> rank = rank();
	
			Map<String,String> array = i.stream().skip(1)
				.sorted((p,a)-> atoi(a) - atoi(p))
					.map(s->Arrays.asList(s,rank.apply(s)))
						.distinct()
							.collect(Collectors.toMap((k)->k.get(0), (v)->v.get(1)));	
			//�_�������ʂɒu��
			i.replaceAll(s->array.containsKey(s) ? array.get(s) : s);
		});
		
		//�c���ϊ��i�������ݒP�ʂɖ߂��j
		IndexList<StringBuilder> writeList = new IndexList<>();
		dataList.forEach(c->{
			new IndexList<String>(c).forEachWithIndex((e,i)->{;
				StringBuilder name = writeList.size() <= i ? writeList.append(new StringBuilder()) : writeList.get(i).append(",");				
				name.append(e);				
			});			
		});
						
		//�t�@�C���o��
		Files.write(Paths.get(String.format("class_3c_output_%d.csv",System.currentTimeMillis())),writeList,CHARSET);
	}
	
	/**
	 * ������ to ���l
	 * @param value
	 * @return
	 */
	private static int atoi(String value){
		return Integer.parseInt(value);
	}
	
	/**
	 * ���v�\���\�̍��K�֐�
	 * @return
	 */
	private static Function<String,IndexList<String>> sum(){
		boolean[] first = new boolean[]{true};
		return (String s) -> {
			IndexList<String> arrayList = new IndexList<>(Arrays.asList(s.split(",")));				
			if(first[0]){
				first[0] = false;
				arrayList.add("���v");
			}else{
				int sum = arrayList.subList(1,arrayList.size())
							.stream()
								.mapToInt(v->atoi(v))
									.sum();
				arrayList.add(String.valueOf(sum));
			}
			return arrayList;
		};		
	}
	
	/**
	 * �����L���O�\���p�̍��K�֐�
	 * @return
	 */
	private static Function<String,String> rank() {
		int[] min = new int[]{-1};
		int[] count = new int[]{0};
		int[] rank = new int[]{1};		
		return (String next) -> {						
			count[0]++;
			if(min[0] > atoi(next)){
				rank[0] = count[0];
			}			
			min[0] = atoi(next);
			return String.valueOf(rank[0]);
		};
	}
}