package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OperateRecordInfo;
import cc.mrbird.febs.cos.service.IOperateRecordInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 操作记录 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/operate-record-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OperateRecordInfoController {

    private final IOperateRecordInfoService operateRecordInfoService;

    /**
     * 分页获取操作记录信息
     *
     * @param page              分页对象
     * @param operateRecordInfo 操作记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OperateRecordInfo> page, OperateRecordInfo operateRecordInfo) {
        return R.ok(operateRecordInfoService.selectOperateRecordPage(page, operateRecordInfo));
    }

    /**
     * 查询操作记录信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(operateRecordInfoService.getById(id));
    }

    /**
     * 查询操作记录信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(operateRecordInfoService.list());
    }

    /**
     * 新增操作记录信息
     *
     * @param operateRecordInfo 操作记录信息
     * @return 结果
     */
    @PostMapping
    public R save(OperateRecordInfo operateRecordInfo) {
        // 创建时间
        operateRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(operateRecordInfoService.save(operateRecordInfo));
    }

    /**
     * 修改操作记录信息
     *
     * @param operateRecordInfo 操作记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(OperateRecordInfo operateRecordInfo) {
        return R.ok(operateRecordInfoService.updateById(operateRecordInfo));
    }

    /**
     * 删除操作记录信息
     *
     * @param ids ids
     * @return 操作记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(operateRecordInfoService.removeByIds(ids));
    }
}
