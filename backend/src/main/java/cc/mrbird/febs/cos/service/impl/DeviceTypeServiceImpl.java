package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.DeviceTypeMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final IDeviceHistoryInfoService deviceHistoryInfoService;

    private final IMessageInfoService messageInfoService;

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
    @Scheduled(fixedRate = 300000)
    public void setDeviceRecord() {
        // 获取设备信息
        List<DeviceInfo> deviceInfoList = deviceInfoService.list();
        if (CollectionUtil.isEmpty(deviceInfoList)) {
            return;
        }

        // 设备报警配置
        List<DeviceAlertInfo> deviceAlertInfoList = deviceAlertInfoService.list(Wrappers.<DeviceAlertInfo>lambdaQuery().eq(DeviceAlertInfo::getType, "2"));
        Map<Integer, DeviceAlertInfo> deviceAlertMap = deviceAlertInfoList.stream().collect(Collectors.toMap(DeviceAlertInfo::getDeviceId, e -> e));

        // 上报数据
        List<DeviceHistoryInfo> historyInfoList = new ArrayList<>();

        // 报警消息
        List<MessageInfo> messageInfoList = new ArrayList<>();
        for (DeviceInfo device : deviceInfoList) {
            DeviceHistoryInfo historyInfo = new DeviceHistoryInfo();
            historyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            historyInfo.setDeviceId(device.getId());
            // 设置设备值
            historyInfo.setDeviceValue(StrUtil.toString(RandomUtil.randomInt(5, 38)));
            device.setDeviceValue(historyInfo.getDeviceValue());

            // 获取此设备报警配置
            DeviceAlertInfo alert = deviceAlertMap.get(device.getId());
            if (alert != null) {
                historyInfo.setAlertValue(String.valueOf(alert.getScore()));
                if (Integer.parseInt(historyInfo.getDeviceValue()) >= alert.getScore()) {
                    // 添加报警消息
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
                    messageInfo.setSendUser(device.getUserId());
                    messageInfo.setContent("你好，您的设备 " + device.getName() + " 触发自定义报警，报警值为" + historyInfo.getDeviceValue() +"，请尽快查看处理");
                    messageInfo.setReadStatus(0);
                    messageInfoList.add(messageInfo);
                }
            }
            historyInfoList.add(historyInfo);
        }
        deviceHistoryInfoService.saveBatch(historyInfoList);
        deviceInfoService.updateBatchById(deviceInfoList);
        messageInfoService.saveBatch(messageInfoList);
    }

    /**
     * 定时任务处理时常报警信息
     */
    @Scheduled(fixedRate = 350000)
    public void setDeviceAlert() {
        // 获取设备信息
        List<DeviceInfo> deviceInfoList = deviceInfoService.list(Wrappers.<DeviceInfo>lambdaQuery().eq(DeviceInfo::getOpenFlag, "1"));
        // 设备报警配置
        List<DeviceAlertInfo> deviceAlertInfoList = deviceAlertInfoService.list(Wrappers.<DeviceAlertInfo>lambdaQuery().eq(DeviceAlertInfo::getType, "1"));;
        Map<Integer, DeviceAlertInfo> deviceAlertMap = deviceAlertInfoList.stream().collect(Collectors.toMap(DeviceAlertInfo::getDeviceId, e -> e));

        // 报警消息
        List<MessageInfo> messageInfoList = new ArrayList<>();
        for (DeviceInfo device : deviceInfoList) {
            // 获取此设备报警配置
            DeviceAlertInfo alert = deviceAlertMap.get(device.getId());
            if (alert == null || device.getLastOpenDate() == null) {
                continue;
            }

            // 获取设备在线时常
            long minute = DateUtil.between(DateUtil.parseDate(device.getLastOpenDate()), new Date(), DateUnit.MINUTE);
            if (minute >= alert.getScore()) {
                // 添加报警消息
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
                messageInfo.setSendUser(device.getUserId());
                messageInfo.setContent("你好，您的设备 " + device.getName() + " 触发超时报警，当前运行为" + minute +"分钟，请尽快查看处理");
                messageInfo.setReadStatus(0);
                messageInfoList.add(messageInfo);
            }
        }
        messageInfoService.saveBatch(messageInfoList);
    }
}
