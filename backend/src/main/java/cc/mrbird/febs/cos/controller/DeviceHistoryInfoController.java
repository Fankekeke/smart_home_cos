package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceHistoryInfo;
import cc.mrbird.febs.cos.service.IDeviceHistoryInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 设备上报历史数据 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/device-history-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceHistoryInfoController {

    private final IDeviceHistoryInfoService deviceHistoryInfoService;

    /**
     * 分页获取设备上报历史数据信息
     *
     * @param page              分页对象
     * @param deviceHistoryInfo 设备上报历史数据信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DeviceHistoryInfo> page, DeviceHistoryInfo deviceHistoryInfo) {
        return R.ok(deviceHistoryInfoService.selectHistoryPage(page, deviceHistoryInfo));
    }

    /**
     * 根据设备ID获取历史记录
     *
     * @param deviceId 设备ID
     * @return 结果
     */
    @GetMapping("/selectHistoryByDevice/{deviceId}")
    public R selectHistoryByDevice(@PathVariable("deviceId") Integer deviceId) {
        return R.ok(deviceHistoryInfoService.list(Wrappers.<DeviceHistoryInfo>lambdaQuery().eq(DeviceHistoryInfo::getDeviceId, deviceId)));
    }

    /**
     * 根据设备ID获取统计信息
     *
     * @param deviceId 设备ID
     * @param date     统计日期
     * @return 结果
     */
    @GetMapping("/selectHistoryByDevice")
    public R selectRateByDeviceId(@RequestParam("deviceId") Integer deviceId, @RequestParam(value = "date", required = false) String date) {
        return R.ok(deviceHistoryInfoService.selectRateByDeviceId(deviceId, date));
    }

    /**
     * 根据设备ID获取历史数据
     *
     * @param deviceId 设备ID
     * @return 结果
     */
    @GetMapping("/selectHistoryByDeviceId")
    public R selectHistoryByDeviceId(Integer deviceId) {
        return R.ok(deviceHistoryInfoService.list(Wrappers.<DeviceHistoryInfo>lambdaQuery().eq(DeviceHistoryInfo::getDeviceId, deviceId)));
    }

    /**
     * 查询设备上报历史数据信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(deviceHistoryInfoService.getById(id));
    }

    /**
     * 查询设备上报历史数据信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(deviceHistoryInfoService.list());
    }

    /**
     * 新增设备上报历史数据信息
     *
     * @param deviceHistoryInfo 设备上报历史数据信息
     * @return 结果
     */
    @PostMapping
    public R save(DeviceHistoryInfo deviceHistoryInfo) {
        // 上报时间
        deviceHistoryInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(deviceHistoryInfoService.save(deviceHistoryInfo));
    }

    /**
     * 修改设备上报历史数据信息
     *
     * @param deviceHistoryInfo 设备上报历史数据信息
     * @return 结果
     */
    @PutMapping
    public R edit(DeviceHistoryInfo deviceHistoryInfo) {
        return R.ok(deviceHistoryInfoService.updateById(deviceHistoryInfo));
    }

    /**
     * 删除设备上报历史数据信息
     *
     * @param ids ids
     * @return 设备上报历史数据信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(deviceHistoryInfoService.removeByIds(ids));
    }
}
