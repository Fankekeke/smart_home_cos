package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.OperateRecordInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 操作记录 service层
 *
 * @author FanK
 */
public interface IOperateRecordInfoService extends IService<OperateRecordInfo> {

    /**
     * 分页获取操作记录信息
     *
     * @param page              分页对象
     * @param operateRecordInfo 操作记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOperateRecordPage(Page<OperateRecordInfo> page, OperateRecordInfo operateRecordInfo);
}
