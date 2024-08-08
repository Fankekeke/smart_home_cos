package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeviceHistoryInfo;
import cc.mrbird.febs.cos.dao.DeviceHistoryInfoMapper;
import cc.mrbird.febs.cos.service.IDeviceHistoryInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

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
}
