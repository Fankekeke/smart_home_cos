package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeviceAlertInfo;
import cc.mrbird.febs.cos.dao.DeviceAlertInfoMapper;
import cc.mrbird.febs.cos.service.IDeviceAlertInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 设备报警配置 实现层
 *
 * @author FanK
 */
@Service
public class DeviceAlertInfoServiceImpl extends ServiceImpl<DeviceAlertInfoMapper, DeviceAlertInfo> implements IDeviceAlertInfoService {

    /**
     * 分页获取设备报警配置信息
     *
     * @param page            分页对象
     * @param deviceAlertInfo 设备报警配置信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDeviceAlertPage(Page<DeviceAlertInfo> page, DeviceAlertInfo deviceAlertInfo) {
        return baseMapper.selectDeviceAlertPage(page, deviceAlertInfo);
    }
}
