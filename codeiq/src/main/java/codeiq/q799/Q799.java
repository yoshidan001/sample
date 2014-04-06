package codeiq.q799;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;



/**
������������ł́A���܂̎���ɂ����Ă��O�[�e���x���O����ɂ�������Ĉ�������Ă��܂��B
�O�[�e���x���O����Ƃ́A���Łi������g�ݍ��킹�č�����Łj�ň��������@�ł��B(Wikipedia)

������������̏����������́A�g�������������̏ꏊ�ɖ߂��Ȃ��̂ŁA
�������������Ă���{�b�N�X�͂����Ⴎ����ŁA
�ق��������������Ɍ����o�����Ƃ��ł��܂���B

�����Ō����˂�������̕������G�~�[�́A���������Ă���{�b�N�X�r�f�I�ŎB�e���A
�摜��͂��A�{�b�N�X�̂ǂ��ɂǂ̊��������邩����͂��A
�ق���������̊������ǂ��ɂ��邩�u���ɂ݂������v���O���~���O����邱�Ƃɂ��܂����B

����̏�Ԃ̃{�b�N�X�͈ȉ��̂悤�ɂȂ��Ă��܂��B


6�~6�̃{�b�N�X�̒�����"PRO, PROGRAMMER"�Ƃ���������T���o�������ł��B
�J���}��X�y�[�X��1�̊����Ƃ��ăJ�E���g���܂��B
�ł��̂ŁA����͕���13�A�J���}1�A�X�y�[�X1�̍��v15�̊������K�v�ł��B
��x�g����������2��g�����Ƃ͂ł��܂���B

�����̏ꏊ�́A(�c�ԍ�,���ԍ�)�ŕ\���ƈȉ��̂悤�ȏ��ԂŊ��������o�����Ƃ��ł��܂��B
�c�ԍ��͏ォ�牺�̕����ɁA���ԍ��͍�����E�̕����ɁA1���琔���܂��B
1,4 = P
2,1 = R
1,6 = O
5,2 = ,
5,4 = space
2,2 = P
3,4 = R
4,5 = O
5,1 = G
4,3 = R
1,1 = A
2,5 = M
6,1 = M
2,4 = E
6,6 = R


��L�̗�ł́A�Ⴆ��"P"�Ƃ���������2��łĂ��܂����A(1,4)��(2,2)�ɂ���"P"�͂ǂ�����ɂ����Ă����܂��܂���B
�܂��ꏊ�̓����1�̂݋L�q����΂悢�ł��B���ׂĂ̏ꏊ�ԍ��������K�v�͂���܂���B

�摜��͂����{�b�N�X�̃f�[�^��CSV�ŏo�͂���܂��B
�{�b�N�X�̃f�[�^���g���Ăق������������邽�߂̊����݂̍菈��T�������Ă��������B

������
gutenberg_Java.zip
zip�t�@�C�����𓀂���ƈȉ��̃t�@�C���������Ă��܂��B

gutenberg.csv
���̃{�b�N�X����"STAY HUNGRY, STAY FOOLISH"�Ƃ������������o���Ă��������B
����21�A�J���}1�A�X�y�[�X3�ŁA���v25�̊������K�v�ł��B

answer.txt
�𓚗p�e�L�X�g�t�@�C���ł��B

���𓚕��@
�𓚗p�e�L�X�g�t�@�C���ɏ�����Ă��邱�Ƃɏ]���ăt�@�C�����쐬���A
����������t�@�C���A�b�v���[�h���Ē�o���������B
*/

public class Q799 {
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q799/gutenberg.csv");
	
	private static final String TARGET = "STAY HUNGRY, STAY FOOLISH";
	
	public static void main(String[] args) throws IOException{
				
		//MapReduce���g�p���ăn�b�V���f�[�^�쐬
		Map<String,List<Position>> index = createImageData();
	
		//�n�b�V���W���C���ŕ\��
		char[] ch = TARGET.toCharArray();
		for(int i = 0 ; i < ch.length; i++){
			String val = String.valueOf(ch[i]);
			List<Position> pos = index.get(val);	
			Position p = pos.get(0);
			pos.remove(0);
			if(pos.isEmpty()){
				index.remove(val);
			}
			System.out.println(String.format("%s:%s", val,p));
		}
	}
	
	/**
	 * �摜�f�[�^��ǂݍ��݃n�b�V�����쐬����.
	 * Java8��StreamAPI���g���Ă���Ă݂�B
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Map<String,List<Position>> createImageData() throws IOException{
		
		//Y�����̃J�E���^
		Supplier<Integer> counterForY = counter();		
				
		try(Stream<String> stream = Files.lines(INPUT_FILE,CHARSET)){	
			//�ϊ�
			return stream.map(s->{
				int y = counterForY.get();
				Map<String,List<Position>> datamap = new HashMap<>();
				//X�����̃J�E���^
				Supplier<Integer> counterForX = counter();
				split(s).forEach(d->{
					Position position = new Position(y,counterForX.get());
					if(datamap.containsKey(d)){
						datamap.get(d).add(position);
					}else {
						datamap.put(d, new ArrayList<>(Arrays.asList(position)));
					}
				});	
				return datamap;
			//������
			}).reduce((k,v)->{			
				v.entrySet().forEach(e->{
					if(k.containsKey(e.getKey())){
						k.get(e.getKey()).addAll(e.getValue());
					}else {
						k.put(e.getKey(), e.getValue());
					}
				});
				return k;
			}).get();
		}
	}
	
	/**
	 * ���̓t�@�C���͒l1������""��؂�ƌ��܂��Ă���̂ŁA�����d���̌��߂����ǂݍ��݁B
	 * ��ʒu�̕����̂ݒ��o����B
	 * 
	 * @param s
	 * @return
	 */
	private static List<String> split(String s){
		char[] ch = s.toCharArray();
		List<String> array = new ArrayList<>();
		for(int i = 1; i < ch.length; i = i + 2){
			array.add(String.valueOf(ch[i]));			
		}
		return array;
	}
		
	/**
	 * ���W�p�̃J�E���^
	 * @return 
	 */
	private static Supplier<Integer> counter(){
		int count[] = new int[]{0};
		return () -> ++count[0];
	}

	static class Position {
		public int y;
		public int x;
		public Position(int y , int x){
			this.y = y;
			this.x = x;
		}
		public String toString(){
			return String.format("%d,%d",y,x);
		}
	}
}
