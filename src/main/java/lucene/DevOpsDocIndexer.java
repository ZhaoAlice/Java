package lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class DevOpsDocIndexer {
	static String srcDoc = "D:\\work\\project\\2024\\ai相关\\doc\\src\\doc";
	static String dstDoc = "D:\\work\\project\\2024\\ai相关\\doc\\dist\\bm25\\14";
	static IndexWriter indexWriter;

	static IndexWriter indexWriter1;
	static IndexWriter indexWriter2;
	static IndexWriter indexWriter3;
	static String dstDoc1 = "D:\\work\\project\\2024\\ai相关\\doc\\dist\\bm25\\15";
	static String dstDoc2 = "D:\\work\\project\\2024\\ai相关\\doc\\dist\\bm25\\16";
	static String dstDoc3 = "D:\\work\\project\\2024\\ai相关\\doc\\dist\\bm25\\17";

	static int fileCount = 0;
	static int sectionCount = 0;

	public static void main(String[] args) throws Exception {
		//
		//long sDate = System.currentTimeMillis();
		//System.out.println("begin: " + sDate);
		//
		//Directory directory = new MMapDirectory(Paths.get(dstDoc));
		//Analyzer analyzer = new StandardAnalyzer();
		//IndexWriterConfig config = new IndexWriterConfig(analyzer);
		//config.setSimilarity(new BM25Similarity());
		//config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		//indexWriter = new IndexWriter(directory, config);
		////获取所有的MD格式的文档
		//fetch();
		//indexWriter.commit();
		//indexWriter.close();
		//
		//long eDate = System.currentTimeMillis();
		//System.out.println("end: " + eDate + " --> cost time(ms): "+ (eDate - sDate));
		//System.out.println("fileCount: " + fileCount + " & sectionCount : "+ sectionCount);



		long ssDate = System.currentTimeMillis();
		System.out.println("ss begin: " + ssDate);

		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config1 = new IndexWriterConfig(analyzer);
		config1.setSimilarity(new BM25Similarity());
		config1.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriterConfig config2 = new IndexWriterConfig(analyzer);
		config2.setSimilarity(new BM25Similarity());
		config2.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriterConfig config3 = new IndexWriterConfig(analyzer);
		config3.setSimilarity(new BM25Similarity());
		config3.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

		Directory directory1 = new MMapDirectory(Paths.get(dstDoc1));
		Directory directory2 = new MMapDirectory(Paths.get(dstDoc2));

		Directory directory3 = new MMapDirectory(Paths.get(dstDoc3));

		indexWriter1 = new IndexWriter(directory1, config1);
		indexWriter2 = new IndexWriter(directory2, config2);
		indexWriter3 = new IndexWriter(directory3, config3);
		//获取所有的MD格式的文档
		fetch();
		indexWriter1.commit();
		indexWriter1.close();
		indexWriter2.commit();
		indexWriter2.close();
		indexWriter3.commit();
		indexWriter3.close();

		long eDate1 = System.currentTimeMillis();
		System.out.println("end: " + eDate1 + " --> cost time(ms): "+ (eDate1 - ssDate));
		System.out.println("fileCount: " + fileCount + " & sectionCount : "+ sectionCount);
	}

	public static void fetch() {
		// 设置目录路径
		Path dirPath = Paths.get(srcDoc);
		// 使用try-with-resources来自动关闭资源
		try (Stream<Path> stream = Files.walk(dirPath)) {
			stream
							// 确保是文件而非目录
							.filter(Files::isRegularFile)
							// 过滤出.md扩展名的文件
							.filter(path -> path.toString().endsWith(".md"))
							// 打印文件名称
							.forEach(DevOpsDocIndexer::bm25FileStore);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void bm25FileStore(Path filePath)  {
		fileCount++;
		Stream<MarkdownSection> markdownSections = processMarkdownFile(filePath);

		markdownSections.forEach(DevOpsDocIndexer::addDoc);


	}

	private static void addDoc(MarkdownSection section) {
		for (int i = 0 ; i < 100; i++) {
			sectionCount++;
			Document doc = new Document();
//		String uniqueId = section.getFileName()+"｜"+section.getHeader();
			String uniqueId = System.currentTimeMillis() + "" + Math.random();
			doc.add(new StringField("id", uniqueId, Field.Store.YES));
			doc.add(new StringField("fileName", section.getFileName(), Field.Store.YES));
			doc.add(new StringField("header", section.getHeader(), Field.Store.YES));
			doc.add(new TextField("content", section.getContent(), Field.Store.YES));
			try {
				Term idTerm = new Term("id", uniqueId);
				if (fileCount % 3 == 0) {
					indexWriter1.updateDocument(idTerm, doc);
				}
				else if (fileCount % 3 == 1) {
					indexWriter2.updateDocument(idTerm, doc);
				}
				else {
					indexWriter3.updateDocument(idTerm, doc);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Stream<MarkdownSection> processMarkdownFile(Path filePath) {
		try {
			List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
			return splitByHeaders(filePath.getFileName().toString(), lines).stream();
		} catch (IOException e) {
			e.printStackTrace();
			return Stream.empty();
		}
	}

	private static List<MarkdownSection> splitByHeaders(String fileName, List<String> lines) {
		List<MarkdownSection> sections = new ArrayList<>();
		StringBuilder currentContent = new StringBuilder();
		String currentHeader = null;

		Pattern headerPattern = Pattern.compile("^#+\\s+(.*)");

		for (String line : lines) {
			if (headerPattern.matcher(line).matches()) {
				if (currentHeader != null) {
					sections.add(new MarkdownSection(fileName, currentHeader, currentContent.toString()));
					currentContent = new StringBuilder();
				}
				currentHeader = line.replaceFirst("^#+\\s+", "");
			} else {
				currentContent.append(line).append("\n");
			}
		}

		if (currentHeader != null && currentContent.length() > 0) {
			sections.add(new MarkdownSection(fileName, currentHeader, currentContent.toString()));
		}

		return sections;
	}
}
