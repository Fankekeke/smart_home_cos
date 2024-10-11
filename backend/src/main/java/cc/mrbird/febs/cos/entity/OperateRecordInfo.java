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
 * 操作记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OperateRecordInfo implements Serializable {

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
     * 设备开关状态（0.关闭 1.开启 2.重启）
     */
    private String openFlag;

    /**
     * 设备值
     */
    private String deviceValue;

    /**
     * 设备值（旧）
     */
    private String deviceOldValue;

    /**
     * 操作时间
     */
    private String createDate;

    @TableField(exist = false)
    private String deviceName;

    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String userId;
}
