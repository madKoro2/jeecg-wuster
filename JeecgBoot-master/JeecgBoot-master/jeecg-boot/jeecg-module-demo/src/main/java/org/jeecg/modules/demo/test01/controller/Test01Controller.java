package org.jeecg.modules.demo.test01.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.test01.entity.Test01;
import org.jeecg.modules.demo.test01.service.ITest01Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 第一个测试
 * @Author: jeecg-boot
 * @Date:   2024-10-31
 * @Version: V1.0
 */
@Api(tags="第一个测试")
@RestController
@RequestMapping("/test01/test01")
@Slf4j
public class Test01Controller extends JeecgController<Test01, ITest01Service> {
	@Autowired
	private ITest01Service test01Service;
	
	/**
	 * 分页列表查询
	 *
	 * @param test01
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "第一个测试-分页列表查询")
	@ApiOperation(value="第一个测试-分页列表查询", notes="第一个测试-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Test01>> queryPageList(Test01 test01,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<Test01> queryWrapper = QueryGenerator.initQueryWrapper(test01, req.getParameterMap());
		if (test01.getTestcontent() != null && !test01.getTestcontent().isEmpty()) {
			// 将 * 替换为 %
			log.info("searchContent:{}", test01.getTestcontent());
			String searchContent = test01.getTestcontent().replace("*", "%");
			log.info("searchContent:{}", searchContent);
			queryWrapper.like("testcontent", searchContent); // 对 testcontent 字段进行模糊查询
		}

		Page<Test01> page = new Page<Test01>(pageNo, pageSize);
		IPage<Test01> pageList = test01Service.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param test01
	 * @return
	 */
	@AutoLog(value = "第一个测试-添加")
	@ApiOperation(value="第一个测试-添加", notes="第一个测试-添加")
	@RequiresPermissions("test01:test01:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Test01 test01) {
		test01Service.save(test01);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param test01
	 * @return
	 */
	@AutoLog(value = "第一个测试-编辑")
	@ApiOperation(value="第一个测试-编辑", notes="第一个测试-编辑")
	@RequiresPermissions("test01:test01:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Test01 test01) {
		test01Service.updateById(test01);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "第一个测试-通过id删除")
	@ApiOperation(value="第一个测试-通过id删除", notes="第一个测试-通过id删除")
	@RequiresPermissions("test01:test01:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		test01Service.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "第一个测试-批量删除")
	@ApiOperation(value="第一个测试-批量删除", notes="第一个测试-批量删除")
	@RequiresPermissions("test01:test01:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.test01Service.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "第一个测试-通过id查询")
	@ApiOperation(value="第一个测试-通过id查询", notes="第一个测试-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Test01> queryById(@RequestParam(name="id",required=true) String id) {
		Test01 test01 = test01Service.getById(id);
		if(test01==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(test01);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param test01
    */
    @RequiresPermissions("test01:test01:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Test01 test01) {
        return super.exportXls(request, test01, Test01.class, "第一个测试");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("test01:test01:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Test01.class);
    }

}
