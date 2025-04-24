package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.EventInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户场景事件 service层
 *
 * @author FanK
 */
public interface IEventInfoService extends IService<EventInfo> {

    /**
     * 分页获取场景事件信息
     *
     * @param page      分页对象
     * @param eventInfo 场景事件信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryEventPage(Page<EventInfo> page, EventInfo eventInfo);

    /**
     * 获取事件详情
     *
     * @param eventId 事件ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> queryEventDetail(Integer eventId);
}
