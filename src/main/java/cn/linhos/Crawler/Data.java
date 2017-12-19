package cn.linhos.Crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fprok on 2017/12/19.
 */

/**
 * 存储具体的采集信息
 */
public class Data implements Comparable<Data> {

    private String BookName;
    private float Source;
    private int EvaluationNumber;
    private String Author;
    private String Publishers;

    public Data() {
    }

    public Data(String bookName, float source, int evaluationNumber, String author, String publishers) {
        this.BookName = bookName;
        this.Source = source;
        this.EvaluationNumber = evaluationNumber;
        this.Author = author;
        this.Publishers = publishers;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }


    public float getSource() {
        return Source;
    }

    public void setSource(float source) {
        Source = source;
    }

    public int getEvaluationNumber() {
        return EvaluationNumber;
    }

    public void setEvaluationNumber(int evaluationNumber) {
        EvaluationNumber = evaluationNumber;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getPublishers() {
        return Publishers;
    }

    public void setPublishers(String publishers) {
        Publishers = publishers;
    }

    /**
     * 将Data 转换成List
     *
     * @param index
     * @return
     */
    public List<String> DataToList(int index) {
        List<String> list = new ArrayList<String>();
        list.add(Integer.toString(index));
        list.add(BookName);
        list.add(Float.toString(Source));
        list.add(Integer.toString(EvaluationNumber));
        list.add(Author);
        list.add(Publishers);
        return list;
    }


    @Override
    public int compareTo(Data o) {
        return this.EvaluationNumber - o.EvaluationNumber;

    }
}
