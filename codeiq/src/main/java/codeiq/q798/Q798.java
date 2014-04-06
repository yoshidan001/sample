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
 *キャサリンは、フルーツ販売している会社のシステム部でログデータを使った分析をやっています。
ログデータには、フルーツが売れると、その名前が出力されるようになっています。

販売しているフルーツは、りんご、いちご、メロンの3種類のみです。

ある日、キャサリンはログデータの一部を使って、家で分析の仕事していたところ、
なんと息子のジョージがいたずらをして、ログファイルがぐちゃぐちゃになってしまいました。

ウィリアム上司から、明日までにデータを集計してくれといわれているので、
ログデータを再取得しなおす暇はありません。

データは、目も当てられないほどぐちゃぐちゃになってしまったので、さぁ大変です。

そこでキャサリンは、ひとまず区切り文字が一致している中で最もフルーツが多くあるデータだけ抜き出すことにしました。
区切り文字は{}()[]の3種類のみです。

例えば、
{apple strawberry (melon [ apple )}

というデータでは、{}と()の区切り文字はペアで存在しますが、[ は、]がありませんので、
取得できるデータは、"{apple strawberry melon apple}"と、"(melon apple)"の2つになります。
{}の方がフルーツの数が4個、()のフルーツの数が2個なので、
このログデータから取得できるフルーツの最大数は、4となります。

幾つかサンプルのデータとフルーツの数の例を示します。

【ログデータ】
{melon (()melon strawberry)][apple}
[apple apple }{melon](strawberry}(melon]]
({}apple) melon strawberry{melon(apple apple) melon strawberry}

【フルーツの数】
4
3
5

サンプルのように、一致する区切り文字の中で、最も多くのフルーツを持っているものを探し出し、いくつフルーツが入っているかを数えてください。

プログラミング言語はJavaを使ってください。	

*/
public class Q798 {
	
	/** ログの正規表現 */
	private static final List<Pattern> LOG_PATTERNS = Arrays.asList(new Pattern[]{Pattern.compile("\\{([^\\}]+)\\}"),Pattern.compile("\\(([^\\)]+)\\)"),Pattern.compile("\\[([^\\]]+)\\]")});
	
	/** ログ内フルーツの正規表現 */
	private static final Pattern FRUITS_PATTERN = Pattern.compile("([a-z]+)");
	
	private static final Path INPUT_FILE = Paths.get("src/main/resources/q798/fruits.log");
	
	private static final Charset CHARSET = Charset.forName("Shift_JIS");
	
	public static void main(String[] args) throws IOException {
	
		try(Stream<String> stream = Files.lines(INPUT_FILE,CHARSET)){
			stream.forEach(line -> {
				//行毎の正規表現調査は並列処理
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
