package lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import lucene.analyzer.AsIKAnalyzer;

/**
 * 〈〉<br>
 *
 * @author 0027009101
 * @create 2024/5/28
 * @since 1.0.0
 */
public class MarkdownSection {
    private String fileName;
    private String header;
    private String content;

    public MarkdownSection(String fileName, String header, String content) {
        this.fileName = fileName;
        this.header = header;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public static void main(String[] args) throws Exception {
        // 使用 IKAnalyzer 进行分词
        Analyzer analyzer = new AsIKAnalyzer(true);

        // 创建内存索引
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        // 添加文档
        addDoc(writer, "用户管理");
        addDoc(writer, "系统管理");
        addDoc(writer, "用户");
        addDoc(writer, "管理");
        addDoc(writer, "用");
        addDoc(writer, "户");
        addDoc(writer, "户用");
        writer.close();

        // 使用 IKAnalyzer 进行分词
        Analyzer analyzer1= new StandardAnalyzer();

        // 创建内存索引
        Directory directory1 = new RAMDirectory();
        IndexWriterConfig config1 = new IndexWriterConfig(analyzer1);
        IndexWriter writer1 = new IndexWriter(directory1, config1);
        addDoc(writer1, "Menu: User And Role Management");
        addDoc(writer1, "User Management");
        addDoc(writer1, "User");
        addDoc(writer1, "management");
        addDoc(writer1, "log management");
        addDoc(writer1, "log");
        addDoc(writer1, "l management");
        addDoc(writer1, "lvg management");
        writer1.close();

        // 搜索
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        DirectoryReader reader1 = DirectoryReader.open(directory1);
        IndexSearcher searcher1 = new IndexSearcher(reader1);

        // 使用单个字进行搜索
        searchBySingleChar(searcher, analyzer, "用");
        searchBySingleChar(searcher, analyzer, "户");
        searchBySingleChar(searcher, analyzer, "用户");
        searchBySingleChar(searcher, analyzer, "用户管理");
        searchBySingleChar(searcher, analyzer, "错误");

        // 使用单个字进行搜索
        searchBySingleChar(searcher1, analyzer1, "User Management");
        searchBySingleChar(searcher1, analyzer1, "User");
        searchBySingleChar(searcher1, analyzer1, "management");
        searchBySingleChar(searcher1, analyzer1, "log management");
        searchBySingleChar(searcher1, analyzer1, "lo");
        searchBySingleChar(searcher1, analyzer1, "l");
        searchBySingleChar(searcher1, analyzer1, "o");
        searchBySingleChar(searcher1, analyzer1, "lov");

        reader.close();
        directory.close();
        reader1.close();
        directory1.close();
    }

    private static void addDoc(IndexWriter writer, String title) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        writer.addDocument(doc);
    }

    private static void searchBySingleChar(IndexSearcher searcher, Analyzer analyzer, String searchText) throws Exception {
        // 使用 TermQuery 进行精确匹配
        Query query = new TermQuery(new Term("title", searchText));

        QueryParser parser = new QueryParser("title", analyzer);
        Query query1 = parser.parse(searchText);

// 创建一个 WildcardQuery 查询
        Term term = new Term("title", searchText + "*");
        Query query3 = new WildcardQuery(term);
        Query query2 = new FuzzyQuery(new Term("title", searchText));
        BoostQuery boostedPhraseQuery = new BoostQuery(query, 5f);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        //builder.add(query, BooleanClause.Occur.SHOULD);
        builder.add(query1, BooleanClause.Occur.SHOULD);
        builder.add(query3, BooleanClause.Occur.SHOULD);
        builder.add(boostedPhraseQuery, BooleanClause.Occur.SHOULD);
        BooleanQuery booleanQuery = builder.build();
        // 搜索并输出结果
        ScoreDoc[] hits = searcher.search(booleanQuery, 10).scoreDocs;
        System.out.println("搜索 " + searchText + " 结果:");
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            System.out.println("Title: " + doc.get("title") + "   Score:" + hit.score);
        }

    }
}