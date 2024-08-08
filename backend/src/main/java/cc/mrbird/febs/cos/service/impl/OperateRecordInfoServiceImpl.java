package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OperateRecordInfo;
import cc.mrbird.febs.cos.dao.OperateRecordInfoMapper;
import cc.mrbird.febs.cos.service.IOperateRecordInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 操作记录 实现层
 *
 * @author FanK
 */
@Service
public class OperateRecordInfoServiceImpl extends ServiceImpl<OperateRecordInfoMapper, OperateRecordInfo> implements IOperateRecordInfoService {

    /**
     * 分页获取操作记录信息
     *
     * @param page              分页对象
     * @param operateRecordInfo 操作记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOperateRecordPage(Page<OperateRecordInfo> page, OperateRecordInfo operateRecordInfo) {
        return baseMapper.selectOperateRecordPage(page, operateRecordInfo);
    }
}
