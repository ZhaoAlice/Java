package lucene.analyzer;

/**
 * @Author WCJ
 * @Description
 **/
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public final class AsIKAnalyzer extends Analyzer {
    private boolean useSmart;

    public boolean isUseSmart() {
        return this.useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public AsIKAnalyzer() {
        this(false);
    }

    @Override
    protected TokenStreamComponents createComponents(String s) {
        Tokenizer _MyIKTokenizer = new AsIKTokenizer(this.isUseSmart());
        return new TokenStreamComponents(_MyIKTokenizer);
    }

    public AsIKAnalyzer(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public static void main(String[] args) throws Exception {
        // 使用 IKAnalyzer 进行分词
        Analyzer analyzer = new AsIKAnalyzer(true);

        // 创建内存索引
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        // 添加文档
        addDoc(writer, "用户反馈");
        addDoc(writer, "管理员");
        addDoc(writer, "用户管理");
        addDoc(writer, "用户体验");

        writer.close();

        // 搜索
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        // 使用前缀进行搜索
        searchByPrefix(searcher, "用户");

        reader.close();
        directory.close();
    }

    private static void addDoc(IndexWriter writer, String title) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        writer.addDocument(doc);
    }

    private static void searchByPrefix(IndexSearcher searcher, String prefix) throws Exception {
        // 使用 PrefixQuery 进行前缀匹配
        Query query = new WildcardQuery(new Term("title", prefix));

        // 搜索并输出结果
        ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
        System.out.println("搜索前缀 " + prefix + " 结果:");
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            System.out.println("Title: " + doc.get("title"));
        }
    }
}


