package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 设备上下线记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceOfflineRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设备ID
     */
    private Integer deviceId;

    /**
     * 上线时间
     */
    private String onlineDate;

    /**
     * 下线时间
     */
    private String offline;

    /**
     * 类型 0.下线 1.上线
     */
    private String type;

    @TableField(exist = false)
    private String deviceName;

    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String userId;
}
