package codeiq.q798;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;



/**
 *�L���T�����́A�t���[�c�̔����Ă����Ђ̃V�X�e�����Ń��O�f�[�^���g�������͂�����Ă��܂��B
���O�f�[�^�ɂ́A�t���[�c�������ƁA���̖��O���o�͂����悤�ɂȂ��Ă��܂��B

�̔����Ă���t���[�c�́A��񂲁A�������A��������3��ނ݂̂ł��B

������A�L���T�����̓��O�f�[�^�̈ꕔ���g���āA�Ƃŕ��͂̎d�����Ă����Ƃ���A
�Ȃ�Ƒ��q�̃W���[�W��������������āA���O�t�@�C���������Ⴎ����ɂȂ��Ă��܂��܂����B

�E�B���A����i����A�����܂łɃf�[�^���W�v���Ă���Ƃ����Ă���̂ŁA
���O�f�[�^���Ď擾���Ȃ����ɂ͂���܂���B

�f�[�^�́A�ڂ����Ă��Ȃ��قǂ����Ⴎ����ɂȂ��Ă��܂����̂ŁA������ςł��B

�����ŃL���T�����́A�ЂƂ܂���؂蕶������v���Ă��钆�ōł��t���[�c����������f�[�^���������o�����Ƃɂ��܂����B
��؂蕶����{}()[]��3��ނ݂̂ł��B

�Ⴆ�΁A
{apple strawberry (melon [ apple )}

�Ƃ����f�[�^�ł́A{}��()�̋�؂蕶���̓y�A�ő��݂��܂����A[ �́A]������܂���̂ŁA
�擾�ł���f�[�^�́A"{apple strawberry melon apple}"�ƁA"(melon apple)"��2�ɂȂ�܂��B
{}�̕����t���[�c�̐���4�A()�̃t���[�c�̐���2�Ȃ̂ŁA
���̃��O�f�[�^����擾�ł���t���[�c�̍ő吔�́A4�ƂȂ�܂��B

����T���v���̃f�[�^�ƃt���[�c�̐��̗�������܂��B

�y���O�f�[�^�z
{melon (()melon strawberry)][apple}
[apple apple }{melon](strawberry}(melon]]
({}apple) melon strawberry{melon(apple apple) melon strawberry}

�y�t���[�c�̐��z
4
3
5

�T���v���̂悤�ɁA��v�����؂蕶���̒��ŁA�ł������̃t���[�c�������Ă�����̂�T���o���A�����t���[�c�������Ă��邩�𐔂��Ă��������B

�v���O���~���O�����Java���g���Ă��������B	

*/
public class Q798 {
	
	/** ���O�̐��K�\�� */
	private static final List<Pattern> LOG_PATTERNS = Arrays.asList(new Pattern[]{Pattern.compile("\\{([^\\}]+)\\}"),Pattern.compile("\\(([^\\)]+)\\)"),Pattern.compile("\\[([^\\]]+)\\]")});
	
	/** ���O���t���[�c�̐��K�\�� */
	private static final Pattern FRUITS_PATTERN = Pattern.compile("([a-z]+)");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q798/fruits.log");
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	public static void main(String[] args) throws IOException {
	
		try(Stream<String> stream = Files.lines(INPUT_FILE,CHARSET)){
			stream.forEach(line -> {
				//�s���̐��K�\�������͕��񏈗�
				LOG_PATTERNS.parallelStream().forEach(p->{
					Matcher matcher = p.matcher(line);
					while( matcher.find() ){
						String fruits = matcher.group(1);
						Matcher fruitsMatcher = FRUITS_PATTERN.matcher(fruits);
						int fruitCount = 0;
						while(fruitsMatcher.find()){
							fruitCount++;
						}
						System.out.println(String.format("count=%d,src=%s,regex=%s"
								,fruitCount,line,p.toString()));
					}			
				});
			});
		}
	}

}
