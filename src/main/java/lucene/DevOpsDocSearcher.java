package lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.*;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DevOpsDocSearcher {
	static String dstDoc1 = "D:\\work\\project\\2024\\ai相关\\doc\\dist\\bm25\\15";
	static String dstDoc2 = "D:\\work\\project\\2024\\ai相关\\doc\\dist\\bm25\\16";
	static String dstDoc3 = "D:\\work\\project\\2024\\ai相关\\doc\\dist\\bm25\\17";

	public static void main(String[] args) throws Exception {

//		Directory directory = new MMapDirectory(Paths.get(dstDoc1));
//		Analyzer analyzer = new StandardAnalyzer();
//
//		long sDate = System.currentTimeMillis();
//		System.out.println("begin: " + sDate);
//
//		IndexReader reader = DirectoryReader.open(directory);
//		IndexSearcher searcher = new IndexSearcher(reader);
//		searcher.setSimilarity(new BM25Similarity());
//		QueryParser parser = new QueryParser("content", analyzer);
//		Query query = parser.parse("菜单");
//		// 创建权限过滤查询条件
//		Query excludeFilter = new QueryParser("filename", new StandardAnalyzer()).parse("-filename:xxxxxx.md");
//		// 将基本查询和过滤查询组合起来
//		BooleanQuery booleanQuery = new BooleanQuery.Builder()
//						.add(query, BooleanClause.Occur.MUST) // 必须满足基本查询
//						.add(excludeFilter, BooleanClause.Occur.FILTER) // 应用过滤条件
//						.build();
//		TopDocs results = searcher.search(query, 10);
//		System.out.println("Total hits: " + results.totalHits.value);
//		for (ScoreDoc scoreDoc : results.scoreDocs) {
//			Document doc = searcher.doc(scoreDoc.doc);
////			System.out.println("---------------score:" + scoreDoc.score);
////			System.out.println("fileName: " + doc.get("fileName"));
////			System.out.println("header: " + doc.get("header"));
////			System.out.println("content.length: " + doc.get("content").length());
//		}
//		reader.close();
//
//		long eDate = System.currentTimeMillis();
//		System.out.println("end: " + eDate + " --> cost time(ms): "+ (eDate - sDate));

		List<String> topMenus = new ArrayList<>();
		topMenus.add("ww");
		topMenus.add("wwxxx");
		topMenus.add("xx");
		List<String> codes = new ArrayList<>();
		codes.add("ww");
		codes.add("xx");
		codes.add("wwxxx");
		sortByCodes(topMenus, codes);
		System.out.println(topMenus);
	}

	private static void sortByCodes(List<String> topMenus, List<String> codes) {
		topMenus.sort((item1, item2) -> {
			int index1 = codes.indexOf(item1);
			if (index1 == -1) {
				return 1;
			}
			int index2 = codes.indexOf(item2);
			if (index2 == -1) {
				return -1;
			}
			return index1 - index2;
		});
	}
}
