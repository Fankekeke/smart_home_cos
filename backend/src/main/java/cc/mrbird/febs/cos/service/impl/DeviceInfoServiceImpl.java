package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceInfo;
import cc.mrbird.febs.cos.dao.DeviceInfoMapper;
import cc.mrbird.febs.cos.entity.DeviceOfflineRecord;
import cc.mrbird.febs.cos.entity.EventDetail;
import cc.mrbird.febs.cos.service.IDeviceInfoService;
import cc.mrbird.febs.cos.service.IDeviceOfflineRecordService;
import cc.mrbird.febs.cos.service.IEventDetailService;
import cc.mrbird.febs.cos.service.IOperateRecordInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 设备管理 实现层
 *
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements IDeviceInfoService {

    private final IEventDetailService eventDetailService;

    private final IDeviceOfflineRecordService deviceOfflineRecordService;

    private final IOperateRecordInfoService operateRecordInfoService;

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

    /**
     * 场景事件处理
     *
     * @param eventId 事件ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eventCheck(Integer eventId) {
        if (eventId == null) {
            return;
        }
        // 获取事件详情
        List<EventDetail> eventDetailList = eventDetailService.list(Wrappers.<EventDetail>lambdaQuery().eq(EventDetail::getEventId, eventId));
        if (CollectionUtil.isEmpty(eventDetailList)) {
            return;
        }

        // 待添加的上下线记录
        List<DeviceOfflineRecord> deviceOfflineRecordList = CollectionUtil.newArrayList();
        List<DeviceInfo> toUpdateList = CollectionUtil.newArrayList();
        for (EventDetail eventDetail : eventDetailList) {
            // 获取设备信息
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(eventDetail.getDeviceId());
            deviceInfo.setOnlineFlag(eventDetail.getOpenFlag());
            deviceInfo.setOpenFlag(eventDetail.getOpenFlag());
            toUpdateList.add(deviceInfo);

            // 设置上下线记录
            DeviceOfflineRecord deviceOfflineRecord = new DeviceOfflineRecord();
            deviceOfflineRecord.setType(eventDetail.getOpenFlag());
            deviceOfflineRecord.setOnlineDate(DateUtil.formatDateTime(new Date()));
            deviceOfflineRecord.setDeviceId(eventDetail.getDeviceId());
            deviceOfflineRecordList.add(deviceOfflineRecord);
        }


        deviceOfflineRecordService.saveBatch(deviceOfflineRecordList);
        this.updateBatchById(toUpdateList);
    }
}
