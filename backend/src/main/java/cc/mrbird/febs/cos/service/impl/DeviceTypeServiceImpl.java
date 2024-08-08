package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeviceType;
import cc.mrbird.febs.cos.dao.DeviceTypeMapper;
import cc.mrbird.febs.cos.service.IDeviceTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 设备类型 实现层
 *
 * @author FanK
 */
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

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
}
