package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceHistoryInfo;
import cc.mrbird.febs.cos.service.IDeviceHistoryInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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