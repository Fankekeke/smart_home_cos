package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.DeviceTypeMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserInfoMapper userInfoMapper;

    private final IBulletinInfoService bulletinInfoService;


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
     * 获取首页统计数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData() {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("userNum", 0);
                put("deviceNum", 0);
                put("historyNum", 0);
                put("alertNum", 0);
            }
        };

        result.put("userNum", userInfoMapper.selectCount(Wrappers.<UserInfo>lambdaQuery()));
        result.put("deviceNum", deviceInfoService.count(Wrappers.<DeviceInfo>lambdaQuery()));
        result.put("historyNum", deviceHistoryInfoService.count(Wrappers.<DeviceHistoryInfo>lambdaQuery()));
        result.put("alertNum", messageInfoService.count());

        int year = DateUtil.thisYear();
        int month = DateUtil.thisMonth() + 1;
        // 本月数据上报数量
        result.put("monthNum", baseMapper.selectDataByMonth(year, month));
        // 本月数据报警数量
        result.put("monthAlertNum", baseMapper.selectAlertByMonth(year, month));

        // 本年数据上报数量
        result.put("yearNum", baseMapper.selectDataByMonth(year, null));
        // 本年数据报警数量
        result.put("yearAlertNum", baseMapper.selectAlertByMonth(year, null));

        // 近十天数据上报数量
        result.put("numDayList", baseMapper.selectDataNumWithinDays(null));
        // 近十天数据上报数量
        result.put("alertDayList", baseMapper.selectAlertNumWithinDays(null));
        // 公告信息
        result.put("bulletin", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDeviceRecordMqtt(String message) {
        DeviceHistoryInfo historyInfo = JSONUtil.toBean(message, DeviceHistoryInfo.class);
        if (historyInfo == null) {
            return;
        }

        if (StrUtil.isNotEmpty(historyInfo.getValueType()) && "event".equals(historyInfo.getValueType())) {
            deviceInfoService.eventCheck(historyInfo.getEventId());
        } else {
            // 获取此设备信息
            DeviceInfo deviceInfo = deviceInfoService.getById(historyInfo.getDeviceId());
            // 获取设备报警值
            DeviceAlertInfo alert = deviceAlertInfoService.getOne(Wrappers.<DeviceAlertInfo>lambdaQuery().eq(DeviceAlertInfo::getDeviceId, historyInfo.getDeviceId()).eq(DeviceAlertInfo::getType, "2"));

            deviceInfo.setDeviceValue(historyInfo.getDeviceValue());
            if (alert != null) {
                historyInfo.setAlertValue(String.valueOf(alert.getScore()));
                if (Integer.parseInt(historyInfo.getDeviceValue()) >= alert.getScore()) {
                    // 添加报警消息
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
                    messageInfo.setSendUser(deviceInfo.getUserId());
                    messageInfo.setContent("你好，您的设备 " + deviceInfo.getName() + " 触发自定义报警，报警值为" + historyInfo.getDeviceValue() +"，请尽快查看处理");
                    messageInfo.setReadStatus(0);
                    messageInfoService.save(messageInfo);
                }
            }
            deviceHistoryInfoService.save(historyInfo);
            deviceInfoService.updateById(deviceInfo);
        }
    }

    /**
     * 定时任务设置设备上报数据
     */
//    @Scheduled(fixedRate = 300000)
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
