package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DeviceOfflineRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 设备上下线记录 service层
 *
 * @author FanK
 */
public interface IDeviceOfflineRecordService extends IService<DeviceOfflineRecord> {

    /**
     * 分页获取设备上下线记录信息
     *
     * @param page                分页对象
     * @param deviceOfflineRecord 设备上下线记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOfflineRecordPage(Page<DeviceOfflineRecord> page, DeviceOfflineRecord deviceOfflineRecord);
}
