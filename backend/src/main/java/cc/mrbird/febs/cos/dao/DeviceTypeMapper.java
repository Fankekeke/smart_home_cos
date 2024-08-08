package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DeviceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 设备类型 mapper层
 *
 * @author FanK
 */
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {

    /**
     * 分页获取设备类型信息
     *
     * @param page       分页对象
     * @param deviceType 设备类型信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDeviceTypePage(Page<DeviceType> page, @Param("deviceType") DeviceType deviceType);
}
