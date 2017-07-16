package filter;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet Filter implementation class DisFilter
 */
@WebFilter(
		urlPatterns = { "/DisServlet" }, 
		initParams = { 
				@WebInitParam(name = "path", value = "c:")
		})
public class DisFilter implements Filter {

	
	private List<String> dirtyData;
    /**
     * Default constructor. 
     */
    public DisFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		final HttpServletRequest request1 = (HttpServletRequest) request;    
		HttpServletResponse response1 = (HttpServletResponse) response;
		
		System.out.println("the filter in doFilter function working ");
		// 一、处理公用业务
		request1.setCharacterEncoding("UTF-8");					// POST提交有效
		response1.setContentType("text/html;charset=UTF-8");
		
		/*
		 * 出现GET中文乱码，是因为在request.getParameter方法内部没有进行提交方式判断并处理。
		 * String name = request.getParameter("userName");
		 * 
		 * 解决：对指定接口的某一个方法进行功能扩展，可以使用代理!
		 *      对request对象(目标对象)，创建代理对象！
		 */
		System.out.println("the filter in doFilter function works ");
		HttpServletRequest proxy =  (HttpServletRequest) Proxy.newProxyInstance(
				request1.getClass().getClassLoader(), 		// 指定当前使用的累加载器
				new Class[]{HttpServletRequest.class}, 		// 对目标对象实现的接口类型
				new InvocationHandler() {					// 事件处理器
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						// 定义方法返回值
						Object returnValue = null;
						// 获取方法名
						String methodName = method.getName();
						// 判断：对getParameter方法进行GET提交中文处理
						if ("getParameter".equals(methodName)) {
							
							// 获取请求数据值【 <input type="text" name="userName">】
							String value = request1.getParameter(args[0].toString());	// 调用目标对象的方法
							System.out.println("原来的value"+value);
							// 获取提交方式
							String methodSubmit = request1.getMethod(); // 直接调用目标对象的方法
							
							// 判断如果是GET提交，需要对数据进行处理  (POST提交已经处理过了)
							if ("GET".equals(methodSubmit)) {
								System.out.println("能进入编码1吗");
								if (value != null && !"".equals(value.trim())){
									// 处理GET中文
//									value = new String(value.getBytes("ISO8859-1"),"UTF-8");
									System.out.println("能进入编码2吗");
									System.out.println("新的的value"+value);
								}
							} 
							for(String dirtyword:dirtyData)
							{
								if(value.contains(dirtyword))
								{
									value=value.replace(dirtyword, "xxxx");
								}
								
							}
							
							
							return value;
						}
						else {
							// 执行request对象的其他方法
							returnValue = method.invoke(request1, args);
						}
						
						return returnValue;
					}
				});
		
		// 二、放行 (执行下一个过滤器或者servlet)

		// pass the request along the filter chain
		chain.doFilter(proxy, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		dirtyData=new ArrayList<>();
		dirtyData.add("傻逼");
		dirtyData.add("草泥马");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	 System.out.println("have fun");
	
	 
	}

}
