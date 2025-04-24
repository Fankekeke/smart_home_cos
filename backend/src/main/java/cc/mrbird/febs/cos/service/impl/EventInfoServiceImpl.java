package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.EventInfo;
import cc.mrbird.febs.cos.dao.EventInfoMapper;
import cc.mrbird.febs.cos.service.IEventInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户场景事件 实现层
 *
 * @author FanK
 */
@Service
public class EventInfoServiceImpl extends ServiceImpl<EventInfoMapper, EventInfo> implements IEventInfoService {

    /**
     * 分页获取场景事件信息
     *
     * @param page      分页对象
     * @param eventInfo 场景事件信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryEventPage(Page<EventInfo> page, EventInfo eventInfo) {
        return baseMapper.queryEventPage(page, eventInfo);
    }

    /**
     * 获取事件详情
     *
     * @param eventId 事件ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryEventDetail(Integer eventId) {
        return baseMapper.queryEventDetail(eventId);
    }
}
