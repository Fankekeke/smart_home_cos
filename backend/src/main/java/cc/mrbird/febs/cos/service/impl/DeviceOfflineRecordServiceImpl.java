package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeviceOfflineRecord;
import cc.mrbird.febs.cos.dao.DeviceOfflineRecordMapper;
import cc.mrbird.febs.cos.service.IDeviceOfflineRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 设备上下线记录 实现层
 *
 * @author FanK
 */
@Service
public class DeviceOfflineRecordServiceImpl extends ServiceImpl<DeviceOfflineRecordMapper, DeviceOfflineRecord> implements IDeviceOfflineRecordService {

    /**
     * 分页获取设备上下线记录信息
     *
     * @param page                分页对象
     * @param deviceOfflineRecord 设备上下线记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOfflineRecordPage(Page<DeviceOfflineRecord> page, DeviceOfflineRecord deviceOfflineRecord) {
        return baseMapper.selectOfflineRecordPage(page, deviceOfflineRecord);
    }
}
