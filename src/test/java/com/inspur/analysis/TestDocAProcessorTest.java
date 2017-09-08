package com.inspur.analysis;

import com.inspur.analysis.tool.plugin.NlpProcessor;
import com.inspur.analysis.tool.plugin.data.UnData;
import com.inspur.analysis.tool.plugin.data.UnObject;

/**
 * Created by liutingna on 2017/9/8.
 */
public class TestDocAProcessorTest {
    public static void main(String[] args) {
        String mockCleanedContent = "浪潮集团有限公司，即浪潮集团，是中国本土综合实力强大的大型IT企业之一，国内领先的云计算领导厂商[1]  ，" +
                "先进的信息科技产品与解决方案服务商。浪潮集团旗下拥有浪潮信息、浪潮软件、浪潮国际、华光光电四家上市公司，业务涵盖系统与技术、" +
                "软件与服务、半导体三大产业群组，为全球八十多个国家和地区提供IT产品和服务。[2] \n" +
                "浪潮集团居2008年度中国大企业集团竞争力500强第3位[3]  ，中国电子信息百强企业第10位[4]  ，2014年中国软件业务收入百强企业第5位[5]" +
                "  ，2015年中国企业500强第244位[6]  ，2014年度全球增长最快服务器企业[7]  。浪潮服务器已连续17年蝉联国有品牌销量第一，" +
                "浪潮存储连续9年蝉联国有品牌销量第一，浪潮集团管理软件连续10年市场占有率第一[8]  。根据2015年一季度Gartner的统计数据，" +
                "浪潮全面超越IBM、惠普、戴尔等国际知名企业，以21%的服务器销售额占有率再次蝉联中国X86服务器市场第一。" +
                "这是公司过去5个季度中累计三个季度获得中国市场第一，在全球服务器市场占有率排名第五[9]  。" +
                "浪潮拥有中国IT领域唯一设在企业内的国家重点实验室“国家高效能服务器和存储技术重点实验室”[10]  ，" +
                "自主研发的中国第一款关键应用主机浪潮K1使中国成为继美日之后第三个掌握高端服务器核心技术的国家，" +
                "荣获2014年度国家科技进步一等奖[11]  ，浪潮天梭K系统已超过美国甲骨文，跻身高端Unix服务器市场的前三强[12]  。" +
                "2016年8月，浪潮集团在\"2016中国企业500强\"中排名第218位。";

        UnData unData = new UnData("testUnDataName", "", "", "");
        unData.setCleanedData(mockCleanedContent);

        NlpProcessor nlpProcessor = new TestDocAProcessor();
        UnObject object = nlpProcessor.process(unData);
        System.out.println(object);

    }
}