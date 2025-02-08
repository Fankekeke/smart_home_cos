package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceAlertInfo;
import cc.mrbird.febs.cos.entity.DeviceInfo;
import cc.mrbird.febs.cos.service.IDeviceAlertInfoService;
import cc.mrbird.febs.cos.service.IDeviceInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 设备报警配置 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/device-alert-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceAlertInfoController {

    private final IDeviceAlertInfoService deviceAlertInfoService;
    private final IDeviceInfoService deviceInfoService;

    /**
     * 分页获取设备报警配置信息
     *
     * @param page            分页对象
     * @param deviceAlertInfo 设备报警配置信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DeviceAlertInfo> page, DeviceAlertInfo deviceAlertInfo) {
        return R.ok(deviceAlertInfoService.selectDeviceAlertPage(page, deviceAlertInfo));
    }

    /**
     * 查询设备报警配置信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(deviceAlertInfoService.getById(id));
    }

    /**
     * 查询设备报警配置信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(deviceAlertInfoService.list());
    }

    /**
     * 新增设备报警配置信息
     *
     * @param deviceAlertInfo 设备报警配置信息
     * @return 结果
     */
    @PostMapping
    public R save(DeviceAlertInfo deviceAlertInfo) throws FebsException {
        int count = deviceAlertInfoService.count(Wrappers.<DeviceAlertInfo>lambdaQuery().eq(DeviceAlertInfo::getDeviceId, deviceAlertInfo.getDeviceId()));
        if (count > 0) {
            throw new FebsException("改设备已经绑定报警配置");
        }
        // 获取设备所属用户
        DeviceInfo deviceInfo = deviceInfoService.getById(deviceAlertInfo.getDeviceId());
        if (deviceInfo != null) {
            deviceAlertInfo.setUserId(deviceInfo.getUserId());
        }
        // 上报时间
        deviceAlertInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(deviceAlertInfoService.save(deviceAlertInfo));
    }

    /**
     * 修改设备报警配置信息
     *
     * @param deviceAlertInfo 设备报警配置信息
     * @return 结果
     */
    @PutMapping
    public R edit(DeviceAlertInfo deviceAlertInfo) throws FebsException {
        List<DeviceAlertInfo> deviceAlertInfoList = deviceAlertInfoService.list(Wrappers.<DeviceAlertInfo>lambdaQuery().eq(DeviceAlertInfo::getDeviceId, deviceAlertInfo.getDeviceId()));
        if (CollectionUtil.isNotEmpty(deviceAlertInfoList) && !deviceAlertInfoList.get(0).getId().equals(deviceAlertInfo.getId())) {
            throw new FebsException("改设备已经绑定报警配置");
        }
        return R.ok(deviceAlertInfoService.updateById(deviceAlertInfo));
    }

    /**
     * 删除设备报警配置信息
     *
     * @param ids ids
     * @return 设备报警配置信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(deviceAlertInfoService.removeByIds(ids));
    }
}
