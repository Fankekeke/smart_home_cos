package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeviceHistoryInfo;
import cc.mrbird.febs.cos.dao.DeviceHistoryInfoMapper;
import cc.mrbird.febs.cos.service.IDeviceHistoryInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备上报历史数据 实现层
 *
 * @author FanK
 */
@Service
public class DeviceHistoryInfoServiceImpl extends ServiceImpl<DeviceHistoryInfoMapper, DeviceHistoryInfo> implements IDeviceHistoryInfoService {

    /**
     * 分页获取设备上报历史数据信息
     *
     * @param page              分页对象
     * @param deviceHistoryInfo 设备上报历史数据信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectHistoryPage(Page<DeviceHistoryInfo> page, DeviceHistoryInfo deviceHistoryInfo) {
        return baseMapper.selectHistoryPage(page, deviceHistoryInfo);
    }

    /**
     * 根据设备ID获取统计信息
     *
     * @param deviceId 设备ID
     * @param date     统计日期
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectRateByDeviceId(Integer deviceId, String date) {
        if (StrUtil.isEmpty(date)) {
            date = DateUtil.formatDate(new Date());
        }
        List<DeviceHistoryInfo> historyList = baseMapper.selectRateByDeviceId(deviceId, date);
        Map<Integer, List<DeviceHistoryInfo>> historyMap = historyList.stream().collect(Collectors.groupingBy(DeviceHistoryInfo::getHour));
        // 返回数据
        List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < 24 ; i++) {
            List<DeviceHistoryInfo> itemList = historyMap.get(i);
            BigDecimal value = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(itemList)) {
                value = BigDecimal.valueOf(itemList.stream().mapToDouble(DeviceHistoryInfo::getDeviceValue1).average().orElse(0));
            }
            int finalI = i;
            BigDecimal finalValue = value;
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("date", finalI + "时");
                    put("value", NumberUtil.round(finalValue, 0));
                }
            };
            resultList.add(item);
        }
        return resultList;
    }
}
