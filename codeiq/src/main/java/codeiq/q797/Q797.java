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
時代はコロンブスが生きていた15世紀までさかのぼります。
多くの冒険家たちが新大陸発見に躍起になって海へ冒険にでていました。
あなたもその冒険家たちの仲間です。

あなたが乗った船はエーゲ海に向かって出発しました。

出発してまもなく船長・ルビーが怒鳴り声をあげました。

「おい、この島の多さはなんとかならんか！島が多すぎる！船の進路が決められんではないか！どうにかしたまえ」

エーゲ海は島が密集しており、特に島と島が隣り合っているところは浅瀬なので、
船が座礁しないように回避して通らなければなりません。

そこで、あなたは手元にあるエーゲ海の島データを使って、「島の固まり」の数を数えてみることにしました。
「島の固まり」がわかることで、船の進路が決めやすくなるためです。

【エーゲ海の島データ】※一部分のデータです。

○○○○○○
○●●○○○
○●○○○○
○○○○●○
○○○●○○
○○○○○○

これは21万4千平方キロメートルあるエーゲ海の島データの一部分です。
エーゲ海を100平方キロメートル単位に区切ってデータを持っています。

黒丸は島があることを意味します。
白丸は島がないことを意味します。

★黒丸が上下左右にある場合、そこは船が通れないのでひとつの「島の固まり」として数えます。
★黒丸が斜めに並んでいる場合、そこは船が通れるので、固まりとは数えません。

上記の島データだと、「島の固まり」は、3つになります。

また実際のエーゲ海の島データは、黒丸は1、白丸は0で、CSVファイルで保存されています。

■問題
「島の固まり」をカウントするクラスを完成させてください。
計算結果は、コンソールに出力するようにしてください。

エーゲ海の島データファイルは、3つ用意されていますのでテストデータとしてお使いください。

今回、あなたが作る「島の固まり」をカウントするコードは、別の冒険で使う可能性があります。
他の人がいつでも使えるように簡単なメモと、コードにコメントを適宜残してください。

言語はJavaを使用してください。

■資料
AegeanSea.zip:
zipファイルを解答してお使いください。
以下の4つファイルが入っています。

answer.txt
解答用サンプルファイルです。

AegeanSea1.csv
エーゲ海の島データです。

AegeanSea2.csv
エーゲ海の島データです。

AegeanSea3.csv
エーゲ海の島データです。

■解答方法
解答用サンプルファイルに書かれていることにしたがい、解答ファイルを作成し、完成したらファイルアップロードにて提出してください。
*/
public class Q797{
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q797/AegeanSea3.csv");
	
	/**
	 * 右下捜索
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{

		//地図読み取り
		List<List<Integer>> rowData = Files.readAllLines(INPUT_FILE,CHARSET).stream().map(s -> {
			return Arrays.asList(s.split(",")).stream().map(d -> Integer.parseInt(d)).collect(Collectors.toList());
		}).collect(Collectors.toList());;
		
		BitMap map = new BitMap(rowData);
		GroupManager manager = new GroupManager();
		
		//ビットが立っているもののみ実行
		map.forEachOn(current->{
			
			//自分が所属するグループを取得する
			Group currentGroup = manager.findOrCreateGroup(current);					
				
			//下方向の捜索
			if(current.under().isOn()){	
				//現在グループに所属させる
				currentGroup.join(current.under());			
			}
			
			//右方向の捜索と現在グループとの関連づけ
			if(current.right().isOn()){

				Position right = current.right();
				
				//右側は既に別のグループに所属している可能性がある
				if(manager.isJoinedOtherGroup(right)){
					//既存のグループに自分が現在所属するグループを合成する
					manager.mergeGroup(right,currentGroup);
				}else{
					//現在グループに所属させる
					currentGroup.join(right);
				}
			}				
		});
		//結果表示
		manager.show();
	}

}