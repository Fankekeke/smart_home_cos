package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OperateRecordInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 操作记录 mapper层
 *
 * @author FanK
 */
public interface OperateRecordInfoMapper extends BaseMapper<OperateRecordInfo> {

    /**
     * 分页获取操作记录信息
     *
     * @param page              分页对象
     * @param operateRecordInfo 操作记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOperateRecordPage(Page<OperateRecordInfo> page, @Param("operateRecordInfo") OperateRecordInfo operateRecordInfo);
}
