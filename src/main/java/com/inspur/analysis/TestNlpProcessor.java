package com.inspur.analysis;

import com.alibaba.fastjson.JSONObject;
import com.inspur.analysis.tool.plugin.NlpProcessor;
import com.inspur.analysis.tool.plugin.data.Property;
import com.inspur.analysis.tool.plugin.data.UnData;
import com.inspur.analysis.tool.plugin.data.UnObject;
import com.inspur.analysis.util.ZHttpRequest;

import java.io.IOException;
import java.util.*;

/**
 * Created by liutingna on 2017/9/15.
 * 调用NLP小组提供的服务进行提取关键词的插件
 */
public class TestNlpProcessor implements NlpProcessor {

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

        List<String> keywordList = getNlpKeywords(data.getCleanedData());

        //设置对象的名称
        unObject.setObjectName(data.getName());
        //设置对象的属性

        Property keywords=new Property("data.property.string","keywords",objectUri);
        keywords.setPropertyValue(keywordList);
        keywords.setUnique(true);
        unObject.setPropertyList(Arrays.asList(new Property[]{keywords}));
        //返回本体对象
        System.out.println("OK, we get the processed unObject: " + unObject);
        return unObject;
    }

    private List<String> getNlpKeywords(String content){
        return getNlpKeywords(content, -1);
    }

    private List<String> getNlpKeywords(String content, int limit){
        String reqUrl = "http://10.110.13.171:9080/nlp-core-service/nlp/annotators/keywordTextRank";
        Object retObj = callNlpService(reqUrl, content);
        JSONObject jsonObject = JSONObject.parseObject(retObj.toString());
        Map retMap = JSONObject.toJavaObject(jsonObject, Map.class);
        List<Map> rawKeywords = (List) retMap.get("keywords");
        List<String> keywords = new ArrayList<>();
        int retN=5;//默认返回5个关键词
        if (limit > 0) {
            retN = limit;
        }
        int count = 0;
        for (Map map : rawKeywords) {
            Object keyword = map.get("keyword");
            if (null != keyword) {
                keywords.add(keyword.toString());
                count++;
                if (count == 5) {
                    break;
                }
            }
        }
        return keywords;
    }

    private Object callNlpService(String requestUrl, String content){
        ZHttpRequest request = new ZHttpRequest();
        String ret;
        try {
            Map<String, String> param = new HashMap<>();
            param.put("text",content);
            param.put("access_token", "123456");
            param.put("lang", "Chinese");
            param.put("output_format", "json");
//            ret = request.post(requestUrl, param, null);
            ret = request.post(requestUrl, param,"utf-8");
            return ret;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static void main(String[] args) {
        String content = "张燕玲：《厦门宣言》第五部分就是加强人文交流合作的内容，这次会议的附件还公布了20个人文交流合作的文件，包括你刚才说的图书馆、博物馆、美术馆、青少年儿童戏剧联盟，医药、教育、媒体、政党、智库、民间社团组织方方面面的行动计划在那个附件里有。这些里边看了以后就感觉到体现了“亲”字，亲切、和谐，还有非常近，像朋友亲戚一样的，非常非常近。现在好多国外的媒体也谈到了，像中国的上海、北京，俄罗斯的莫斯科，巴西的里约热内卢很多的地方都成为了五国人民旅游的目的地，拉近了金砖国家民众的距离，这实际上是金砖真正的黏合剂。除了领导人的会晤，老百姓也有黏合剂，这样能保证金砖一切顺利进展。\n" +
                "\n" +
                "主持人：昨天举行的中外记者会上听到了非常具有创新型的词语，就是“金砖+”，什么是“金砖+”模式呢？\n" +
                "\n" +
                "张燕玲：“金砖+”受到了广泛的欢迎和追捧。大家都知道“金砖+”，它是金砖国家和其它新兴市场和发展中国家对话、合作、模式的建立，这是时代发展的需要，也是这些国家的诉求。发达国家主导世界治理，制定地球村的游戏规则，同时它们还当了裁判长，发展中国家只有执行的义务。21世纪以来世界格局发生了深刻的变化，出现了引领世界发展的新兴经济体，特别是美国次贷危机发生以后，金砖国家对世界经济增长贡献率超过50%，所以西方国家也是西方国家经济学家总结出了“BRICS”就形容这些国家在全球经济疲软情况下，它们不软，砖头还是挺硬的。同时认为如果不出现意外到2035年将超过G7。\n" +
                "\n" +
                "以美国为首的西方又提出了去国际化，搞贸易保护主义、民粹主义，以金砖国家为核心的新兴经济体和发展中国家，我们刚发展，他们又提出去全球化。在这样的环境下，我们必须得要加紧合作，要保护发展的环境，不能再允许他们随意割韭菜，弱肉强食的丛林法则，和零合游戏不再符合时代的逻辑，和平发展、合作、共赢成为世界人民的共同呼声。";
        TestNlpProcessor processor = new TestNlpProcessor();
        List<String> keywords = processor.getNlpKeywords(content);
        System.out.println(keywords);
    }

}
