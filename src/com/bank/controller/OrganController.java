package com.bank.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bank.beans.Organ;
import com.bank.service.IOrganService;

/**
 * 处理机构的新增、修改、删除等操作
 * @author Huzq
 *
 */
@Controller
@RequestMapping(value = "/organ")
public class OrganController {
	@Resource
	private IOrganService organSerivce;
	
	@RequestMapping(value = "/loadOrgan", method = RequestMethod.POST)
	public Organ loadOrgan(@RequestParam(value="organId",required=true) String organId, HttpServletResponse response) throws Exception {
		Organ Organ = organSerivce.loadOrgan(organId);
		String json = JSON.toJSONString(Organ);
		response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(json);
		return null;
	}
	
	@RequestMapping(value = "/saveOrgan", method = RequestMethod.POST)
	public Organ saveOrgan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String formData = request.getParameter("formData");
		String actionType = request.getParameter("actionType");
		//Object obj = JSON.toJSONStringWithDateFormat(formData, formData, arg2);
		JSONObject jsb = JSONObject.parseObject(formData); //将json串转成JSONObject
		Organ Organ = (Organ) JSONObject.toJavaObject(jsb, Organ.class);
		String organId = "";
		if ("add".equals(actionType)) {//organId为空，做新增操作
			organId = organSerivce.saveOrgan(Organ);
		} else {//organId不为空，做更新操作
			organSerivce.updateOrgan(Organ);
			organId = Organ.getOrganId();
		}
		response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(organId);
		return null;
	}
	
	@RequestMapping(value = "/deleteOrgan", method = RequestMethod.POST)
	public boolean deleteOrgan(@RequestParam("organId") String organId){
		boolean flag = organSerivce.deleteOrgan(organId);
		return flag;
	}
	
	@RequestMapping(value = "/loadAllOrgans", method = RequestMethod.POST)
	public Organ loadAllOrgans(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//查询条件
	    String key = request.getParameter("submitData");
	    //分页
	    int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
	    int pageSize = Integer.parseInt(request.getParameter("pageSize"));        
	    //字段排序
	    String sortField = request.getParameter("sortField");
	    String sortOrder = request.getParameter("sortOrder");
	    
	    List<Organ> data = organSerivce.loadAllOrgans(key, pageIndex, pageSize, sortField, sortOrder);
	    
	    HashMap result = new HashMap();
        result.put("data", data);
        result.put("total", data.size());
        
	    String json = JSON.toJSONString(result);
	    response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(json);
		return null;
	}
	
	@RequestMapping(value = "/organUserTree", method = RequestMethod.POST)
	public Organ organUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<?> list = organSerivce.getOrganUserTree();
		JSONArray arr = (JSONArray) JSONArray.toJSON(list);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(arr.toString());
		return null;
	}	
}
