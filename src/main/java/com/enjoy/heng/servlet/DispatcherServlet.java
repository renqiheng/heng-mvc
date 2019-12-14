package com.enjoy.heng.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoy.heng.annotation.EnjoyController;
import com.enjoy.heng.annotation.EnjoyQualifier;
import com.enjoy.heng.annotation.EnjoyRequestMapping;
import com.enjoy.heng.annotation.EnjoyService;
import com.enjoy.heng.controller.HengController;
import com.enjoy.heng.hand.HandToolsService;

public class DispatcherServlet extends HttpServlet{
	
	List<String> classNames = new ArrayList<String>();
	Map<String,Object> beans = new HashMap<String,Object>(16);
	Map<String,Object> handMap = new HashMap<String,Object>();

	public DispatcherServlet() {
		System.out.println("DispatcherServlet constructor....");
	}
	
	public void init(ServletConfig config) throws ServletException {
		
		//1.ɨ����Ҫʵ�������� class(���������µ�class)
		doScanPackage("com.enjoy");
		for(String cname : classNames){
			System.out.println(cname);
		}
		
		//2.classNames��������beanȫ����·��,������classʵ����
		doInstance();
		
		//3.����ע�룬��service����׵Ķ���ע�뵽controller
		iocDi();
		
		//4.����һ��URL��method��ӳ���ϵ
		hengHandMapper();
		for(Entry<String, Object> entry : handMap.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//�õ�heng-mvc/heng/query
		String uri = req.getRequestURI();
		String context = req.getContextPath();//�õ�heng-mvc
		String path = uri.replace(context, "");
		
		Method method = (Method)handMap.get(path);
		
		//TODO ������
		HengController instance = (HengController) beans.get("/"+path.split("/")[1]);
		//������-����ģʽ
		HandToolsService hand = (HandToolsService) beans.get("hengHandTool");
		
		Object[] args = hand.hand(req, resp, method, beans);
		
		try {
			method.invoke(instance, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	//ɨ��class��
	private void doScanPackage(String basePackage){
		//ɨ�����õ���Ŀ·���µ�������
		URL url = this.getClass().getClassLoader().getResource("/"+basePackage.replaceAll("\\.", "/"));
		if(url == null){
			System.out.println(basePackage);
			return;
		}
		String fileStr = url.getFile();
		File file = new File(fileStr);
		
		String[] files = file.list();
		for(String path : files){
			File filePath = new File(fileStr + path);//����·��+com.enjoy
			
			//�����·��
			if(filePath.isDirectory()){
				doScanPackage(basePackage+"."+path);
			}else{
				classNames.add(basePackage+"."+filePath.getName());//com/enjoy/xxxService.class
			}
		}
	}
	
	//ʵ����������
	public void doInstance(){
		if(classNames.size() <= 0){
			System.out.println("doScanFailed..................................");
			return;
		}
		
		//����ɨ�赽��classȫ����·�������䴴������
		for(String className : classNames){
			//ȥ�������.class���з���
			String cn = className.replace(".class", "");
			
			try {
				Class<?> clazz = Class.forName(cn);
				//�жϵ�ǰ���Ƿ���EnjoyControllerע��
				if(clazz.isAnnotationPresent(EnjoyController.class)){
					//��ȡEnjoyControllerע��
					EnjoyController controller = clazz.getAnnotation(EnjoyController.class);
					Object instance = clazz.newInstance();//�õ�ʵ������bean
					
					EnjoyRequestMapping requestMapping = clazz.getAnnotation(EnjoyRequestMapping.class);
					String key = requestMapping.value();
					beans.put(key, instance);
				}else if(clazz.isAnnotationPresent(EnjoyService.class)){
					EnjoyService service = clazz.getAnnotation(EnjoyService.class);
					Object instance = clazz.newInstance();
					
					beans.put(service.value(), instance);
				}else{
					continue;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch(InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void iocDi(){
		if(beans.entrySet().size() <= 0){
			System.out.println("��û�б�ʵ����.......................");
			return;
		}
		
		//��serviceע�뵽controller
		for(Map.Entry<String, Object> entry : beans.entrySet()){
			Object instance = entry.getValue();
			//��ȡ��,��ȡ��������������Щע��
			Class<?> clazz = instance.getClass();
			if(clazz.isAnnotationPresent(EnjoyController.class)){
				Field[] fields = clazz.getDeclaredFields();
				for(Field field : fields){
					if(field.isAnnotationPresent(EnjoyQualifier.class)){
						EnjoyQualifier qualifier = field.getAnnotation(EnjoyQualifier.class);
						String value = qualifier.value();
						
						//��˽�����Էſ�Ȩ��
						field.setAccessible(true);
						try {
							field.set(instance, beans.get(value));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}else{
						continue;
					}
				}
			}else{
				continue;
			}
		}
	}
	
	//����ӳ���ϵ
	private void hengHandMapper(){
		if(beans.entrySet().size() <= 0){
			System.out.println("��û��ʵ����........................");
			return;
		}
		
		for(Map.Entry<String, Object> entry : beans.entrySet()){
			Object instance = entry.getValue();
			Class<?> clazz = instance.getClass();
			if(clazz.isAnnotationPresent(EnjoyController.class)){
				EnjoyRequestMapping requestMapping = clazz.getAnnotation(EnjoyRequestMapping.class);
				String classPath = requestMapping.value();
				
				Method[] methods = clazz.getMethods();
				for(Method method : methods){
					if(method.isAnnotationPresent(EnjoyRequestMapping.class)){
						EnjoyRequestMapping mapping = method.getAnnotation(EnjoyRequestMapping.class);
						String methodUrl = mapping.value();
						handMap.put(classPath+methodUrl, method);
					}else{
						continue;
					}
				}
			}
		}
	}
}
