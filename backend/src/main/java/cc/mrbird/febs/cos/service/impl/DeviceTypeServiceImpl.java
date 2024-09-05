package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeviceAlertInfo;
import cc.mrbird.febs.cos.entity.DeviceHistoryInfo;
import cc.mrbird.febs.cos.entity.DeviceInfo;
import cc.mrbird.febs.cos.entity.DeviceType;
import cc.mrbird.febs.cos.dao.DeviceTypeMapper;
import cc.mrbird.febs.cos.service.IDeviceAlertInfoService;
import cc.mrbird.febs.cos.service.IDeviceInfoService;
import cc.mrbird.febs.cos.service.IDeviceTypeService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备类型 实现层
 *
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

    private final IDeviceInfoService deviceInfoService;

    private final IDeviceAlertInfoService deviceAlertInfoService;

    /**
     * 分页获取设备类型信息
     *
     * @param page       分页对象
     * @param deviceType 设备类型信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDeviceTypePage(Page<DeviceType> page, DeviceType deviceType) {
        return baseMapper.selectDeviceTypePage(page, deviceType);
    }

    /**
     * 定时任务设置设备上报数据
     */
    private void setDeviceRecord() {
        // 获取设备信息
        List<DeviceInfo> deviceInfoList = deviceInfoService.list();
        if (CollectionUtil.isEmpty(deviceInfoList)) {
            return;
        }

        // 设备报警配置
        List<DeviceAlertInfo> deviceAlertInfoList = deviceAlertInfoService.list();
        Map<Integer, DeviceAlertInfo> deviceAlertMap = deviceAlertInfoList.stream().collect(Collectors.toMap(DeviceAlertInfo::getDeviceId, e -> e));

        List<DeviceHistoryInfo> historyInfoList = new ArrayList<>();
        for (DeviceInfo device : deviceInfoList) {
            DeviceHistoryInfo historyInfo = new DeviceHistoryInfo();
            historyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            historyInfo.setDeviceId(device.getId());
            // 获取此设备报警配置
            DeviceAlertInfo alert = deviceAlertMap.get(device.getId());
            if (alert != null) {
                historyInfo.setAlertValue(String.valueOf(alert.getScore()));
                // 根据设备类型设置设备值
                historyInfo.setDeviceValue("0");
            }
        }
    }

    public void setDeviceAlert() {
        // 获取设备信息
        List<DeviceInfo> deviceInfoList = deviceInfoService.list();
        // 设备报警配置
        List<DeviceAlertInfo> deviceAlertInfoList = deviceAlertInfoService.list();
        Map<Integer, DeviceAlertInfo> deviceAlertMap = deviceAlertInfoList.stream().collect(Collectors.toMap(DeviceAlertInfo::getDeviceId, e -> e));

        for (DeviceInfo device : deviceInfoList) {
            // 获取此设备报警配置
            DeviceAlertInfo alert = deviceAlertMap.get(device.getId());
            if (alert == null) {
                continue;
            }
        }
    }
}
