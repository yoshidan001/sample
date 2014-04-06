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
 * 順位づけの方法
 *	点数が高い順に、1, 2, ...と順位をつけるだけですが、同じ得点が2人以上の場合は同じ順位になり、それより下の点数は順位がずれていきます。
 *	たとえば、80点が5位で2人いたとすると、79点の人は6位ではなく7位になります。80点が3人いれば、8位になります。
 *
 *・csvファイルについて
 *	csvファイルの文字コードはShift_JIS、改行コードはCR+LFになっています。
 *	出力データに関しては、文字コード・改行コードの指定は特にありません。
 *・出力データの項目名
 *	国語・数学・英語・社会・理科の順位に加えて、合計点の順位も出力することになりますので、1行目の項目名に「合計」を追加してください。
 *
 *・その他
 * 	本問の記述に関してあいまいな部分があると判断した場合は、どのように解釈・対応したかを、ソースコード内にコメントとして記述してください。
 */
public class Q787 {
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q787/class_3c_input.csv");

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		//合計計算用の疑似クロージャ
		Function<String,IndexList<String>> sum = sum();
	
		//縦横変換（科目単位に分割する）
		IndexList<List<String>> dataList = new IndexList<>();
		try(Stream<String> stream = Files.lines(INPUT_FILE,CHARSET)){
			stream.forEach(s->{
				sum.apply(s).forEachWithIndex((e,i)-> {
					List<String> subject = dataList.size() <= i ? dataList.append(new ArrayList<>()) : dataList.get(i);				
					subject.add(e);				
				});		
			});
		}
		
		//順位付け(科目ごとに独立なので並列処理可能）
		dataList.parallelStream().skip(1).forEach(i->{
			
			//順位保持用の疑似クロージャ
			Function<String,String> rank = rank();
	
			Map<String,String> array = i.stream().skip(1)
				.sorted((p,a)-> atoi(a) - atoi(p))
					.map(s->Arrays.asList(s,rank.apply(s)))
						.distinct()
							.collect(Collectors.toMap((k)->k.get(0), (v)->v.get(1)));	
			//点数を順位に置換
			i.replaceAll(s->array.containsKey(s) ? array.get(s) : s);
		});
		
		//縦横変換（書き込み単位に戻す）
		IndexList<StringBuilder> writeList = new IndexList<>();
		dataList.forEach(c->{
			new IndexList<String>(c).forEachWithIndex((e,i)->{;
				StringBuilder name = writeList.size() <= i ? writeList.append(new StringBuilder()) : writeList.get(i).append(",");				
				name.append(e);				
			});			
		});
						
		//ファイル出力
		Files.write(Paths.get(String.format("class_3c_output_%d.csv",System.currentTimeMillis())),writeList,CHARSET);
	}
	
	/**
	 * 文字列 to 数値
	 * @param value
	 * @return
	 */
	private static int atoi(String value){
		return Integer.parseInt(value);
	}
	
	/**
	 * 合計表示表の高階関数
	 * @return
	 */
	private static Function<String,IndexList<String>> sum(){
		boolean[] first = new boolean[]{true};
		return (String s) -> {
			IndexList<String> arrayList = new IndexList<>(Arrays.asList(s.split(",")));				
			if(first[0]){
				first[0] = false;
				arrayList.add("合計");
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
	 * ランキング表示用の高階関数
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