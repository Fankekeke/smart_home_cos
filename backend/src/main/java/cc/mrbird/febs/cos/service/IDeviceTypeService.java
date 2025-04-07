package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DeviceType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 设备类型 service层
 *
 * @author FanK
 */
public interface IDeviceTypeService extends IService<DeviceType> {

    /**
     * 分页获取设备类型信息
     *
     * @param page       分页对象
     * @param deviceType 设备类型信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDeviceTypePage(Page<DeviceType> page, DeviceType deviceType);

    /**
     * 获取首页统计数据
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> homeData();

    /**
     * mqtt数据解析
     *
     * @param message 消息内容
     */
    void setDeviceRecordMqtt(String message);
}
