package com.inspur.analysis;

import com.inspur.analysis.tool.plugin.UnDataClean;
import com.inspur.analysis.tool.plugin.data.UnData;

/**
 * Created by liutingna on 2017/9/8.
 */
public class NullClean implements UnDataClean {
    @Override
    public UnData clean(UnData unData) {
        //对只做简单的拷贝，不进行具体的清理操作
        unData.setCleanedData(unData.getContent());
        return unData;
    }
}
