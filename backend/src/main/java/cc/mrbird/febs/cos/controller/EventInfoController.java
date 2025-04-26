package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.EventDetail;
import cc.mrbird.febs.cos.entity.EventInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IEventDetailService;
import cc.mrbird.febs.cos.service.IEventInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 用户场景事件 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/event-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventInfoController {

    private final IEventInfoService eventInfoService;

    private final IEventDetailService eventDetailService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取场景事件信息
     *
     * @param page      分页对象
     * @param eventInfo 场景事件信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<EventInfo> page, EventInfo eventInfo) {
        return R.ok(eventInfoService.queryEventPage(page, eventInfo));
    }

    /**
     * 查询场景事件信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(eventInfoService.getById(id));
    }

    /**
     * 查询场景事件信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(eventInfoService.list());
    }

    /**
     * 新增场景事件信息
     *
     * @param eventInfo 场景事件信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(EventInfo eventInfo) throws FebsException {
        eventInfo.setCode("EVE-" + System.currentTimeMillis());
        eventInfo.setCreateDate(DateUtil.formatDateTime(new Date()));

        // 获取用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, eventInfo.getUserId()));
        if (userInfo != null) {
            eventInfo.setUserId(userInfo.getId());
        }

        // 解析事件详情
        if (StrUtil.isEmpty(eventInfo.getEventDetail())) {
            throw new FebsException("事件详情不能为空");
        }
        eventInfoService.save(eventInfo);

        List<EventDetail> eventDetailList = JSONUtil.toList(eventInfo.getEventDetail(), EventDetail.class);
        for (EventDetail eventDetail : eventDetailList) {
            eventDetail.setEventId(eventInfo.getId());
        }
        eventDetailService.saveBatch(eventDetailList);
        return R.ok(true);
    }

    /**
     * 获取事件详情
     *
     * @param eventId 事件ID
     * @return 结果
     */
    @GetMapping("/queryEventDetail")
    public R queryEventDetail(Integer eventId) {
        return R.ok(eventInfoService.queryEventDetail(eventId));
    }

    /**
     * 修改场景事件信息
     *
     * @param eventInfo 场景事件信息
     * @return 结果
     */
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public R edit(EventInfo eventInfo) throws FebsException {
        // 解析事件详情
        if (StrUtil.isEmpty(eventInfo.getEventDetail())) {
            throw new FebsException("事件详情不能为空");
        }
        // 删除旧数据
        eventDetailService.remove(Wrappers.<EventDetail>lambdaQuery().eq(EventDetail::getEventId, eventInfo.getId()));

        List<EventDetail> eventDetailList = JSONUtil.toList(eventInfo.getEventDetail(), EventDetail.class);
        for (EventDetail eventDetail : eventDetailList) {
            eventDetail.setEventId(eventInfo.getId());
        }
        eventDetailService.saveBatch(eventDetailList);

        return R.ok(eventInfoService.updateById(eventInfo));
    }

    /**
     * 删除场景事件信息
     *
     * @param ids ids
     * @return 场景事件信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(eventInfoService.removeByIds(ids));
    }
}
