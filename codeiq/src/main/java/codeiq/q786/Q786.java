package codeiq.q786;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Q786 {

	public static void main(String[] args){
		String content = null;
		try (Stream<String> stream = Files.lines(Paths.get("hoge.txt"),Charset.forName("Shift_JIS"))) {
			 content = stream.collect(Collectors.joining());
			 System.out.println(content);
	    }catch(IOException ioe){
	    	throw new UncheckedIOException(ioe);
	    }
	}
	
	public List<String> combine(Map<String, String> map) {    
		return map.entrySet().stream().map(e->String.join(":", e.getKey(),e.getValue())).collect(Collectors.toList());
	}
	
}
