package com.bank.controller.manager;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bank.Constants;
import com.bank.beans.Menu;
import com.bank.beans.User;
import com.bank.common.util.JsonUtil;
import com.bank.service.IMenuService;
import com.common.exception.CreateException;
import com.common.exception.DAOException;
import com.common.exception.DataNotFoundException;
import com.common.exception.DeleteException;
import com.common.exception.UpdateException;

/**
 * 处理菜单的新增、修改、删除等操作
 * @author Huzq
 *
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuController {
	private static Logger log = LoggerFactory.getLogger(MenuController.class);
	private static String ADD = "add";
	
	@Resource
	private IMenuService menuSerivce;
	
	@RequestMapping(value = "/loadMenus", method = RequestMethod.POST)
	public Menu loadMenus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Menu> data = new ArrayList<Menu>();
		try {
			data = menuSerivce.getAllEntities();
		} catch (DAOException e) {
			log.error("get menus occurs error . ", e);
		}
	    
	    String json = JSON.toJSONString(data);
	    response.setContentType("text/html;charset=UTF-8");
	    try {
			response.getWriter().write(json);
		} catch (IOException e) {
			log.error("", e);
			throw new IOException("", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Menu save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User) request.getSession().getAttribute(Constants.SESSION_AUTH_USER);
		String formData = request.getParameter("formData");
		String actionType = request.getParameter("actionType");
		//這裡做了時間格式的處理
		Object decodeJsonData = JsonUtil.Decode(formData);
		String formatdata = JSON.toJSONStringWithDateFormat(decodeJsonData, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
		JSONObject jsb = JSONObject.parseObject(formatdata);
		Menu menu = (Menu) JSON.toJavaObject(jsb, Menu.class);
		long menuId = menu.getId();
		
		if (ADD.equals(actionType)) {//user为空，做新增操作
			menu.setCreateUser(user);
			menu.setCreateTime(new Timestamp(System.currentTimeMillis()));
			try {
				menuSerivce.save(menu);
			} catch (DAOException e) {
				String msg = "Create menu occurs DAO error";
				log.error(msg, e);
				throw new DAOException(msg, e);
			} catch (CreateException e) {
				String msg = "Create menu occurs error";
				log.error(msg, e);
				throw new CreateException(msg, e);
			}
		} else {//userId不为空，做更新操作
			menu.setUpdateUser(user);
			menu.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			try {
				menuSerivce.update(menu);
			} catch (DAOException e) {
				String msg = "update privilege occurs DAO error. ";
				log.error(msg, e);
				throw new DAOException(msg, e);
			} catch (UpdateException e) {
				String msg = "update menu (pk:" + menu.getId() + ") occur errors";
				log.error(msg, e);
				throw new UpdateException(msg, e);
			} catch (DataNotFoundException e) {
				String msg = "delete menu not found (pk:" + menu.getId() + ")";
				log.error(msg, e);
				throw new UpdateException(msg, e);
			}
		}
		response.setContentType("text/html;charset=UTF-8");
	    try {
			response.getWriter().write(menuId + "");
		} catch (IOException e) {
			log.error("", e);
			throw new IOException("", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public boolean delete(@RequestParam("menuId") String menuId) throws Exception{
		try {
			if (StringUtils.isEmpty(menuId)) throw new DAOException("主键不能为空!");
			
			menuSerivce.delete(Long.valueOf(menuId));
			
		} catch (DAOException e) {
			log.error("", e);
			throw new DAOException("", e);
		} catch (DeleteException e) {
			String msg = "delete menu occur errors";
			log.error(msg, e);
			throw new DeleteException(msg, e);
		} catch (DataNotFoundException e) {
			String msg = "delete menu not found (pk:" + menuId + ")";
			log.error(msg, e);
			throw new DataNotFoundException(msg, e);
		}
		return true;
	}
	
}
