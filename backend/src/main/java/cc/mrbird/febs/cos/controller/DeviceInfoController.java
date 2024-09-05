package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceInfo;
import cc.mrbird.febs.cos.entity.DeviceOfflineRecord;
import cc.mrbird.febs.cos.entity.OperateRecordInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IDeviceInfoService;
import cc.mrbird.febs.cos.service.IDeviceOfflineRecordService;
import cc.mrbird.febs.cos.service.IOperateRecordInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 设备管理 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/device-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceInfoController {

    private final IDeviceInfoService deviceInfoService;

    private final IUserInfoService userInfoService;

    private final IDeviceOfflineRecordService deviceOfflineRecordService;

    private final IOperateRecordInfoService operateRecordInfoService;

    /**
     * 分页获取设备管理信息
     *
     * @param page       分页对象
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DeviceInfo> page, DeviceInfo deviceInfo) {
        return R.ok(deviceInfoService.selectDevicePage(page, deviceInfo));
    }

    /**
     * 根据用户获取设备在线状态
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/selectDeviceStatusByUser")
    public R selectDeviceStatusByUser(Integer userId) {
        return R.ok();
    }

    /**
     * 查询设备管理信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(deviceInfoService.getById(id));
    }

    /**
     * 查询设备管理信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(deviceInfoService.list());
    }

    /**
     * 新增设备管理信息
     *
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping
    public R save(DeviceInfo deviceInfo) {
        // 所属用户
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, deviceInfo.getUserId()));
        if (userInfo != null) {
            deviceInfo.setUserId(userInfo.getUserId());
        }
        // 创建时间
        deviceInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        deviceInfoService.save(deviceInfo);
        if (StrUtil.isNotEmpty(deviceInfo.getOnlineFlag())) {
            DeviceOfflineRecord deviceOfflineRecord = new DeviceOfflineRecord();
            deviceOfflineRecord.setType(deviceInfo.getOnlineFlag());
            deviceOfflineRecord.setOnlineDate(DateUtil.formatDateTime(new Date()));
            deviceOfflineRecord.setDeviceId(deviceInfo.getId());
            deviceOfflineRecordService.save(deviceOfflineRecord);
        }
        return R.ok(true);
    }

    /**
     * 修改设备管理信息
     *
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    @PutMapping
    public R edit(DeviceInfo deviceInfo) {
        if (StrUtil.isNotEmpty(deviceInfo.getOnlineFlag())) {
            DeviceInfo historyDevice = deviceInfoService.getById(deviceInfo.getId());
            if (historyDevice != null && !historyDevice.getOnlineFlag().equals(deviceInfo.getOnlineFlag())) {
                DeviceOfflineRecord deviceOfflineRecord = new DeviceOfflineRecord();
                deviceOfflineRecord.setType(deviceInfo.getOnlineFlag());
                deviceOfflineRecord.setOnlineDate(DateUtil.formatDateTime(new Date()));
                deviceOfflineRecord.setDeviceId(deviceInfo.getId());
                deviceOfflineRecordService.save(deviceOfflineRecord);
            }
        }
        // 开关记录
        if (StrUtil.isNotEmpty(deviceInfo.getOpenFlag())) {
            OperateRecordInfo opRecord = new OperateRecordInfo();
            opRecord.setDeviceId(deviceInfo.getId());
            opRecord.setOpenFlag(deviceInfo.getOpenFlag());
            opRecord.setCreateDate(DateUtil.formatDateTime(new Date()));
            operateRecordInfoService.save(opRecord);
        }
        if ("2".equals(deviceInfo.getOpenFlag())) {
            deviceInfo.setOpenFlag("1");
        }
        return R.ok(deviceInfoService.updateById(deviceInfo));
    }

    /**
     * 删除设备管理信息
     *
     * @param ids ids
     * @return 设备管理信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(deviceInfoService.removeByIds(ids));
    }
}
