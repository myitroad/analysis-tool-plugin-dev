package com.inspur.analysis;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.inspur.analysis.tool.plugin.NlpProcessor;
import com.inspur.analysis.tool.plugin.data.Property;
import com.inspur.analysis.tool.plugin.data.UnData;
import com.inspur.analysis.tool.plugin.data.UnDataSourceRecord;
import com.inspur.analysis.tool.plugin.data.UnObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liutingna on 2017/9/8.
 */
public class TestDocAProcessor implements NlpProcessor {
    //创建分词器
    private Segment segment = HanLP.newSegment().enableAllNamedEntityRecognize(true).enableOffset(true);

    @SuppressWarnings("unchecked")
    @Override
    public UnObject process(UnData data) {
        System.out.println("Process data is:"+data);
        System.out.println("Process data content is:" + data.getContent());
        if (data.getCleanedData() == null || data.getCleanedData().equals("")) {
            System.out.println("Warning: Preclean seems failed, use raw-content instead of  cleaned-content");
            data.setCleanedData(data.getContent());
        }
        //创建本体对象
        String objectUri = "data.object.file.document.paper";
        UnObject unObject = new UnObject(objectUri);
        unObject.setEditor("Inspur Test");
        System.out.println("Segment ins: "+segment);
        if (segment == null) {
            segment=HanLP.newSegment().enableAllNamedEntityRecognize(true).enableOffset(true);
        }
        List<Term> result = segment.seg(data.getCleanedData());
        List<String> nr = new ArrayList<>();
        List<String> ntc = new ArrayList<>();
        List<UnDataSourceRecord> nameDSR = new ArrayList<>();
        List<UnDataSourceRecord> companyDSR = new ArrayList<>();
        List<String> keywordList = HanLP.extractKeyword(data.getCleanedData(), 5);
        System.out.println("Get keywords: "+keywordList);
        List<String> sentenceList = HanLP.extractSummary(data.getCleanedData(), 3);
        System.out.println("Get summary: " + sentenceList);
        for (Term t : result) {
            //提取人名
            if (t.nature.startsWith("nr")) {
                UnDataSourceRecord tmp = new UnDataSourceRecord();
                tmp.setStartPosition(t.offset);
                tmp.setEndPosition(t.offset + t.word.length());
                nameDSR.add(tmp);
                nr.add(t.word);
            }
            //提取公司名
            if (t.nature.startsWith("ntc")) {
                UnDataSourceRecord tmp = new UnDataSourceRecord();
                tmp.setStartPosition(t.offset);
                tmp.setEndPosition(t.offset + t.word.length());
                companyDSR.add(tmp);
                ntc.add(t.word);
            }
        }
        //本体的人名属性
        Property name = new Property("data.property.string", "title", objectUri);
        //设置属性的值
        name.setPropertyValue(nr);
        //设置数据源信息
        name.setDataSourceRecordList(nameDSR);

        //设置对象的名称
        unObject.setObjectName(data.getName());
        //设置对象的属性

        Property keywords=new Property("data.property.string","keywords",objectUri);
        keywords.setPropertyValue(keywordList);
        keywords.setUnique(true);
        Property summery=new Property("data.property.string","abstract",objectUri);
        summery.setPropertyValue(sentenceList);
        summery.setUnique(true);
        unObject.setPropertyList(Arrays.asList(new Property[]{name,keywords,summery}));
        //返回本体对象
        System.out.println("OK, we get the processed unObject: " + unObject);
        return unObject;
    }
}