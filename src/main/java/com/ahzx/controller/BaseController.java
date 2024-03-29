package com.ahzx.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.ahzx.core.domain.AjaxResult;
import com.ahzx.core.domain.AjaxResult.Type;
import com.ahzx.core.page.PageDomain;
import com.ahzx.core.page.TableDataInfo;
import com.ahzx.core.page.TableSupport;
import com.ahzx.utils.DateUtils;
import com.ahzx.utils.ServletUtils;
import com.ahzx.utils.StringUtils;
import com.ahzx.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * web层通用数据处理
 * 
 * 
 */
public class BaseController {

	/**
	 * 将前台传递过来的日期格式的字符串，自动转化为Date类型
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	/**
	 * 设置请求分页数据
	 */
	protected void startPage() {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();
		if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
			String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
			PageHelper.startPage(pageNum, pageSize, orderBy);
		}
	}

	/**
	 * 手动设置参数 设置请求分页数据
	 */
	protected void startPage(int pageNum, int pageSize) {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		pageDomain.setPageNum(pageNum);
		pageDomain.setPageSize(pageSize);
		if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
			String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
			PageHelper.startPage(pageNum, pageSize, orderBy);
		}
	}

	/**
	 * 手动设置参数 设置请求分页数据
	 */
	protected void startPage(int pageNum, int pageSize, String orderByColumn, String isAsc) {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		pageDomain.setPageNum(pageNum);
		pageDomain.setPageSize(pageSize);
		pageDomain.setOrderByColumn(orderByColumn);
		pageDomain.setIsAsc(isAsc);
		if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
			String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
			PageHelper.startPage(pageNum, pageSize, orderBy);
		}
	}

	/**
	 * 获取request
	 */
	public HttpServletRequest getRequest() {
		return ServletUtils.getRequest();
	}

	/**
	 * 获取response
	 */
	public HttpServletResponse getResponse() {
		return ServletUtils.getResponse();
	}

	/**
	 * 获取session
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 响应请求分页数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected TableDataInfo getDataTable(List<?> list) {
		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setData(list);
		rspData.setCount(new PageInfo(list).getTotal());
		return rspData;
	}

	/**
	 * 响应返回结果
	 * 
	 * @param rows 影响行数
	 * @return 操作结果
	 */
	protected AjaxResult toAjax(int rows) {
		return rows > 0 ? success() : error();
	}

	/**
	 * 响应返回结果
	 * 
	 * @param result 结果
	 * @return 操作结果
	 */
	protected AjaxResult toAjax(boolean result) {
		return result ? success() : error();
	}

	/**
	 * 返回成功
	 */
	public AjaxResult success() {
		return AjaxResult.success();
	}

	/**
	 * 返回失败消息
	 */
	public AjaxResult error() {
		return AjaxResult.error();
	}

	/**
	 * 返回成功消息
	 */
	public AjaxResult success(String message) {
		return AjaxResult.success(message);
	}

	/**
	 * 返回失败消息
	 */
	public AjaxResult error(String message) {
		return AjaxResult.error(message);
	}

	/**
	 * 返回错误码消息
	 */
	public AjaxResult error(Type type, String message) {
		return new AjaxResult(type, message);
	}

	/**
	 * 页面跳转
	 */
	public String redirect(String url) {
		return StringUtils.format("redirect:{}", url);
	}
}
