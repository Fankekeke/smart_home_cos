package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeviceInfo;
import cc.mrbird.febs.cos.dao.DeviceInfoMapper;
import cc.mrbird.febs.cos.service.IDeviceInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 设备管理 实现层
 *
 * @author FanK
 */
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements IDeviceInfoService {

    /**
     * 分页获取设备管理信息
     *
     * @param page       分页对象
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDevicePage(Page<DeviceInfo> page, DeviceInfo deviceInfo) {
        return baseMapper.selectDevicePage(page, deviceInfo);
    }
}
