package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.EventDetail;
import cc.mrbird.febs.cos.dao.EventDetailMapper;
import cc.mrbird.febs.cos.service.IEventDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 场景事件详情 实现层
 *
 * @author FanK
 */
@Service
public class EventDetailServiceImpl extends ServiceImpl<EventDetailMapper, EventDetail> implements IEventDetailService {

}
