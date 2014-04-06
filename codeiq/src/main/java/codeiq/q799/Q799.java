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
かたやん印刷所では、いまの時代においてもグーテンベルグ印刷にこだわって印刷をしています。
グーテンベルグ印刷とは、活版（活字を組み合わせて作った版）で印刷する方法です。(Wikipedia)

かたやん印刷所の所長かたやんは、使った活字を元の場所に戻さないので、
いつも活字を入れているボックスはぐちゃぐちゃで、
ほしい活字をすぐに見つけ出すことができません。

そこで見かねた印刷所の副所長エミーは、活字を入れているボックスビデオで撮影し、
画像解析し、ボックスのどこにどの活字があるかを解析し、
ほしい文字列の活字がどこにあるか瞬時にみつけだすプログラミングを作ることにしました。

現状の状態のボックスは以下のようになっています。


6×6のボックスの中から"PRO, PROGRAMMER"という活字を探し出したいです。
カンマやスペースは1つの活字としてカウントします。
ですので、今回は文字13個、カンマ1個、スペース1個の合計15個の活字が必要です。
一度使った活字は2回使うことはできません。

活字の場所は、(縦番号,横番号)で表すと以下のような順番で活字を取り出すことができます。
縦番号は上から下の方向に、横番号は左から右の方向に、1から数えます。
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


上記の例では、例えば"P"という文字が2回でてきますが、(1,4)と(2,2)にある"P"はどちらを先につかってもかまいません。
また場所の特定は1つのみ記述すればよいです。すべての場所番号を書く必要はありません。

画像解析したボックスのデータはCSVで出力されます。
ボックスのデータを使ってほしい文字列を作るための活字の在り処を探しだしてください。

■資料
gutenberg_Java.zip
zipファイルを解凍すると以下のファイルが入っています。

gutenberg.csv
このボックスから"STAY HUNGRY, STAY FOOLISH"という活字を取り出してください。
文字21個、カンマ1個、スペース3個で、合計25個の活字が必要です。

answer.txt
解答用テキストファイルです。

■解答方法
解答用テキストファイルに書かれていることに従ってファイルを作成し、
完成したらファイルアップロードして提出ください。
*/

public class Q799 {
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q799/gutenberg.csv");
	
	private static final String TARGET = "STAY HUNGRY, STAY FOOLISH";
	
	public static void main(String[] args) throws IOException{
				
		//MapReduceを使用してハッシュデータ作成
		Map<String,List<Position>> index = createImageData();
	
		//ハッシュジョインで表示
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
	 * 画像データを読み込みハッシュを作成する.
	 * Java8のStreamAPIを使ってやってみる。
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Map<String,List<Position>> createImageData() throws IOException{
		
		//Y方向のカウンタ
		Supplier<Integer> counterForY = counter();		
				
		try(Stream<String> stream = Files.lines(INPUT_FILE,CHARSET)){	
			//変換
			return stream.map(s->{
				int y = counterForY.get();
				Map<String,List<Position>> datamap = new HashMap<>();
				//X方向のカウンタ
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
			//たたむ
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
	 * 入力ファイルは値1文字で""区切りと決まっているので、効率重視の決めうち読み込み。
	 * 奇数位置の文字のみ抽出する。
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
	 * 座標用のカウンタ
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
