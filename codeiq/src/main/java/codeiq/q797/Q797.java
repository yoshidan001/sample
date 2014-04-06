package codeiq.q797;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import codeiq.q797.BitMap.Position;
import codeiq.q797.GroupManager.Group;

/**
����̓R�����u�X�������Ă���15���I�܂ł����̂ڂ�܂��B
�����̖`���Ƃ������V�嗤�����ɖ�N�ɂȂ��ĊC�֖`���ɂłĂ��܂����B
���Ȃ������̖`���Ƃ����̒��Ԃł��B

���Ȃ���������D�̓G�[�Q�C�Ɍ������ďo�����܂����B

�o�����Ă܂��Ȃ��D���E���r�[���{�萺�������܂����B

�u�����A���̓��̑����͂Ȃ�Ƃ��Ȃ�񂩁I������������I�D�̐i�H�����߂���ł͂Ȃ����I�ǂ��ɂ������܂��v

�G�[�Q�C�͓������W���Ă���A���ɓ��Ɠ����ׂ荇���Ă���Ƃ���͐󐣂Ȃ̂ŁA
�D�����ʂ��Ȃ��悤�ɉ�����Ēʂ�Ȃ���΂Ȃ�܂���B

�����ŁA���Ȃ��͎茳�ɂ���G�[�Q�C�̓��f�[�^���g���āA�u���̌ł܂�v�̐��𐔂��Ă݂邱�Ƃɂ��܂����B
�u���̌ł܂�v���킩�邱�ƂŁA�D�̐i�H�����߂₷���Ȃ邽�߂ł��B

�y�G�[�Q�C�̓��f�[�^�z���ꕔ���̃f�[�^�ł��B

������������
������������
������������
������������
������������
������������

�����21��4�畽���L�����[�g������G�[�Q�C�̓��f�[�^�̈ꕔ���ł��B
�G�[�Q�C��100�����L�����[�g���P�ʂɋ�؂��ăf�[�^�������Ă��܂��B

���ۂ͓������邱�Ƃ��Ӗ����܂��B
���ۂ͓����Ȃ����Ƃ��Ӗ����܂��B

�����ۂ��㉺���E�ɂ���ꍇ�A�����͑D���ʂ�Ȃ��̂łЂƂ́u���̌ł܂�v�Ƃ��Đ����܂��B
�����ۂ��΂߂ɕ���ł���ꍇ�A�����͑D���ʂ��̂ŁA�ł܂�Ƃ͐����܂���B

��L�̓��f�[�^���ƁA�u���̌ł܂�v�́A3�ɂȂ�܂��B

�܂����ۂ̃G�[�Q�C�̓��f�[�^�́A���ۂ�1�A���ۂ�0�ŁACSV�t�@�C���ŕۑ�����Ă��܂��B

�����
�u���̌ł܂�v���J�E���g����N���X�����������Ă��������B
�v�Z���ʂ́A�R���\�[���ɏo�͂���悤�ɂ��Ă��������B

�G�[�Q�C�̓��f�[�^�t�@�C���́A3�p�ӂ���Ă��܂��̂Ńe�X�g�f�[�^�Ƃ��Ă��g�����������B

����A���Ȃ������u���̌ł܂�v���J�E���g����R�[�h�́A�ʂ̖`���Ŏg���\��������܂��B
���̐l�����ł��g����悤�ɊȒP�ȃ����ƁA�R�[�h�ɃR�����g��K�X�c���Ă��������B

�����Java���g�p���Ă��������B

������
AegeanSea.zip:
zip�t�@�C�����𓚂��Ă��g�����������B
�ȉ���4�t�@�C���������Ă��܂��B

answer.txt
�𓚗p�T���v���t�@�C���ł��B

AegeanSea1.csv
�G�[�Q�C�̓��f�[�^�ł��B

AegeanSea2.csv
�G�[�Q�C�̓��f�[�^�ł��B

AegeanSea3.csv
�G�[�Q�C�̓��f�[�^�ł��B

���𓚕��@
�𓚗p�T���v���t�@�C���ɏ�����Ă��邱�Ƃɂ��������A�𓚃t�@�C�����쐬���A����������t�@�C���A�b�v���[�h�ɂĒ�o���Ă��������B
*/
public class Q797{
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q797/AegeanSea3.csv");
	
	/**
	 * �E���{��
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{

		//�n�}�ǂݎ��
		List<List<Integer>> rowData = Files.readAllLines(INPUT_FILE,CHARSET).stream().map(s -> {
			return Arrays.asList(s.split(",")).stream().map(d -> Integer.parseInt(d)).collect(Collectors.toList());
		}).collect(Collectors.toList());;
		
		BitMap map = new BitMap(rowData);
		GroupManager manager = new GroupManager();
		
		//�r�b�g�������Ă�����̂̂ݎ��s
		map.forEachOn(current->{
			
			//��������������O���[�v���擾����
			Group currentGroup = manager.findOrCreateGroup(current);					
				
			//�������̑{��
			if(current.under().isOn()){	
				//���݃O���[�v�ɏ���������
				currentGroup.join(current.under());			
			}
			
			//�E�����̑{���ƌ��݃O���[�v�Ƃ̊֘A�Â�
			if(current.right().isOn()){

				Position right = current.right();
				
				//�E���͊��ɕʂ̃O���[�v�ɏ������Ă���\��������
				if(manager.isJoinedOtherGroup(right)){
					//�����̃O���[�v�Ɏ��������ݏ�������O���[�v����������
					manager.mergeGroup(right,currentGroup);
				}else{
					//���݃O���[�v�ɏ���������
					currentGroup.join(right);
				}
			}				
		});
		//���ʕ\��
		manager.show();
	}

}